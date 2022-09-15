package com.guet.enclusiv.Entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("image")
public class Image {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String type;

    private long userId;

    private String path;

}
