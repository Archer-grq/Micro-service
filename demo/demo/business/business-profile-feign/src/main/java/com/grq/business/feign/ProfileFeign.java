package com.grq.business.feign;

import com.grq.business.dto.ProfileParam;
import com.grq.configuration.FeignRequestConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * feign 伪http客户端
 * feign 客户端 value=服务名 path=路径
 * configuration=feign 配置类
 */
@FeignClient(value = "business-profile",path = "profile",configuration = FeignRequestConfiguration.class)
public interface ProfileFeign {

    @GetMapping(value = "info/{username}")
    String info(@PathVariable String username);

    @PostMapping(value = "update")
    String update(@RequestBody ProfileParam profileParam);

}
