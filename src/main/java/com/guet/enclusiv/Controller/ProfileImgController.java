package com.guet.enclusiv.Controller;


import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.Service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/profile_img")
public class ProfileImgController {

    private final ImageService imageService;

    private final String imgPath = "ProfilePictures/";

    @Autowired
    public ProfileImgController(ImageService storageService) {
        this.imageService = storageService;
    }

    @GetMapping("/image")
    public ResponseEntity<FileSystemResource> download(@RequestParam("id") String id) {

        ResponseEntity<FileSystemResource> img = imageService.download(imgPath + id + ".jpg");

        if (img == null){
            return imageService.download(imgPath + id + ".png");
        }else {
            return img;
        }

    }

    @PostMapping("/upload")
    public RequestResult<Boolean> upload(@RequestParam("image") MultipartFile image, @RequestParam("id") String id, @RequestParam("type") String type) {

        try {
            imageService.upload(image, imgPath , id + "." + type);
            return new RequestResult<Boolean>().setData(true).setResult("success");
        } catch (Exception e){
            e.printStackTrace();
            return new RequestResult<Boolean>().setData(false).setResult("failed");
        }

    }

}
