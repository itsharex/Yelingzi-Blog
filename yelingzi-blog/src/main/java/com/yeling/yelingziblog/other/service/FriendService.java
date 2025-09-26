package com.yeling.yelingziblog.other.service;

import com.yeling.yelingziblog.other.entity.Friend;
import com.yeling.yelingziblog.common.dto.PageResult;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.other.vo.request.FriendReq;
import com.yeling.yelingziblog.other.vo.response.FriendResp;

import java.util.List;

public interface FriendService {

    void addFriend(FriendReq friendReq, User user);

    void delFriend(Long id);

    void updateFriend(Long id);

    PageResult<Friend> getFriendListByPage(int page, int pageSize);

    List<FriendResp> getFriendList();

}
