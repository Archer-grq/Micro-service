package com.grq.provider.service;

import com.grq.provider.domain.UmsAdmin;
import org.apache.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import com.grq.provider.mapper.UmsAdminMapper;
import com.grq.provider.api.UmsAdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service(version = "1.0.0")
public class UmsAdminServiceImpl implements UmsAdminService{

    @Resource
    private UmsAdminMapper umsAdminMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public int insert(UmsAdmin umsAdmin) {
        //初始化原始用户信息
        initUmsAdmin(umsAdmin);
        return umsAdminMapper.insert(umsAdmin);
    }

    @Override
    public UmsAdmin get(String username) {

        Example example =new Example(UmsAdmin.class);
        example.createCriteria().andEqualTo("username",username);
        return umsAdminMapper.selectOneByExample(example);

    }

    @Override
    public UmsAdmin get(UmsAdmin umsAdmin) {
        return umsAdminMapper.selectOne(umsAdmin);
    }

    @Override
    public int update(UmsAdmin umsAdmin) {
        //获取原始用户信息
        UmsAdmin oldAdmin =get(umsAdmin.getUsername());

        //仅更新邮箱、昵称、备注、状态
        oldAdmin.setEmail(umsAdmin.getEmail());
        oldAdmin.setNickName(umsAdmin.getNickName());
        oldAdmin.setNote(umsAdmin.getNote());
        oldAdmin.setStatus(umsAdmin.getStatus());
        return umsAdminMapper.updateByPrimaryKey(oldAdmin);
    }

    @Override
    public int modifyPassword(String username, String password) {
        //修改密码
        UmsAdmin umsAdmin=get(username);
        umsAdmin.setPassword(passwordEncoder.encode(password));
        return umsAdminMapper.updateByPrimaryKey(umsAdmin);
    }

    @Override
    public int modifyIcon(String username, String path) {
        UmsAdmin umsAdmin=get(username);
        umsAdmin.setIcon(path);
        return umsAdminMapper.updateByPrimaryKey(umsAdmin);
    }
    private void initUmsAdmin(UmsAdmin umsAdmin){
        //初始化创建时间
        umsAdmin.setCreateTime(new Date());
        umsAdmin.setLoginTime(new Date());

        //初始化状态
        if(umsAdmin.getStatus()==null){
            umsAdmin.setStatus(0);
        }

        //密码加密
        umsAdmin.setPassword(passwordEncoder.encode(umsAdmin.getPassword()));
    }
}
