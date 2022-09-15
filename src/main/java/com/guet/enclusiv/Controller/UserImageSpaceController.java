package com.guet.enclusiv.Controller;

import com.guet.enclusiv.DTO.ImageInfo;
import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.Entity.Image;
import com.guet.enclusiv.Service.ArticleService;
import com.guet.enclusiv.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("user_images")
public class UserImageSpaceController {

    private final ImageService imageService;

    private final ArticleService articleService;

    @Autowired
    public UserImageSpaceController(ImageService storageService, ArticleService articleService) {
        this.imageService = storageService;
        this.articleService = articleService;
    }

    @GetMapping("/image")
    public ResponseEntity<FileSystemResource> download(@RequestParam("id") long imageId) {

        Image image = imageService.getImageById(imageId);
        System.out.println(image);

        if (image != null) {
            return imageService.download(image.getPath());
        } else {
            return null;
        }
    }

    @GetMapping("/image_info")
    private RequestResult<List<ImageInfo>> getImageIds(@RequestParam("id") long id) {

        return new RequestResult<List<ImageInfo>>().setData(imageService.getImageInfoByUserId(id)).setResult("success");
    }

    @PostMapping("/upload")
    public RequestResult<List<Long>> upload(@RequestParam("id") long id, @RequestParam("image") MultipartFile[] images, @RequestParam("type") String[] types) {

        int i = 0;
        try {
            List<Long> ids = articleService.saveUserImg(id, images, types);
            return new RequestResult<List<Long>>().setData(ids).setResult("success");
        } catch (Exception e) {
            e.printStackTrace();
            return new RequestResult<List<Long>>()
                    .setData(null)
                    .setResult((i + 1) + " files upload succeed, " + (images.length - i - 1) + " files upload failed");
        }

    }

}
