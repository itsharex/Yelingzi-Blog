package com.yeling.yelingziblog.article.service.Impl;

import com.yeling.yelingziblog.article.entity.Category;
import com.yeling.yelingziblog.common.dto.PageResult;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.article.vo.request.CategoryReq;
import com.yeling.yelingziblog.article.vo.response.CategoryResp;
import com.yeling.yelingziblog.article.mapper.ArticleCategoryMapper;
import com.yeling.yelingziblog.article.service.ArticleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCategoryServiceImpl implements ArticleCategoryService {

    @Autowired
    private ArticleCategoryMapper categoryMapper;


    @Override
    public CategoryResp addCategory(String category, User user){


        Integer count = categoryMapper.findNotNullByCategoryName(category);
        if(count > 0){
            return null;
        }

        categoryMapper.addCategory(category, user.getId(), user.getNickname());

        return categoryMapper.findCategoryByCategoryName(category);

    }

    @Override
    public void deleteCategory(Integer id){
        categoryMapper.deleteCategoryById(id);
    }

    @Override
    public PageResult<Category> getArticleCategoryListByPage(int page, int pageSize){
        if(page <= 0){
            return null;
        }
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(categoryMapper.findCategoryCount());
        pageResult.setData(categoryMapper.findCategoryByPage((page - 1)*pageSize, pageSize));
        return pageResult;
    }

    @Override
    public PageResult<Category> userGetArticleCategoryListByPage(int page, int pageSize, Integer userId){
        if(page <= 0 || pageSize <=0){
            return null;
        }
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setPageSize(pageSize);
        pageResult.setTotal(categoryMapper.findCategoryCountByUserId(userId));
        pageResult.setData(categoryMapper.findCategoryByPageByUserId(userId, (page - 1)*pageSize, pageSize));
        return pageResult;
    }

    @Override
    public List<Category> getArticleCategoryList(){
        return categoryMapper.findCategory();
    }

    @Override
    public List<Category> userGetArticleCategoryList(Integer userId){
        return categoryMapper.findCategoryByUserId(userId);
    }

    @Override
    public CategoryResp updateCategory(CategoryReq categoryReq, User user){

        Integer count = categoryMapper.findNotNullById(categoryReq.getId());
        if(count == 0){
            return null;
        }

        categoryMapper.updateCategoryById(categoryReq.getId(), categoryReq.getCategoryName(), user.getId(), user.getNickname());

        return new CategoryResp(categoryReq.getId(), categoryReq.getCategoryName());
    }

}
