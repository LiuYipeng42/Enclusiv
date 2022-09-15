# API

## 登陆：

**URL:** http://120.77.177.229:8080/account/login

**请求类型:** POST

**POST数据:** 

Json:
```json
{
    "email": "liuyipeng42@gmail.com",
    "password": "123"
}
```

**返回数据:**

登陆成功:

```json
{
    "data": {
        "id": 1,
        "name": "xxx",
        "email": "xxxxxxxx@gmail.com",
        "password": "xxx",
        "signature": "xxx",
        "profilePicture": null
    },
    "result": "success"
}
```

密码错误:

```json
{
    "data": null,
    "result": "wrong password"
}
```

用户不存在

```json
{
    "data": null,
    "result": "user not found"
}
```



## 注册：

**URL:** http://120.77.177.229:8080/account/register

请求类型: POST

**POST数据:** 

Json:
```json
{
    "email": "43722511@gmail.com",
    "name": "test",
    "password": "123"
}
```

**返回数据:**

注册成功:

```json
{
    "data": 100000001,
    "result": "success"
}
```
注册失败:
```json
{
    "data": 0,
    "result": "email already registered"
}
```



## 更改密码：

**URL:** http://120.77.177.229:8080/account/change

**请求类型:** POST

**POST数据:** 

Json:
```json
{
    "email": "43722511@gmail.com",
    "password": "123"
}
```

**返回数据:**

修改成功:

```json
{
    "data": true,
    "result": "success"
}
```

邮箱没有被注册

```json
{
    "data": false,
    "result": "email hasn't been registered"
}
```

## 获取用户数据：

**URL:** http://120.77.177.229:8080/account/user

**请求类型:** GET

**GET参数:**
```
id: 用户id
```


```json
{
    "data": {
        "id": 100000010,
        "name": "绘画-肝货",
        "email": "154871277@qq.com",
        "password": "123456",
        "signature": null
    },
    "result": "success"
}
```



## 获取验证码：

**URL:** http://120.77.177.229:8080/checkcode/get

请求类型: GET

**GET参数:**
```
email: 用户邮箱
```
**返回数据:**

获取成功:

```json
{
    "data": true,
    "result": "success"
}
```



## 检验验证码：

**URL:** http://120.77.177.229:8080/checkcode/check

**请求类型:** GET

**GET参数:**
```
email: 用户邮箱

code: 用户输入的验证码
```

**返回数据:**

用户验证码正确:

```json
{
    "data": true,
    "result": "success"
}
```

用户验证码错误:

```json
{
    "data": false,
    "result": "code not the same"
}
```

用户验证码不存在:

```json
{
    "data": false,
    "result": "code not exist"
}
```



## 用户头像上传：

**URL:** https://120.77.177.229:8080/profile_img/upload

**请求类型:** POST

POST数据：

form-data:
```
image: 图片数据

id: 用户id

type: 图片类型(png或jpg)
```

Headers:
```
Content-Type: multipart/form-data
```
**返回数据:**

上传成功：
```json
{
    "data": true,
    "result": "success"
}
```

上传失败：
```json
{
    "data": false,
    "result": "failed"
}
```



## 获取用户头像：

**URL:** https://120.77.177.229:8080/profile_img/image

**请求类型:** GET

**GET参数:**
```
id: 用户id
```
**返回数据:**

图片



## 向用户图片空间上传数据：

**URL：** https://120.77.177.229:8080/user_images/upload

**请求类型:** POST

POST数据：

form-data:
```
id: 用户id

image: 图片数据

type: 图片后缀(jpg、png或gif)
```
可以同时上传多个图片，即有多个 image 和 type ，要保证 image 和 type 的相对位置

Headers:
```
Content-Type: multipart/form-data
```

**返回数据:**

上转成功：
```json
{
    "data": true,
    "result": "success"
}
```

上转失败：
```json
{
    "data": false,
    "result": "x files upload succeed, x files upload failed"
}
```
x: 从前往后数第几个图片



## 获取用户图片空间的所有图片的id：

**URL：** https://120.77.177.229:8080/user_images/image_ids

**请求类型:** GET

**GET参数:**
```
id: 用户id
```

**返回数据:**
```json
{
    "data": [
        9,
        10,
        11
    ],
    "result": "success"
}
```



## 获取一张用户图片空间中的图片：

**URL：** https://120.77.177.229:8080/user_images/image

**请求类型:** GET

**GET参数:**
```
id: 图片id
```
**返回数据:**

图片



## 上传一篇文章：

**URL：**  https://120.77.177.229:8080/article/upload

**请求类型:** POST

POST数据：

Json:
```json
{
  "title": "gfsdfaefasdf",
  "authorId": "xxxxxxxx",
  "article": "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
  "imageIds": [9, 10, 11]
}
```

**返回数据:**

上转成功：
```json
{
    "data": true,
    "result": "success"
}
```
上转失败：
```json
{
    "data": false,
    "result": "failed"
}
```

## 获取用户所订阅作者的文章最新文章id：

**URL：** https://120.77.177.229:8080/article/subscribe_article

**请求类型:** GET

**GET参数:**
```
id: 用户id
```

**返回数据:**

```json
{
    "data": [
        4357,
        4358,
        4359,
        4360,
        4361,
        4362
    ],
    "result": "success"
}
```

最多可以返回 20 个文章的 id，在作者所发布的文章数大于20后，会去掉一篇最旧的文章，保证 id 数不超过 20，列表中越往后的文章id代表的文章越新（相当于队列）


## 获取用户文章id：

**URL：** https://120.77.177.229:8080/article/article_id

**请求类型:** GET

**GET参数:**
```
id: 用户id
```

**返回数据:**

```json
{
    "data": [
        78,
        103,
        1038,
        1131,
        1309,
        1354,
        1432,
        1553
    ],
    "result": "success"
}
```

## 获取一篇文章的信息：

**URL：** https://120.77.177.229:8080/article/article_info

**请求类型:** GET

**GET参数:**
```
id: 文章id
```

**返回数据:**

获取成功：
```json
{
    "data": {
        "id": 1,
        "title": "二次元美食番剧大盘点",
        "authorId": 100000002,
        "author": {
            "id": 100000002,
            "name": "User100000002",
            "email": "xxxxxxxx@qq.com",
            "signature": null
        },
        "article": "sssafgiauegfhiauegfiaugvsfkasugiawgolanvcoauge",
        "imageIds": [
            9,
            10,
            11
        ],
        "likes": 2,
        "collects": 2
    },
    "result": "success"
}
```

获取失败：
```json
{
    "data": null,
    "result": "article not found"
}
```



## <a id="1">用户点赞文章</a>：

**URL：** https://120.77.177.229:8080/action/like

**请求类型:** GET

**GET参数:**

```
user_id：用户id
article_id: 文章id
```

**返回数据:**

获取成功：
```json
{
    "data": true,
    "result": "success"
}
```

文章不存在：
```json
{
    "data": false,
    "result": "article not found"
}
```

用户不存在
```json
{
    "data": false,
    "result": "user not found"
}
```



## 用户取消点赞文章：

**URL：** https://120.77.177.229:8080/action/dislike

**请求类型:** GET

**GET参数:**

与 [用户点赞文章](#1) 相同

**返回数据:**

与 [用户点赞文章](#1) 相同



## 用户收藏文章：

**URL：** https://120.77.177.229:8080/action/collect

**请求类型:** GET

**GET参数:**

与 [用户点赞文章](#1) 相同

**返回数据:**

与 [用户点赞文章](#1) 相同



## 用户取消收藏文章：

**URL：** https://120.77.177.229:8080/action/cancel_collect

**请求类型:** GET

**GET参数:**

与 [用户点赞文章](#1) 相同

**返回数据:**

与 [用户点赞文章](#1) 相同




## <a id="2">用户订阅作者</a>：

**URL：** https://120.77.177.229:8080/action/subscribe

**请求类型:** GET

**GET参数:**

```
id：用户id
author_id: 被订阅用户id
```

**返回数据:**

获取成功：
```json
{
    "data": true,
    "result": "success"
}
```

用户不存在：
```json
{
  "data": false,
  "result": "user not found"
}
```

作者不存在：
```json
{
  "data": false,
  "result": "author not found"
}
```



## 用户取消订阅作者：

**URL：** https://120.77.177.229:8080/action/cancel_subscribe

**请求类型:** GET

**GET参数:**

与 [用户订阅作者](#2) 相同

**返回数据:**

与 [用户订阅作者](#2) 相同




## 获取用户点赞的文章：

**URL：** https://120.77.177.229:8080/action/likes

**请求类型:** GET

**GET参数:**

```
id：用户id
```

**返回数据：**

```json
{
    "data": [
        1,
        2
    ],
    "result": "success"
}
```
data: 文章id



## 获取用户收藏的文章：

**URL：** https://120.77.177.229:8080/action/collections

**请求类型:** GET

**GET参数:**

```
id：用户id
```
**返回数据：**

```json
{
    "data": [
        1,
        2
    ],
    "result": "success"
}
```
data: 文章id



## 获取用户订阅的作者：

**URL：** https://120.77.177.229:8080/action/subscribes

**请求类型:** GET

**GET参数:**

```
id：用户id
```
**返回数据：**

```json
{
    "data": [
        100000006,
        100000007
    ],
    "result": "success"
}
```
data: 用户id



## 获取用户的订阅者：

**URL：** https://120.77.177.229:8080/action/followers

**请求类型:** GET

**GET参数:**

```
id：用户id
```
**返回数据：**

```json
{
    "data": [
        100000006,
        100000007
    ],
    "result": "success"
}
```
data: 用户id


## 获取推荐文章id：

**URL：** https://120.77.177.229:8080/article/recommend

**请求类型:** GET

**GET参数:**

```
id：用户id
```

**返回数据：**
```json
{
    "data": [
        2660,
        734,
        1693,
        4280,
        3910,
        1075,
        693,
        3562,
        1324,
        3899
    ],
    "result": "success"
}
```
