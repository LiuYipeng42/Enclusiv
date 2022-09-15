package com.guet.enclusiv.Entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("user_subscribe")
public class UserSubscribe {
    @TableId(type = IdType.AUTO)
    private Long id;

    private long userId;

    private long authorId;
}
