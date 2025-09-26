package com.yeling.yelingziblog.other.service;

import com.yeling.yelingziblog.other.entity.Background;
import com.yeling.yelingziblog.common.dto.PageResult;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.other.vo.response.BackgroundResp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BackgroundService {

    void uploadBgImage(MultipartFile multipartFile, User user);

    PageResult<Background> getBackgroundListByPage(Integer page, Integer pageSize);

    PageResult<Background> getBackgroundDelListByPage(Integer page, Integer pageSize);

     void delTalkComment(Integer id);

    void showTalkComment(Integer id);

    List<BackgroundResp> getBackgroundList();

}
