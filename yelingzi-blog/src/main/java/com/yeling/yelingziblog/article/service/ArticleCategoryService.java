package com.yeling.yelingziblog.article.service;

import com.yeling.yelingziblog.article.entity.Category;
import com.yeling.yelingziblog.common.dto.PageResult;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.article.vo.request.CategoryReq;
import com.yeling.yelingziblog.article.vo.response.CategoryResp;

import java.util.List;

public interface ArticleCategoryService {

    CategoryResp addCategory(String category, User user);

    void deleteCategory(Integer id);

    PageResult<Category> getArticleCategoryListByPage(int page, int pageSize);

    PageResult<Category> userGetArticleCategoryListByPage(int page, int pageSize, Integer userId);

    List<Category> getArticleCategoryList();

    List<Category> userGetArticleCategoryList(Integer userId);


    CategoryResp updateCategory(CategoryReq categoryReq, User user);

}
