package com.guet.enclusiv.DTO;

import com.guet.enclusiv.Entity.Article;
import lombok.Data;


@Data
public class ArticleInfo {

    private long id;

    private String title;

    private long authorId;

    private UserInfo author;

    private String article;

    private long[] imageIds;

    private ArticleStats stats;

    public ArticleInfo(){
    }

    public ArticleInfo(Article article){
        id = article.getId();
        title = article.getTitle();
        author = new UserInfo().setId(article.getAuthorId());
        stats = new ArticleStats()
                .setViewers(article.getViewers())
                .setLikes(article.getLikes())
                .setCollects(article.getCollects());

        this.article = article.getArticle();
    }

}
