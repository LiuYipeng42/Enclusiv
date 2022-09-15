package com.guet.enclusiv.Service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.enclusiv.DTO.*;
import com.guet.enclusiv.Entity.*;
import com.guet.enclusiv.Service.ArticleService;
import com.guet.enclusiv.Service.ImageService;
import com.guet.enclusiv.Utils.RunCMD;
import com.guet.enclusiv.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;

    private final ImageMapper imageMapper;

    private final UserMapper userMapper;

    private final ImageService imageService;

    private final UserLikeMapper userLikeMapper;

    private final UserCollectMapper userCollectMapper;

    private final HashMap<String, List<ArticleRecData>> articleData = new HashMap<>();

    {
        try {
            String output = RunCMD.execute("python3 ./Python/recommend.py all_all");
            insertRecommendData(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public ArticleServiceImpl(ArticleMapper articleMapper, ImageMapper imageMapper, UserMapper userMapper,
                              ImageService imageService, UserLikeMapper userLikeMapper, UserCollectMapper userCollectMapper) {
        this.articleMapper = articleMapper;
        this.imageMapper = imageMapper;
        this.userMapper = userMapper;
        this.imageService = imageService;
        this.userLikeMapper = userLikeMapper;
        this.userCollectMapper = userCollectMapper;
    }

    @Override
    public List<Long> saveUserImg(long userId, MultipartFile[] images, String[] types) {
        String imgPath = "UserImageSpace/";
        List<Long> ids = new ArrayList<>();

        try {
            Image image;
            for (int i = 0; i < images.length; i++) {
                image = new Image().setUserId(userId);
                imageMapper.insert(image);
                imageMapper.update(
                        image.setPath(imgPath + userId + "/" + image.getId() + "." + types[i]),
                        new QueryWrapper<Image>().eq("id", image.getId())
                );
                ids.add(image.getId());
                imageService.upload(
                        images[i],
                        "UserImageSpace/" + userId,
                        image.getId() + "." + types[i]);
            }

            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RequestResult<ArticleInfo> getArticle(long id) {

        Article article = articleMapper.selectOne(new QueryWrapper<Article>().eq("id", id));

        if (article != null) {
            User author = userMapper.selectOne(new QueryWrapper<User>().eq("id", article.getAuthorId()));
            UserInfo userInfo = new UserInfo(author);
            ArticleInfo articleInfo = new ArticleInfo(article);
            articleInfo.setAuthor(userInfo);
            articleInfo.setImageIds(articleMapper.getImageId(article.getId()));
            return new RequestResult<ArticleInfo>().setData(articleInfo).setResult("success");
        } else {
            return new RequestResult<ArticleInfo>().setData(null).setResult("article not found");
        }

    }

    @Override
    public long uploadArticle(long userId, String title, String articleStr, long[] imageIds) {

        try {
            Article article = new Article();

            article.setArticle(articleStr);
            article.setTitle(title);
            article.setAuthorId(userId);
            article.setLikes(0);
            article.setCollects(0);

            articleMapper.insert(article);

            for (long imageId : imageIds) {
                articleMapper.saveImageId(article.getId(), imageId);
            }

            get_score(Math.toIntExact(userId), Math.toIntExact(article.getId()));
            return article.getId();
        } catch (Exception e) {
            return 0L;
        }

    }

    @Override
    public RequestResult<List<Long>> recommend(long userId) {
        List<Long> recommendIds = new ArrayList<>();

        List<ArticleRecData> data = articleData.getOrDefault(String.valueOf(userId), new ArrayList<>());

        for (ArticleRecData article : data) {
            if (!article.isUsed()) {
                recommendIds.add(Long.parseLong(article.getId()));
                if (recommendIds.size() >= 10) {
                    break;
                }
                article.setUsed(true);
            }
        }

        Random rd = new Random();

        if (recommendIds.size() < 10) {
            int size = articleMapper.count();
            int num = recommendIds.size();
            for (int i = 0; i < 10 - num; i++) {
                int id = rd.nextInt(size);
                recommendIds.add((long) id);
            }
        }

        return new RequestResult<List<Long>>().setData(recommendIds).setResult("success");
    }

    private void insertRecommendData(String output) {

        String[] parts = output.split("---------------------------------\n");

        for (String part : parts) {
            String[] lines = part.split("\n");
            String userId = lines[0];
            String articleId;
            double score;
            for (int i = 1; i < lines.length; i++) {
                articleId = lines[i].split(" ")[0];
                score = Double.parseDouble(lines[i].split(" ")[1]);
                List<ArticleRecData> data = articleData.getOrDefault(userId, new ArrayList<>());

                if (data.size() > 0) {
                    int j;
                    for (j = 0; j < data.size(); j++) {
                        if (data.get(j).getScore() < score) {
                            data.add(j, new ArticleRecData().setId(articleId).setScore(score));
                            break;
                        }
                    }
                    if (j == data.size()) {
                        data.add(new ArticleRecData().setId(articleId).setScore(score));
                    }
                } else {
                    data.add(new ArticleRecData().setId(articleId).setScore(score));
                }
                articleData.put(userId, data);
            }
        }

    }

    private void get_score(int authorId, int articleId) {

        new Thread(
                () -> {
                    try {
                        RunCMD.execute("python3 ./Python/article_words.py " + articleId);
                        RunCMD.execute("python3 ./Python/article_feature.py transform " + articleId);
                        String output = RunCMD.execute("python3 ./Python/recommend.py all_one " + authorId + " " + articleId);
                        insertRecommendData(output);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();

    }

    @Override
    public void updateArticleData(long userId) {

        new Thread(
                () -> {
                    if (articleData.getOrDefault(String.valueOf(userId), new ArrayList<>()).size() > 0) {
                        articleData.put(String.valueOf(userId), new ArrayList<>());
                    }
                    try {
                        String output = RunCMD.execute("python3 ./Python/recommend.py one_all " + userId);
                        insertRecommendData(output);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();

    }

    @Override
    public RequestResult<List<Long>> getUserArticleIds(long userId) {
        List<Article> articles = articleMapper.selectList(new QueryWrapper<Article>().eq("author_id", userId));

        List<Long> ids = articles.stream().map(Article::getId).collect(Collectors.toList());

        return new RequestResult<List<Long>>().setData(ids).setResult("success");
    }

    @Override
    public RequestResult<UserArticleStats> getUserArticleStats(long articleId, long userId) {
        UserArticleStats userArticleStats = new UserArticleStats();

        UserLike userLike = userLikeMapper.selectOne(
                new QueryWrapper<UserLike>().eq("article_id", articleId).eq("user_id", userId)
        );

        UserCollect userCollect = userCollectMapper.selectOne(
                new QueryWrapper<UserCollect>().eq("article_id", articleId).eq("user_id", userId)
        );

        if (userLike != null){
            userArticleStats.setLike(true);
        }

        if (userCollect != null){
            userArticleStats.setCollect(true);
        }

        return new RequestResult<UserArticleStats>().setData(userArticleStats).setResult("success");
    }

}
