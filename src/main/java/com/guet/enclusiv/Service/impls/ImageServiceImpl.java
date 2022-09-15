package com.guet.enclusiv.Service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.enclusiv.DTO.ImageInfo;
import com.guet.enclusiv.Entity.Image;
import com.guet.enclusiv.Service.ImageService;
import com.guet.enclusiv.mapper.ImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    private final ImageMapper imageMapper;

    @Autowired
    public ImageServiceImpl(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    @Override
    public Image getImageById(long id) {
        return imageMapper.selectOne(new QueryWrapper<Image>().eq("id", id));
    }

    @Override
    public List<ImageInfo> getImageInfoByUserId(long userId){
        return imageMapper.selectList(
                new QueryWrapper<Image>().eq("user_id", userId)
        ).stream().map(
                img-> new ImageInfo().setId(img.getId()).setType(img.getType())
        ).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<FileSystemResource> download(String filePath) {
        if (filePath == null || filePath.isEmpty()){
            return null;
        }
        System.out.println(filePath);

        File file = new File(filePath);

        if (!file.exists()){
            return null;
        }

        if (file.canRead()){
            return ResponseEntity.ok()
                    .contentType(file.getName().contains("jpg") ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG)
                    .body(new FileSystemResource(file));
        }else {
            return null;
        }
    }

    @Override
    public boolean upload(MultipartFile file, String filepath, String filename) {
        if (file == null || file.isEmpty() || filepath == null || filepath.isEmpty() || filename == null || filename.isEmpty()){
            return false;
        }

        try{

            File path = new File(filepath);
            InputStream inputStream = file.getInputStream();

            if (path.exists()){
                Files.copy(inputStream, Paths.get(filepath + "/" + filename), StandardCopyOption.REPLACE_EXISTING);
            }else {
                if (path.mkdir()){
                    Files.copy(inputStream, Paths.get(filepath + "/" + filename), StandardCopyOption.REPLACE_EXISTING);
                }else {
                    return false;
                }
            }

            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
