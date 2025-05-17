package cn.edu.tust.beauty_back.controller;

import cn.edu.tust.beauty_back.bean.Result;
import cn.edu.tust.beauty_back.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadController {

    /**
     * 上传文件
     **/

    @PostMapping("/api/upload")
    public Result<String> upload(MultipartFile file) throws Exception {
        //暂存上传的文件，获取文件名
        String originalFilename = file.getOriginalFilename();

        //使用UUID来防止文件重名
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        String url = AliOssUtil.uploadFile(fileName,file.getInputStream());

        return Result.success(url);
    }
}
