package com.yeling.yelingziblog.other.service;

import com.yeling.yelingziblog.other.entity.UpdateLog;
import com.yeling.yelingziblog.other.entity.LeaveMessage;
import com.yeling.yelingziblog.other.entity.UpdateLogAdd;
import com.yeling.yelingziblog.other.entity.UpdateLogAdmin;

import java.util.List;

public interface BulletinBoardService {
    /**
     * 查询日志
     * @return
     */

    List<UpdateLog> updateLogList();

    List<UpdateLogAdmin> adminUpdateLogList();

    void addLog(UpdateLogAdd updateLogAdd);

    void editLog(UpdateLogAdd updateLogAdd);

    void delUpdateLog(Integer id);

    void pinnedLog(Integer id);

    void unpinnedLog(Integer id);

    void leaveMessage(String leaveMessage);

    List<LeaveMessage> getLeaveMessage(Integer num);

    List<LeaveMessage> getUnreadLeaveMessage(Integer num);

    LeaveMessage setUnreadLeaveMessage(Integer id);

    List<Integer> getLeaveMessageCount();

}
