package com.yeling.yelingziblog.chatai.mapper;

import com.yeling.yelingziblog.chatai.dto.History;
import com.yeling.yelingziblog.chatai.entity.ChatAi;
import com.yeling.yelingziblog.chatai.entity.ChatAiUserLink;
import com.yeling.yelingziblog.chatai.vo.response.HistoryLinkResp;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HistoryMapper {


    @Insert("""
            INSERT INTO chat_ai(session_id, role, content)
            VALUES (#{sessionId}, #{role}, #{content})
            """)
    void addChatAi(ChatAi chatAi);


    @Select("""
            SELECT role, content FROM chat_ai 
            WHERE session_id=#{sessionId}
            """)
    List<History> selectChatAiById(String sessionId);

    @Insert("""
            INSERT INTO chat_ai_user_link(session_id, user_ident, description)
            VALUES (#{sessionId}, #{userIdent}, #{description})
            """)
    void addChatAiUserLink(ChatAiUserLink chatAiUserLink);

    @Select("""
            SELECT session_id, description FROM chat_ai_user_link
            WHERE user_ident=#{userIdent}
            """)
    List<HistoryLinkResp> selectChatAiUserLinkById(String userIdent);


    @Select("""
            SELECT COUNT(*) FROM chat_ai_user_link
            WHERE user_ident=#{userIdent} AND session_id=#{sessionId}
            """)
    Long selectChatAiUserLinkCount(String userIdent, String sessionId);

}
