package com.springboot.fastdfs.controller;

import com.springboot.fastdfs.client.FastDfsClient;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @Author FlyFish
 * @Link https://github.com/oddnumber7
 * @ClassName FastDfsController
 * @Create 2023/4/11 21:07
 * @Description
 */
@RestController
@RequestMapping(value = "/api/fastdfs")
public class FastDfsController {

    @Value("${fileServer.url}")
    private String fileUrl;

    @Autowired
    private FastDfsClient fastDfsClient;

    /**
     * 上传
     * @param file 文件
     * @return 全路径
     */
    @SneakyThrows
    @PostMapping(value = "/upload")
    public String upload(@RequestBody MultipartFile file) {
        return fileUrl + fastDfsClient.upload(file);
    }

    /**
     * 下载
     * @param groupName 分组名
     * @param path      路径
     */
    @SneakyThrows
    @GetMapping(value = "/download")
    public void download(String groupName, String path) {
        byte[] download = fastDfsClient.download(groupName, path);
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\" + UUID.randomUUID().toString() + ".jpg");
        IOUtils.write(download, fileOutputStream);
        fileOutputStream.close();
    }

}
