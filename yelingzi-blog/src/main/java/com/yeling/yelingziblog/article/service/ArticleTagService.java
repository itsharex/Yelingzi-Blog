package com.yeling.yelingziblog.article.service;

import com.yeling.yelingziblog.common.dto.PageResult;
import com.yeling.yelingziblog.article.entity.Tag;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.article.vo.request.TagReq;
import com.yeling.yelingziblog.article.vo.response.TagResp;


import java.util.List;

public interface ArticleTagService {

    TagResp addTag(String tag, User user);

    void deleteTag(Integer id);

    PageResult<Tag> getArticleTagListByPage(int page, int pageSize);

    PageResult<Tag> userGetArticleTagListByPage(int page, int pageSize, Integer userId);

    List<Tag> getArticleTagList();

    void updateTag(TagReq tagReq, User user);

    List<TagResp> getArticleTagRespList();


}
