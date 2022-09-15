package com.guet.enclusiv.Service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.guet.enclusiv.DTO.ImageInfo;
import com.guet.enclusiv.Entity.Image;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService  extends IService<Image> {

    Image getImageById(long id);

    List<ImageInfo> getImageInfoByUserId(long userId);

    ResponseEntity<FileSystemResource> download(String filename);

    boolean upload(MultipartFile file, String filepath, String filename);

}