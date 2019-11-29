package com.grq.business.controller;

import com.google.common.collect.Maps;
import com.grq.business.dto.LoginInfo;
import com.grq.business.dto.LoginParam;
import com.grq.commons.dto.ResponseResult;
import com.grq.commons.utils.MapperUtils;
import com.grq.commons.utils.OkHttpClientUtil;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.SecurityContextAccessor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

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
        //封装结果集
        Map<String,Object> result=Maps.newHashMap();

        //验证账号密码是否正确
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginParam.getUsername());
        if(userDetails==null||!passwordEncoder.matches(loginParam.getPassword(),userDetails.getPassword())){
            return new ResponseResult<Map<String, Object>>(ResponseResult.CodeStatus.FAIL,"账号或密码错误",null);
        }

        Map<String,String> map= Maps.newHashMap();

        map.put("username",loginParam.getUsername());
        map.put("password",loginParam.getPassword());
        map.put("grant_type",oauth2GrantType);
        map.put("client_id",oauth2ClientId);
        map.put("client_secret",oauth2ClientSecret);

        try {
            //使用 okhttp 去获取 token
            Response response = OkHttpClientUtil.getInstance().postData(URL_OAUTH_TOKEN, map);
            String jsonString= Objects.requireNonNull(response.body()).string();
            //使用工具类将json转成对象
            Map<String, Object> stringObjectMap = MapperUtils.json2map(jsonString);
            //将获得的token传给前端
            result.put("token",stringObjectMap.get("access_token"));
        }catch (Exception e){
            e.printStackTrace();
        }

       return new ResponseResult<Map<String,Object>>(ResponseResult.CodeStatus.OK,"登录成功",result);

    }

    @GetMapping(value = "/user/info")
    public ResponseResult<LoginInfo> userInfo(){
        //访问时会携带 access_token，所以可以根据 access_token 获得用户信息
        //获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName(authentication.getName());
        return new ResponseResult<LoginInfo>(ResponseResult.CodeStatus.OK,"获取用户信息",loginInfo);
    }

}
