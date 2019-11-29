package com.grq.provider.service;

import com.grq.provider.domain.UmsAdmin;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import com.grq.provider.mapper.UmsAdminMapper;
import com.grq.provider.api.UmsAdminService;
import tk.mybatis.mapper.entity.Example;

@Service(version = "1.0.0")
public class UmsAdminServiceImpl implements UmsAdminService{

    @Resource
    private UmsAdminMapper umsAdminMapper;

    @Override
    public int insert(UmsAdmin umsAdmin) {
        return umsAdminMapper.insert(umsAdmin);
    }

    @Override
    public UmsAdmin get(String username) {
        return null;
    }

    @Override
    public UmsAdmin get(UmsAdmin umsAdmin) {
        return umsAdminMapper.selectOne(umsAdmin);
    }

    @Override
    public int update(UmsAdmin umsAdmin) {

        return 0;
    }

    @Override
    public int modifyPassword(String username, String password) {
        return 0;
    }

    @Override
    public int modifyIcon(String username, String path) {
        return 0;
    }
}
