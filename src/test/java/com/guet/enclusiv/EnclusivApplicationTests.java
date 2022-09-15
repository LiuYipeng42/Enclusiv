package com.guet.enclusiv;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guet.enclusiv.Entity.Article;
import com.guet.enclusiv.Utils.RunCMD;
import com.guet.enclusiv.mapper.ArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@SpringBootTest
class EnclusivApplicationTests {

    @Test
    public void test() {

    }

//    public static void test(String[] args) throws IOException {
//        HashMap<String, List<Double>> articleScores = new HashMap<>();
//        HashMap<String, List<String>> articleIds = new HashMap<>();
//
//        String output = RunCMD.execute("python3 ./Python/recommend.py all");
//
//        String[] parts = output.split("---------------------------------\n");
//
//        for (String part: parts){
//            String[] lines = part.split("\n");
//            String userId = lines[0];
//            String articleId;
//            double score;
//            for (int i = 1; i < lines.length; i++) {
//                articleId = lines[i].split(" ")[0];
//                score = Double.parseDouble(lines[i].split(" ")[1]);
//                List<Double> scores = articleScores.getOrDefault(userId, new ArrayList<>());
//                List<String> ids = articleIds.getOrDefault(userId, new ArrayList<>());
//
//                if (scores.size() > 0) {
//                    int j;
//                    for (j = 0; j < scores.size(); j++) {
//                        if (scores.get(j) < score) {
//                            scores.add(j, score);
//                            ids.add(j, articleId);
//                            break;
//                        }
//                    }
//                    if (j == scores.size()){
//                        scores.add(score);
//                        ids.add(articleId);
//                    }
//                } else {
//                    scores.add(score);
//                    ids.add(articleId);
//                }
//                articleScores.put(userId, scores);
//                articleIds.put(userId, ids);
//            }
//        }
//        System.out.println(articleIds);
//    }

}
