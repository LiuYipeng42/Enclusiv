package com.guet.enclusiv.Controller;


import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.Service.ArticleService;
import com.guet.enclusiv.Service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/action")
public class UserActionController {

    private final UserService userService;

    public final ArticleService articleService;

    public UserActionController(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/view")
    public RequestResult<Boolean> view(@RequestParam("user_id") long userId, @RequestParam("article_id") long articleId){
        return userService.view(userId, articleId);
    }

    @GetMapping("/like")
    public RequestResult<Boolean> like(@RequestParam("user_id") long userId, @RequestParam("article_id") long articleId) throws IOException {

        RequestResult<Boolean> r = userService.like(userId, articleId);
        articleService.updateArticleData(userId);
        return r;
    }

    @GetMapping("/dislike")
    public RequestResult<Boolean> dislike(@RequestParam("user_id") long userId, @RequestParam("article_id") long articleId) throws IOException {
        RequestResult<Boolean> r = userService.dislike(userId, articleId);
        articleService.updateArticleData(userId);
        return r;
    }

    @GetMapping("/likes")
    RequestResult<List<Long>> likes(@RequestParam("user_id") long userId){
        return userService.likes(userId);
    }

    @GetMapping("/collect")
    public RequestResult<Boolean> collect(@RequestParam("user_id") long userId, @RequestParam("article_id") long articleId) throws IOException {
        RequestResult<Boolean> r = userService.collect(userId, articleId);
        articleService.updateArticleData(userId);
        return r;
    }

    @GetMapping("/cancel_collect")
    public RequestResult<Boolean> cancelCollect(@RequestParam("user_id") long userId, @RequestParam("article_id") long articleId) throws IOException {
        RequestResult<Boolean> r = userService.cancelCollect(userId, articleId);
        articleService.updateArticleData(userId);
        return r;
    }

    @GetMapping("/collections")
    RequestResult<List<Long>> collections(@RequestParam("user_id") long userId){
        return userService.collections(userId);
    }

    @GetMapping("/subscribe")
    public RequestResult<Boolean> subscribe(@RequestParam("user_id") long userId, @RequestParam("author_id") long authorId){
        return userService.subscribe(userId, authorId);
    }

    @GetMapping("/cancel_subscribe")
    public RequestResult<Boolean> cancelSubscribe(@RequestParam("user_id") long userId, @RequestParam("author_id") long authorId){
        return userService.cancelSubscribe(userId, authorId);
    }

    @GetMapping("/subscribes")
    RequestResult<List<Long>> subscribes(@RequestParam("user_id") long userId){
        return userService.subscribes(userId);
    }

    @GetMapping("/followers")
    RequestResult<List<Long>> followers(@RequestParam("user_id") long authorId){
        return userService.followers(authorId);
    }

}
