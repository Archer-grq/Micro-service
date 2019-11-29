package com.grq.business.controller;

import com.google.common.collect.Maps;
import com.grq.business.dto.LoginParam;
import com.grq.commons.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 登录管理
 */
@CrossOrigin
@RestController
public class LoginController {

    private static final String URL_OAUTH_TOKEN = "http://localhost:9001/oauth/token";
    @Value("${business.oauth2.grant_type}")
    public String oauth2GrantType;
    @Value("${business.oauth2.client_id}")
    public String oauth2ClientId;
    @Value("${business.oauth2.client_secret}")
    public String oauth2ClientSecret;
    @Resource(name = "userDetailsServiceBean")
    public UserDetailsService userDetailsService;
    @Resource
    public BCryptPasswordEncoder passwordEncoder;
    @Resource
    public TokenStore tokenStore;

    @PostMapping(value = "/user/login")
    public ResponseResult<Map<String,Object>> login(@RequestBody LoginParam loginParam){

        Map<String,Object> map= Maps.newHashMap();



        map.put("token","123456");
       return new ResponseResult<Map<String,Object>>(20000,"ok",map);

    }


}
