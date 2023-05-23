package com.example.orienteering.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.*;
import com.example.orienteering.mapper.UserQuestMapper;
import com.example.orienteering.service.PuzzleService;
import com.example.orienteering.service.QuestService;
import com.example.orienteering.service.UserQuestService;
import com.example.orienteering.service.UserService;
import com.example.orienteering.util.JwtUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进度Controller
 */

@RestController
@RequestMapping("/progress")
public class ProgressController {

    @Autowired
    private QuestService questService;
    @Autowired
    private UserQuestService userQuestService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserQuestMapper userQuestMapper;
    @Autowired
    private PuzzleService puzzleService;

    /**
     * 当前城市进度
     */
//    @GetMapping("/my/cityProgress")
//    public R cityProgress(int userId, String city) {
//
    @RequestMapping("/my/cityProgress")
    public R cityProgress(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        // 读出参数
        int userId = (int) json.get("userId");
        String city = (String) json.get("city");

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userId != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }


        int totalAmount = questService.count(new QueryWrapper<Quest>().eq("city", city));

        MPJLambdaWrapper<UserQuest> mpjLambdaWrapper = new MPJLambdaWrapper<UserQuest>()
                .select(Quest::getId)
                .eq(UserQuest::getUserId, userId)
                .eq(UserQuest::getQuestState, 2)
                .eq(Quest::getCity, city)
                .leftJoin(Quest.class, Quest::getId, UserQuest::getQuestId)
                .groupBy(Quest::getId);
        List<Map<String, Object>> resultList = userQuestMapper.selectJoinMaps(mpjLambdaWrapper);
        int currentAmount = resultList.size();
//        List<UserQuest> userQuestList = userQuestService.list(new QueryWrapper<UserQuest>().select("quest_id").eq("user_id", userId).eq("quest_state", 2).eq("city", city).groupBy("quest_id"));
//        int currentAmount = userQuestList.size();
        Double progress = (double)currentAmount / totalAmount * 100;
        Map<String, Object> map = new HashMap<>();
        map.put("progress", progress);
        return R.ok(map);
    }

    /**
     * 所有任务的进度
     */
    @RequestMapping("/my/totalProgress")
    public R totalProgress(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        int userId = (int) json.get("userId");
        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userId != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }

        int totalAmount = questService.count();
        System.out.println("totalAmount: "+ totalAmount);

        List<UserQuest> userQuestList = userQuestService.list(new QueryWrapper<UserQuest>().select("quest_id").eq("user_id", userId).eq("quest_state", 2).groupBy("quest_id"));
        int currentAmount = userQuestList.size();

        System.out.println("currentAmount: "+ currentAmount);
        Double progress = (double)currentAmount / totalAmount * 100;
        System.out.println("progress: "+ progress);
        Map<String, Object> map = new HashMap<>();
        map.put("progress", progress);
        return R.ok(map);
    }

    /**
     * 根据theme_id查看当前主题拼图收集进度
     */
    @GetMapping("/my/themeProgress")
    public R themeProgress(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        // 读出参数
        int userId = (int) json.get("userId");
        int themeId = (int) json.get("themeId");

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userId != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }

        // join查询当前用户的拼图收集情况
        MPJLambdaWrapper<UserQuest> mpjLambdaWrapper = new MPJLambdaWrapper<UserQuest>()
//                .selectAll(UserQuest.class)
//                .select(Quest::getThemeId)
                .select(Quest::getPuzzleId)
                .eq(UserQuest::getUserId, userId)
                .eq(UserQuest::getQuestState, 2)
                .leftJoin(Quest.class, Quest::getId, UserQuest::getQuestId)
                .eq(Quest::getThemeId, themeId)
                .groupBy(Quest::getPuzzleId);
        List<Map<String, Object>> resultList = userQuestMapper.selectJoinMaps(mpjLambdaWrapper);
        int currentAmount =  resultList.size();
        System.out.println("currentAmount: " + currentAmount);

        // 根据themeId查询当前主题的拼图总个数
        List<Puzzle> puzzleList = puzzleService.list(new QueryWrapper<Puzzle>().eq("theme_id", themeId));
        int totalAmount = puzzleList.size();
        System.out.println("totalAmount: " + totalAmount);

//        int themeProgress = currentAmount / totalAmount; // 收集进度

        Map<String, Object> map=new HashMap<>();
        map.put("currentAmount", currentAmount);
        map.put("totalAmount", totalAmount);
        return R.ok(map);
    }

}
