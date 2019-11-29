package com.grq.business.oauth2;

import com.google.common.collect.Maps;
import com.grq.commons.utils.MapperUtils;
import com.grq.commons.utils.OkHttpClientUtil;
import okhttp3.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ApplicationTest {

    @Test
    public void testGet(){
        String url = "https://www.baidu.com";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(Objects.requireNonNull(response.body()).string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPost(){
        String url = "http://localhost:9001/oauth/token";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username", "admin")
                .add("password", "123456")
                .add("grant_type", "password")
                .add("client_id", "client")
                .add("client_secret", "secret")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUtils(){
        String url = "http://localhost:9001/oauth/token";
        Map<String,String> map= Maps.newHashMap();
        map.put("username","admin");
        map.put("password","123456");
        map.put("grant_type","password");
        map.put("client_id","client");
        map.put("client_secret","secret");

        try {
            Response response = OkHttpClientUtil.getInstance().postData(url, map);
            String jsonString= Objects.requireNonNull(response.body()).string();
            Map<String, Object> stringObjectMap = MapperUtils.json2map(jsonString);
            System.out.println(stringObjectMap.get("access_token"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
