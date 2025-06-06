package com.sky.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.sky.properties.AliOssProperties;
import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private AliOssProperties ossProp;

    /**
     * 文件上传接口（阿里云OSS）
     *
     * @param file 待上传文件
     * @return 文件的访问URL
     */
    @Override
    public String upload(MultipartFile file) throws IOException, ClientException {
        //创建工具类实例
        //TODO 为什么工具类不使用静态类？
        AliOssUtil aliOssUtil = new AliOssUtil(ossProp.getEndpoint(), ossProp.getBucketName(), ossProp.getBucketRegion());

        //获取文件原扩展名
        String originFileName = file.getOriginalFilename();
        String extensionName = originFileName.substring(originFileName.lastIndexOf("."));

        //为文件生成临时名字
        String objectName = UUID.randomUUID() + extensionName;

        //上传到阿里云OSS
        return aliOssUtil.upload(file.getBytes(), objectName);
    }
}
