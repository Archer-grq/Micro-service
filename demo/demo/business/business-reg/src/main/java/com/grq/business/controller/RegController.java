package com.grq.business.controller;

import com.grq.commons.dto.ResponseResult;
import com.grq.provider.api.UmsAdminService;
import com.grq.provider.domain.UmsAdmin;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)//解决跨域
@RestController
@RequestMapping(value = "reg")
public class RegController {

        @Reference(version = "1.0.0")
        private UmsAdminService umsAdminService;
        /**
         * 用户注册
         *
         * @param umsAdmin {@link UmsAdmin}
         * @return 成功则返回新注册用户信息
         */
        @PostMapping(value = "")
        public ResponseResult<UmsAdmin> reg(@RequestBody UmsAdmin umsAdmin) {
            String message = validateReg(umsAdmin);
            // 验证通过
            if (message == null) {
                int result = umsAdminService.insert(umsAdmin);
                // 注册成功
                if (result > 0) {
                    UmsAdmin admin = umsAdminService.get(umsAdmin.getUsername());
                    return new ResponseResult<UmsAdmin>(HttpStatus.OK.value(), "新用户注册成功", admin);
                }
            }
            return new ResponseResult<UmsAdmin>(HttpStatus.NOT_ACCEPTABLE.value(), message != null ? message : "新用户注册失败");
        }
        /**
         * 验证注册信息
         * @param umsAdmin {@link UmsAdmin}
         * @return 错误信息
         */
        private String validateReg(UmsAdmin umsAdmin) {
            UmsAdmin admin = umsAdminService.get(umsAdmin.getUsername());
            if (admin != null) {
                return "用户名已存在，请重新输入";
            }
            return null;
        }
}
