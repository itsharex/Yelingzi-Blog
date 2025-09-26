package com.yeling.yelingziblog.other.service;

import com.yeling.yelingziblog.other.entity.Statistics;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.other.entity.Views;
import com.yeling.yelingziblog.article.vo.response.ArticleAndTalkStatisticsResp;
import com.yeling.yelingziblog.common.vo.response.ViewInfoResp;

import java.util.List;

public interface StatisticsService {

    Statistics getStatistics();

    List<ArticleAndTalkStatisticsResp> getArticleAndTalkStatistics();

    List<Views> getViewsStatistics();


    List<ViewInfoResp> getViewInfoList();

    void testAdd(String date);

}
