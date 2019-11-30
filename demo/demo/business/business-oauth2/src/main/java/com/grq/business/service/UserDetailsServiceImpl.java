package com.grq.business.service;

import com.google.common.collect.Lists;
import com.grq.provider.api.UmsAdminService;
import com.grq.provider.domain.UmsAdmin;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 自定义认证授权
 * 怎样授权 自己定义
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Reference(version = "1.0.0")
    private UmsAdminService umsAdminService;

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "$2a$10$WhCuqmyCsYdqtJvM0/J4seCU.xZQHe2snNE5VFUuBGUZWPbtdl3GG";
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        //给每个用户获取USER 权限
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("USER");
        grantedAuthorities.add(grantedAuthority);

        UmsAdmin umsAdmin=umsAdminService.get(s);
        if(umsAdmin != null){
            return new User(umsAdmin.getUsername(),umsAdmin.getPassword(),grantedAuthorities);
        }

        else{
            return null;
        }
    }
}
