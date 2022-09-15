package com.guet.enclusiv.DTO;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArticleStats {

    private int viewers;

    private int likes;

    private int collects;
}
