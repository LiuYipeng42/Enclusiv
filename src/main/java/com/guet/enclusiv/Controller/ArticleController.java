package com.guet.enclusiv.Controller;


import com.guet.enclusiv.DTO.ArticleInfo;
import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.DTO.UserArticleStats;
import com.guet.enclusiv.Service.ArticleService;
import com.guet.enclusiv.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    private final UserService userService;

    private final HashMap<Long, List<Long>> articleSubscribe = new HashMap<>();

    @Autowired
    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/article_info")
    public RequestResult<ArticleInfo> getArticle(@RequestParam("id") long articleId){
        return articleService.getArticle(articleId);
    }

    @GetMapping("/article_stats")
    public RequestResult<UserArticleStats> getArticle(@RequestParam("article_id") long articleId, @RequestParam("user_id") long user_Id){
        return articleService.getUserArticleStats(articleId, user_Id);
    }

    @PostMapping("/upload")
    public RequestResult<Long> uploadArticle(@RequestBody ArticleInfo articleInfo) {
        long articleId = articleService.uploadArticle(
                articleInfo.getAuthorId(),
                articleInfo.getTitle(),
                articleInfo.getArticle(),
                articleInfo.getImageIds()
        );

        if (articleId != 0) {
            List<Long> followerIds = userService.followers(articleInfo.getAuthorId()).getData();
            for (Long followerId : followerIds) {
                List<Long> articleIds = articleSubscribe.getOrDefault(followerId, new ArrayList<>());
                articleIds.add(articleId);
                if (articleIds.size() > 20){
                    articleIds.remove(0);
                }
                articleSubscribe.put(followerId, articleIds);
            }
            return new RequestResult<Long>().setData(articleId).setResult("success");
        }else {
            return  new RequestResult<Long>().setData(0L).setResult("failed");
        }
    }

    @GetMapping("/subscribe_article")
    public RequestResult<List<Long>> subscribeArticle(@RequestParam("id") long userId){

        List<Long> articleIds = articleSubscribe.getOrDefault(userId, new ArrayList<>());

        return new RequestResult<List<Long>>().setData(articleIds).setResult("success");
    }

    @GetMapping("/recommend")
    public RequestResult<List<Long>> recommend(@RequestParam("id") long userId){
        return articleService.recommend(userId);
    }

    @GetMapping("/article_id")
    public RequestResult<List<Long>> userArticleId(@RequestParam("id") long userId){
        return articleService.getUserArticleIds(userId);
    }

}
