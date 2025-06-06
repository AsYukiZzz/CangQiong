package com.sky.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CommonService {

    /**
     * 文件上传接口（阿里云OSS）
     *
     * @param file 待上传文件
     * @return 文件的访问URL
     */
    String upload(MultipartFile file) throws IOException, ClientException;
}
