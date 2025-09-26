package com.yeling.yelingziblog.other.service.Impl;

import com.alibaba.fastjson2.JSON;
import com.yeling.yelingziblog.common.service.RedisService;
import com.yeling.yelingziblog.common.vo.response.WeatherResp;
import com.yeling.yelingziblog.other.dto.VisitMessage;
import com.yeling.yelingziblog.other.dto.Weather;
import com.yeling.yelingziblog.other.entity.ViewInfo;
import com.yeling.yelingziblog.other.service.VisitService;
import com.yeling.yelingziblog.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;




@Slf4j
@Service
public class VisitServiceImpl implements VisitService {


    @Value("${my.api.hz.id}")
    private String hzId;

    @Value("${my.api.hz.key}")
    private String hzKey;

    @Value("${my.api.hz.w1path}")
    private String w1Path;

    @Value("${my.api.hz.w2path}")
    private String w2Path;

    private final static String WEATHER_KEY = "weather:";

    private final static String ADDRESS_KEY = "address";

    private final RedisService redisService;
    private final RestTemplate restTemplate;

    private final RabbitTemplate rabbitTemplate;

    public VisitServiceImpl(RedisService redisService, RabbitTemplate rabbitTemplate) {
        this.redisService = redisService;
        this.restTemplate = buildRestTemplate();
        this.rabbitTemplate = rabbitTemplate;
    }

    /* 连接池化 RestTemplate */
    private RestTemplate buildRestTemplate() {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3_000);
        factory.setReadTimeout(3_000);
        return new RestTemplate(factory);
    }

    @Override
    public void recordVisit(String ip, User user) {
        String today = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String userIdent = user.getId() == 0 ? user.getNickname() : String.valueOf(user.getId());
        String key = "views:" + today;

        // 1. 快速判断：今天是否已记录
        Boolean exists = redisService.hExists(key, userIdent);
        if (Boolean.TRUE.equals(exists)) {
            log.info("[Visit] 已存在，跳过。ip={}, user={}", ip, userIdent);
            return;
        }

        // 2. 构造消息
        VisitMessage msg = new VisitMessage(ip, user.getId(), user.getNickname());

        // 3. 发送（无需等待确认，直接返回）
        rabbitTemplate.convertAndSend(
                "other.exchange",
                "other.visit",
                msg);
        log.info("[Visit] 发送异步消息完成，ip={}, user={}", ip, userIdent);
    }

    @Override
    public Weather getWeather(String userIdent, String ip, String deviceId) {
        // 1. 统一构建当前日期的天气key
        String weatherKey = WEATHER_KEY + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        // 2. 尝试获取用户地址缓存
        String address = getAddressFromCache(userIdent, deviceId);

        if (address != null) {
            // 3. 根据地址获取天气缓存
            Weather weather = getWeatherFromCache(weatherKey, address);
            if (weather != null) {
                weather.setUptime(LocalDateTime.now());
                return weather;
            }
            // 4. 缓存未命中，根据城市获取天气并缓存
            weather = getAndCacheWeatherByCity(address);
            if (weather != null) {
                cacheWeatherData(weatherKey, userIdent, weather);
                weather.setUptime(LocalDateTime.now());
                return weather;
            }

        }

        // 5. 地址缓存未命中，通过IP获取天气
        Weather weather = getWeatherByIp(ip);
        cacheWeatherData(weatherKey, userIdent, weather);
        weather.setUptime(LocalDateTime.now());
        return weather;
    }

    // 辅助方法：从缓存获取地址
    private String getAddressFromCache(String userIdent, String deviceId) {
        Object obj = redisService.hget(ADDRESS_KEY, userIdent);
        if (obj != null) return obj.toString();

        // 登录用户尝试获取游客缓存
        if (!userIdent.startsWith("游客")) {
            obj = redisService.hget(ADDRESS_KEY, "游客" + deviceId);
            if (obj != null) return obj.toString();
        }
        return null;
    }

    // 辅助方法：从缓存获取天气
    private Weather getWeatherFromCache(String weatherKey, String address) {
        Object obj = redisService.hget(weatherKey, WEATHER_KEY + address);
        if (obj != null) {
            return JSON.parseObject(obj.toString(), Weather.class);
        }
        return null;
    }

    // 辅助方法：根据城市获取天气
    private Weather getAndCacheWeatherByCity(String address) {
        String[] parts = address.split("-");
        if (parts.length < 2) return null;

        return getWeatherByCity(parts[0], parts[1]);
    }

    // 辅助方法：统一缓存天气数据
    private void cacheWeatherData(String weatherKey, String key, Weather weather) {
        if (weather == null) return;

        // 缓存天气数据
        String field = WEATHER_KEY + weather.getSheng() + "-" + weather.getShi();
        redisService.hset(weatherKey, field, JSON.toJSONString(weather));
        redisService.expire(weatherKey, 7, TimeUnit.DAYS);

        // 缓存地址信息
        redisService.hset(ADDRESS_KEY, key, field.substring(WEATHER_KEY.length()));
        redisService.expire(ADDRESS_KEY, 7, TimeUnit.DAYS);
    }


    @Override
    public void addVisitInfo(String ip, User user) {
        /* 1. 构造访问记录 */
        String userIdent = user.getId() == 0 ? user.getNickname() : String.valueOf(user.getId());
        LocalDateTime now = LocalDateTime.now();
        String todayKey = "views:" + now.format(DateTimeFormatter.ISO_LOCAL_DATE);

        /* 2. 调用第三方接口取天气 */
        Weather weather = getWeatherByIp(ip);

        /* 3. 组装 ViewInfo */
        ViewInfo viewInfo = new ViewInfo();
        viewInfo.setIp(ip);
        viewInfo.setCity(weather.getGuo() + weather.getSheng() + weather.getShi());
        viewInfo.setCreateTime(now);
        viewInfo.setUserId(user.getId());
        viewInfo.setNickname(user.getNickname());

        /* 4. 写 Redis：按天 Hash，field=userIdent，30 天过期 */
        redisService.hset(todayKey, userIdent, viewInfo);
        redisService.expire(todayKey, 30, TimeUnit.DAYS);

        /* 5. 总访问量自增 */
        redisService.increment("dayView", 1);

        /* 6. 把天气原始数据再存一份，方便后台查看 */
        String weatherKey = WEATHER_KEY + now.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String field = WEATHER_KEY + weather.getSheng() + "-" +weather.getShi();

        redisService.hset(weatherKey, field, JSON.toJSONString(weather));
        redisService.expire(weatherKey, 7, TimeUnit.DAYS);
        redisService.hset(ADDRESS_KEY, userIdent, weather.getSheng() + "-" +weather.getShi());
        redisService.expire(ADDRESS_KEY, 7, TimeUnit.DAYS);
    }

    /* 真正发 HTTP 请求的地方 */
    private Weather getWeatherByIp(String ip) {
        try {
            String url = UriComponentsBuilder.fromUriString(w1Path)
                    .queryParam("id", hzId)
                    .queryParam("key", hzKey)
                    .queryParam("ip", ip)
                    .toUriString();

            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            if (resp.getStatusCode() != HttpStatus.OK || resp.getBody() == null) {
                log.warn("[Weather] 调用失败，ip={}，status={}", ip, resp.getStatusCode());
                return unknownWeather();
            }

            /* 假设返回体就是 Weather，字段名与类一致；不成功就捕获异常 */
            return JSON.parseObject(resp.getBody(), Weather.class);

        } catch (Exception e) {
            log.error("[Weather] 调用异常，ip=" + ip, e);
            return unknownWeather();
        }
    }

    private Weather getWeatherByCity(String sheng, String shi) {
        try {
            String url = UriComponentsBuilder.fromUriString(w2Path)
                    .queryParam("id", hzId)
                    .queryParam("key", hzKey)
                    .queryParam("sheng", sheng)
                    .queryParam("place", shi)
                    .toUriString();

            ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
            if (resp.getStatusCode() != HttpStatus.OK || resp.getBody() == null) {
                log.warn("[Weather] 调用失败，place={}-{}，status={}", sheng, shi, resp.getStatusCode());
                return unknownWeather();
            }

            /* 假设返回体就是 Weather，字段名与类一致；不成功就捕获异常 */
            return JSON.parseObject(resp.getBody(), Weather.class);

        } catch (Exception e) {
            log.error("[Weather] 调用异常，place={}-{}", sheng, shi, e);
            return unknownWeather();
        }
    }



    /* 兜底：接口超时/异常时返回未知地址，保证主流程继续 */
    private Weather unknownWeather() {
        Weather w = new Weather();
        w.setGuo("未知");
        w.setSheng("未知");
        w.setShi("未知");
        return w;
    }
}

