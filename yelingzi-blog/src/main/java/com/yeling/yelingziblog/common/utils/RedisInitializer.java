package com.yeling.yelingziblog.common.utils;

import com.yeling.yelingziblog.other.entity.Statistics;
import com.yeling.yelingziblog.common.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
public class RedisInitializer implements CommandLineRunner {

    private final RedisService redisService;
    private final boolean initDataEnabled;
    private final String keyPrefix;

    private final StatisticsUtils statisticsUtils;


    public RedisInitializer(RedisService redisService,
                            @Value("${app.redis.init-data.enabled}") boolean initDataEnabled,
                            @Value("${app.redis.init-data.key-prefix}") String keyPrefix,
                            StatisticsUtils statisticsUtils) {
        this.redisService = redisService;
        this.initDataEnabled = initDataEnabled;
        this.keyPrefix = keyPrefix;
        this.statisticsUtils = statisticsUtils;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!initDataEnabled) {
            return;
        }

        try {
            Statistics stats = statisticsUtils.getStatistics();

            redisService.set("statistics", stats);

            redisService.set("dayView", 0);

            System.out.println("Redis数据更新完成！");
        } catch (Exception e) {
            System.err.println("更新Redis数据时出错：" + e.getMessage());
            e.printStackTrace();
        }

    }

}
