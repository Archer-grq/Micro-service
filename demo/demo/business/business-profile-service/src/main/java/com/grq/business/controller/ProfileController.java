package com.grq.business.controller;

import com.grq.business.dto.IconParam;
import com.grq.business.dto.ProfileParam;
import com.grq.commons.dto.ResponseResult;
import com.grq.provider.api.UmsAdminService;
import com.grq.provider.domain.UmsAdmin;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 个人信息管理
 */
@RestController
@RequestMapping(value = "profile")
public class ProfileController {

    @Reference(version = "1.0.0")
    private UmsAdminService umsAdminService;

    @GetMapping(value = "info/{username}")
    public ResponseResult<UmsAdmin> info(@PathVariable String username){
        UmsAdmin umsAdmin = umsAdminService.get(username);
        return new ResponseResult<UmsAdmin>(ResponseResult.CodeStatus.OK,"查询个人信息",umsAdmin);

    }

    @PostMapping(value = "update")
    public ResponseResult<Void> update(@RequestBody ProfileParam profileParam){
        UmsAdmin umsAdmin =new UmsAdmin();
        BeanUtils.copyProperties(profileParam,umsAdmin);
        int update = umsAdminService.update(umsAdmin);

        if(update>0){
            return new ResponseResult<Void>(ResponseResult.CodeStatus.OK,"更新个人信息成功",null);
        }

        else {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL,"更新个人信息失败",null);
        }
    }

    /**
     * 修改头像信息
     * @param iconParam {@link IconParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "modify/icon")
    public ResponseResult<Void> modifyIcon(@RequestBody IconParam iconParam ){
        int i = umsAdminService.modifyIcon(iconParam.getUsername(), iconParam.getPath());

        if(i>0){
            //更新成功
            return new ResponseResult<Void>(ResponseResult.CodeStatus.OK,"更新头像成功");
        }
        else {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL,"更新头像失败");
        }
    }

}
