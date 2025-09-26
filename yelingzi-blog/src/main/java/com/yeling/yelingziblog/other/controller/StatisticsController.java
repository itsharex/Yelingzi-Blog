package com.yeling.yelingziblog.other.controller;

import com.yeling.yelingziblog.other.entity.Statistics;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.other.entity.Views;
import com.yeling.yelingziblog.article.vo.response.ArticleAndTalkStatisticsResp;
import com.yeling.yelingziblog.common.vo.response.ViewInfoResp;
import com.yeling.yelingziblog.other.service.StatisticsService;
import com.yeling.yelingziblog.common.dto.UserContext;
import com.yeling.yelingziblog.common.utils.IpUtils;
import com.yeling.yelingziblog.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取统计数据
     *
     * @return 结果
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/api/admin/home/statistics")
    public ApiResponse getStatistics() {

        User user = UserContext.getUser();

        log.info("获取统计数据, 管理员：{},名:{}",  Objects.requireNonNull(user).getId(), user.getNickname());

        Statistics statistics =  statisticsService.getStatistics();
        if(statistics == null){
            return ApiResponse.error("获取统计数据失败");
        }

        return ApiResponse.success(statistics);
    }

    /**
     * 获取文章说说发布的统计数据
     *
     * @return 结果
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/api/admin/home/update")
    public ApiResponse getArticleAndTalkStatistics() {

        User user = UserContext.getUser();

        log.info("获取文章说说发布的统计数据, 管理员：{},名:{}",  Objects.requireNonNull(user).getId(), user.getNickname());

        List<ArticleAndTalkStatisticsResp> list =  statisticsService.getArticleAndTalkStatistics();
        if(list.isEmpty()){
            return ApiResponse.error("获取文章说说发布的统计数据失败");
        }

        return ApiResponse.success(list);
    }

    /**
     * 获取最近7天的浏览量
     *
     * @return 结果
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/api/admin/home/view")
    public ApiResponse getViewsStatistics(@RequestHeader("Authorization") String jwtToken) {

        User user = UserContext.getUser();

        log.info("获取最近7天的浏览量, 管理员：{},名:{}",  Objects.requireNonNull(user).getId(), user.getNickname());

        List<Views> list =  statisticsService.getViewsStatistics();
        if(list.isEmpty()){
            return ApiResponse.error("获取最近7天的浏览量失败");
        }

        return ApiResponse.success(list);
    }


    /**
     * 获取访问信息
     *
     * @return 结果
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/api/admin/home/view/list")
    public ApiResponse addViewsStatisticsByUser()  {
        User user = UserContext.getUser();

        log.info("获取访问信息,管理员：{}，邮箱：{}", user.getId(), user.getEmail());

        List<ViewInfoResp> list = statisticsService.getViewInfoList();
        if(list.isEmpty()){
            return ApiResponse.error("获取最近的访客信息失败");
        }
        return ApiResponse.success(list);
    }



    /**
     * 测试结算
     *
     * @return 结果
     */
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/api/admin/home/view/test/add")
    public ApiResponse test(@RequestParam("date") String date)  {
        User user = UserContext.getUser();

        log.info("测试结算,管理员：{}，邮箱：{}", user.getId(), user.getEmail());

        try{
            statisticsService.testAdd(date);
        }catch (Exception e){
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.success();
    }


}
