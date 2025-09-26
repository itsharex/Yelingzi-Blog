package com.yeling.yelingziblog.other.service;

import com.yeling.yelingziblog.user.entity.User;
import com.yeling.yelingziblog.other.entity.Stories;

import java.util.List;

public interface StoriesService {
    Integer addStory(Stories stories, User user);

    void deleteStory(Integer num);

    List<Stories> showPageStories(Integer id);

    List<Stories> showStories(Integer id);

    Integer getStoriesCount();



}
