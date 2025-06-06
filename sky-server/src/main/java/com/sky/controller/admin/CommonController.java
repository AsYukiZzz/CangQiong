package com.sky.controller.admin;

import com.aliyuncs.exceptions.ClientException;
import com.sky.result.Result;
import com.sky.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 通用接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    /**
     * 文件上传通用接口
     *
     * @param file 待上传文件（由前端传递）
     * @return 文件的访问URL地址
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException, ClientException {
        log.info("文件上传，文件名：{}", file.getOriginalFilename());
        return Result.success(commonService.upload(file));
    }
}
