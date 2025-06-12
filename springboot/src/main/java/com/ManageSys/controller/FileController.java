package com.ManageSys.controller;

import com.ManageSys.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {

    private String basePath = "D:/vue/vue3/vue/src/assets/images/avatars/";

    @PostMapping("/upload")
    public Result upload(MultipartFile file) {
        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID().toString() + suffix;

        // 创建一个目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            // 确保路径拼接正确
            File dest = new File(dir, fileName);
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success("上传成功", fileName);
    }

    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response){
        try{
            FileInputStream fis = new FileInputStream(new File(basePath + name));
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/png");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fis.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            outputStream.close();
            fis.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
