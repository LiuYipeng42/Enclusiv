package com.guet.enclusiv.DTO;


import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArticleRecData {

    private String id;

    private double score;

    private boolean used=false;

}
