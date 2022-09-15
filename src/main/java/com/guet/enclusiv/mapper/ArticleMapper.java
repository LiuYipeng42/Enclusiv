package com.guet.enclusiv.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guet.enclusiv.Entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select("insert into article_image (article_id, image_id) values (#{articleId}, #{imageId})")
    void saveImageId(@Param("articleId") long articleId, @Param("imageId") long imageId);

    @Select("select image_id from article_image where article_id = #{articleId}")
    long[] getImageId(@Param("articleId") long articleId);

    @Select("select count(*) from article")
    int count();
}
