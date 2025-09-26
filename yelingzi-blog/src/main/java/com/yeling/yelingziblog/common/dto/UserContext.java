package com.yeling.yelingziblog.common.dto;

import com.yeling.yelingziblog.user.entity.User;

public class UserContext {
    private static final ThreadLocal<User> USER_INFO = new ThreadLocal<>();

    public static void setUser(User user) {
        USER_INFO.set(user);
    }

    public static User getUser() {
        return USER_INFO.get();
    }

    public static void clear() {
        USER_INFO.remove();
    }
}
