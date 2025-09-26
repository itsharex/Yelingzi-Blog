package com.yeling.yelingziblog.article.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

    private Integer userId;
    private String nickname;
    private LocalDateTime createTime; //创建时间
    private Integer isDel;
    private Integer articleCount;

}
