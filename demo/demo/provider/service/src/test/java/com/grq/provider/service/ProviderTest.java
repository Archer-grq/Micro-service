package com.grq.provider.service;

import com.grq.provider.api.UmsAdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class ProviderTest {

    @Resource
    UmsAdminService umsAdminService;

    @Test
    public void test(){
        System.out.println(umsAdminService.get("admin"));
    }

}
