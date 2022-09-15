package com.guet.enclusiv.DTO;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ImageInfo {

    private Long id;

    private String type;
}
