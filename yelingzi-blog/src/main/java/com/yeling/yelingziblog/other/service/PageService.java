package com.yeling.yelingziblog.other.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yeling.yelingziblog.other.entity.Page;
import com.yeling.yelingziblog.common.dto.PageResult;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.other.vo.request.PageReq;
import com.yeling.yelingziblog.common.vo.request.ViewPageReq;

import java.util.Map;

public interface PageService {

    void addPage(PageReq pageReq, User user) throws JsonProcessingException;

    void updatePage(PageReq pageReq, Integer userId) throws JsonProcessingException;

    Map<String, Object> getPage(ViewPageReq pageReq) throws JsonProcessingException;

    Map<String, Object> getPageById(Integer id, Integer userId) throws JsonProcessingException;

    PageResult<Page> userGetPageList(int page, int pageSize, Integer userId);

}
