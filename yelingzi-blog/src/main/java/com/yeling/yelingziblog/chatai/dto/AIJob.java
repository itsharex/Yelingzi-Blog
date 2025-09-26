package com.yeling.yelingziblog.chatai.dto;

import java.util.Map;

public class AIJob {


    record Job(JobType jobType, Map<String, String> keyInfos) {}

    public enum JobType {
        AddFriendLink
    }

}
