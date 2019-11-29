package com.grq.business.controller;

import com.google.common.collect.Maps;
import com.grq.business.dto.LoginParam;
import com.grq.commons.dto.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 登录管理
 */
@CrossOrigin
@RestController
public class LoginController {

    @PostMapping(value = "/user/login")
    public ResponseResult<Map<String,Object>> login(@RequestBody LoginParam loginParam){

        Map<String,Object> map= Maps.newHashMap();
        map.put("token","123456");
       return new ResponseResult<Map<String,Object>>(20000,"ok",map);

    }


}
