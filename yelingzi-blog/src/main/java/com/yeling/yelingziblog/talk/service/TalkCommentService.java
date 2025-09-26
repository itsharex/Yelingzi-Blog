package com.yeling.yelingziblog.talk.service;

import com.yeling.yelingziblog.common.dto.PageResult;
import com.yeling.yelingziblog.talk.entity.TalkComment;
import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.talk.vo.request.TalkCommentReq;
import com.yeling.yelingziblog.common.vo.response.CommentLikeResp;
import com.yeling.yelingziblog.common.vo.response.CommentResp;

import java.util.List;

public interface TalkCommentService {

    List<CommentResp> getTalkCommentList(Integer talkId);

    List<CommentLikeResp> getTalkCommentLikeList(Integer talkId, Integer userId);

    CommentLikeResp addTalkCommentLike(Integer commentId, Integer talkId, Integer userId);

    void delTalkCommentLike(Integer id);

    CommentResp addTalkComment(TalkCommentReq talkCommentReq, User user);

    PageResult<TalkComment> getTalkCommentListByPage(Integer page, Integer pageSize);

    void delTalkComment(Integer id);

    void updateTalkComment(Integer id);

}
