package com.guet.enclusiv.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guet.enclusiv.DTO.ArticleInfo;
import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.DTO.UserArticleStats;
import com.guet.enclusiv.Entity.Article;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface ArticleService extends IService<Article> {

    List<Long> saveUserImg(long userId, MultipartFile[] images, String[] names);

    RequestResult<ArticleInfo> getArticle(long id);

    long uploadArticle(long userId, String title, String article, long[] imgIds);

    RequestResult<List<Long>> recommend(long userId);

    void updateArticleData(long userId) throws IOException;

    RequestResult<List<Long>> getUserArticleIds(long userId);

    RequestResult<UserArticleStats> getUserArticleStats(long articleId, long userId);

}
