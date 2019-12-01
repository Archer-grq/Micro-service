package com.grq.cloud.feign;

import com.grq.configuration.FeignRequestConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "cloud-upload",path = "upload",configuration = FeignRequestConfiguration.class)
public interface UploadFeign {

    /**
     * 文件上传
     * @param multipartFile file
     * @return 文件上传路径
     */
    @PostMapping(value = "")
    String upload(@RequestPart(value = "multipartFile")MultipartFile multipartFile);
}
