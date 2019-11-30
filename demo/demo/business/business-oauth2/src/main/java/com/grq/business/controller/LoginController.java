package com.grq.business.controller;

import com.google.common.collect.Maps;
import com.grq.business.dto.LoginInfo;
import com.grq.business.dto.LoginParam;
import com.grq.business.feign.ProfileFeign;
import com.grq.commons.dto.ResponseResult;
import com.grq.commons.utils.MapperUtils;
import com.grq.commons.utils.OkHttpClientUtil;
import com.grq.provider.domain.UmsAdmin;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.SecurityContextAccessor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.plugin.liveconnect.SecurityContextHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    //注入feign
    @Resource
    public ProfileFeign profileFeign;

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

    //获取用户信息
    @GetMapping(value = "/user/info")
    public ResponseResult<LoginInfo> userInfo() throws Exception {
        //访问时会携带 access_token，所以可以根据 access_token 获得用户信息
        //获取当前用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String jsonInfo = profileFeign.info(authentication.getName());
        UmsAdmin umsAdmin = MapperUtils.json2pojoByTree(jsonInfo, "data", UmsAdmin.class);

        //封装并返回结果
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setName(umsAdmin.getNickName());
        loginInfo.setAvatar(umsAdmin.getIcon());
        return new ResponseResult<LoginInfo>(ResponseResult.CodeStatus.OK,"获取用户信息",loginInfo);
    }


    /**
     * 用户退出
     * @param request 请求信息
     * @return null
     */
    @PostMapping(value = "/user/logout")
    public ResponseResult<Void> userLogout(HttpServletRequest request){
        //ResponseResult<Void>表示泛型为空
        String access_token = request.getParameter("access_token");

        //根据token获取认证信息
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(access_token);
        //删除用户认证信息，即 客户端无法再次访问服务器，因为没有token就没有权限了，请求会被安全配置阻拦
        tokenStore.removeAccessToken(oAuth2AccessToken);

        return new ResponseResult<Void>(ResponseResult.CodeStatus.OK,"用户注销",null);
    }
}
