package com.guet.enclusiv.Service.impls;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guet.enclusiv.DTO.LoginForm;
import com.guet.enclusiv.DTO.RegisterForm;
import com.guet.enclusiv.DTO.RequestResult;
import com.guet.enclusiv.Entity.*;
import com.guet.enclusiv.Service.UserService;
import com.guet.enclusiv.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    private final UserViewMapper userViewMapper;

    private final UserLikeMapper userLikeMapper;

    private final UserCollectMapper userCollectMapper;

    private final UserSubscribeMapper userSubscribeMapper;

    private final ArticleMapper articleMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserViewMapper userViewMapper, UserLikeMapper userLikeMapper,
                           UserCollectMapper userCollectMapper, UserSubscribeMapper userSubscribeMapper, ArticleMapper articleMapper) {
        this.userMapper = userMapper;
        this.userViewMapper = userViewMapper;
        this.userLikeMapper = userLikeMapper;
        this.userCollectMapper = userCollectMapper;
        this.userSubscribeMapper = userSubscribeMapper;
        this.articleMapper = articleMapper;
    }



    @Override
    public RequestResult<User> login(LoginForm loginForm) {

        User user = userMapper.selectOne(new QueryWrapper<User>().select().eq("email", loginForm.getEmail()));

        if (user != null) {
            if (user.getPassword().equals(loginForm.getPassword())) {
                user.setEmail(null);
                user.setPassword(null);
                return new RequestResult<User>().setData(user).setResult("success");
            } else {
                return new RequestResult<User>().setData(null).setResult("wrong password");
            }
        } else {
            return new RequestResult<User>().setResult("user not found");
        }

    }

    @Override
    public RequestResult<User> getUser(long userId) {
        User user = userMapper.selectOne(new QueryWrapper<User>().select().eq("id", userId));

        if (user != null) {
            return new RequestResult<User>().setData(user).setResult("success");
        } else {
            return new RequestResult<User>().setResult("user not found");
        }
    }

    @Override
    public RequestResult<Long> register(RegisterForm registerForm) {

        User user = new User().setEmail(registerForm.getEmail())
                .setName(registerForm.getName())
                .setPassword(registerForm.getPassword());

        try {
            userMapper.insert(user);

            if (user.getName() == null) {
                userMapper.update(
                        user.setName("User" + user.getId()),
                        new QueryWrapper<User>().eq("id", user.getId())
                );
            }

            return new RequestResult<Long>().setData(user.getId()).setResult("success");
        } catch (DuplicateKeyException e) {
            return new RequestResult<Long>().setData(0L).setResult("email already registered");
        }

    }

    @Override
    public RequestResult<Boolean> changePasswd(LoginForm registerForm) {

        int num = userMapper.update(
                new User().setPassword(registerForm.getPassword()),
                new QueryWrapper<User>().select().eq("email", registerForm.getEmail())
        );

        if (num == 0) {
            return new RequestResult<Boolean>().setData(false).setResult("email hasn't been registered");
        } else {
            return new RequestResult<Boolean>().setData(true).setResult("success");
        }

    }

    @Override
    public RequestResult<Boolean> view(long userId, long articleId) {
        return userArticleAction(userId, articleId, "view");
    }

    @Override
    public RequestResult<Boolean> like(long userId, long articleId) {
        return userArticleAction(userId, articleId, "like");
    }

    @Override
    public RequestResult<Boolean> dislike(long userId, long articleId) {
        return userArticleAction(userId, articleId, "dislike");
    }

    @Override
    public RequestResult<List<Long>> likes(long userId) {

        List<Long> articles = userLikeMapper
                .selectList(
                        new QueryWrapper<UserLike>().eq("user_id", userId)
                ).stream()
                .map(UserLike::getArticleId)
                .collect(Collectors.toList());
        return new RequestResult<List<Long>>().setData(articles).setResult("success");
    }

    @Override
    public RequestResult<Boolean> collect(long userId, long articleId) {
        return userArticleAction(userId, articleId, "collect");
    }

    @Override
    public RequestResult<Boolean> cancelCollect(long userId, long articleId) {
        return userArticleAction(userId, articleId, "cancelCollect");
    }

    @Override
    public RequestResult<List<Long>> collections(long userId) {
        List<Long> articles = userCollectMapper
                .selectList(
                        new QueryWrapper<UserCollect>().eq("user_id", userId)
                ).stream()
                .map(UserCollect::getArticleId)
                .collect(Collectors.toList());

        return new RequestResult<List<Long>>().setData(articles).setResult("success");
    }

    @Override
    public RequestResult<Boolean> subscribe(long userId, long authorId) {
        return userAuthorAction(userId, authorId, "subscribe");
    }

    @Override
    public RequestResult<Boolean> cancelSubscribe(long userId, long authorId) {
        return userAuthorAction(userId, authorId, "unsubscribe");
    }

    @Override
    public RequestResult<List<Long>> subscribes(long userId) {
        List<Long> authors = userSubscribeMapper
                .selectList(
                        new QueryWrapper<UserSubscribe>().eq("user_id", userId)
                ).stream()
                .map(UserSubscribe::getAuthorId)
                .collect(Collectors.toList());

        return new RequestResult<List<Long>>().setData(authors).setResult("success");
    }

    @Override
    public RequestResult<List<Long>> followers(long authorId) {
        List<Long> followers = userSubscribeMapper
                .selectList(
                        new QueryWrapper<UserSubscribe>().eq("author_id", authorId)
                ).stream()
                .map(UserSubscribe::getUserId)
                .collect(Collectors.toList());

        return new RequestResult<List<Long>>().setData(followers).setResult("success");
    }

    private RequestResult<Boolean> userArticleAction(long userId, long articleId, String action) {
        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("id", userId)
        );

        if (user != null) {
            Article article = articleMapper.selectOne(
                    new QueryWrapper<Article>().eq("id", articleId)
            );

            if (article != null) {
                int effected;
                switch (action) {
                    case "view":
                        try {
                            userViewMapper.insert(new UserView().setUserId(userId).setArticleId(articleId));
                            article.setViewers(article.getViewers() + 1);
                        } catch (Exception ignored) {
                        }
                        break;
                    case "like":
                        try {
                            userLikeMapper.insert(new UserLike().setUserId(userId).setArticleId(articleId));
                            article.setLikes(article.getLikes() + 1);
                        } catch (Exception ignored) {
                        }
                        break;
                    case "dislike":
                        effected = userLikeMapper.delete(
                                new QueryWrapper<UserLike>().eq("user_id", userId).eq("article_id", articleId)
                        );
                        if (effected > 0)
                            article.setLikes(article.getLikes() - 1);
                        break;
                    case "collect":
                        try {
                            userCollectMapper.insert(new UserCollect().setUserId(userId).setArticleId(articleId));
                            article.setCollects(article.getCollects() + 1);
                        } catch (Exception ignored) {
                        }
                        break;
                    case "cancelCollect":
                        effected = userCollectMapper.delete(
                                new QueryWrapper<UserCollect>()
                                        .eq("user_id", userId)
                                        .eq("article_id", articleId)
                        );
                        if (effected > 0)
                            article.setCollects(article.getCollects() - 1);
                        break;
                }
                articleMapper.update(
                        article,
                        new QueryWrapper<Article>().eq("id", article.getId())
                );
                return new RequestResult<Boolean>().setData(true).setResult("success");
            } else {
                return new RequestResult<Boolean>().setData(false).setResult("article not found");
            }
        } else {
            return new RequestResult<Boolean>().setData(false).setResult("user not found");
        }
    }

    private RequestResult<Boolean> userAuthorAction(long userId, long authorId, String action) {
        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("id", userId)
        );

        if (user != null) {
            User author = userMapper.selectOne(
                    new QueryWrapper<User>().eq("id", authorId)
            );

            if (author != null) {
                switch (action) {
                    case "subscribe":
                        try {
                            userSubscribeMapper.insert(new UserSubscribe().setUserId(userId).setAuthorId(authorId));
                        } catch (Exception ignored) {
                        }
                        break;
                    case "unsubscribe":
                        userSubscribeMapper.delete(
                                new QueryWrapper<UserSubscribe>()
                                        .eq("user_id", userId)
                                        .eq("author_id", authorId)
                        );
                        break;
                }

                return new RequestResult<Boolean>().setData(true).setResult("success");
            } else {
                return new RequestResult<Boolean>().setData(false).setResult("author not found");
            }
        } else {
            return new RequestResult<Boolean>().setData(false).setResult("user not found");
        }

    }

}
