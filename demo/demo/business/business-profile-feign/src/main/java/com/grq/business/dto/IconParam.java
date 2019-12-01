package com.grq.business.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IconParam implements Serializable {


    /**
     * 用户名
     */
    private String username;

    /**
     * 头像地址
     */
    private String path;

}
