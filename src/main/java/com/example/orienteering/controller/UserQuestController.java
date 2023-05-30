package com.example.orienteering.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.*;
import com.example.orienteering.mapper.UserQuestMapper;
import com.example.orienteering.service.*;
import com.example.orienteering.util.JwtUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户任务Controller
 */

@RestController
@RequestMapping("/userQuest")
public class UserQuestController {

    @Autowired
    private UserQuestService userQuestService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestService questService;

    @Autowired
    private RewardService rewardService;

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private UserQuestMapper userQuestMapper;

    @GetMapping("/findAll")
    public R findAll() {
        List<UserQuest> userQuestList = userQuestService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("userQuestList", userQuestList);
        return R.ok(map);
    }

    /**
     * 根据id查询用户任务
     */
    @GetMapping("/detail")
    public R detail(int userQuestId){
        UserQuest userQuest = userQuestService.getById(userQuestId);
        Map<String, Object> map=new HashMap<>();
        map.put("message", userQuest);
        return R.ok(map);
    }


    /**
     * 根据id更新用户任务（接受）
     */
    @RequestMapping("/my/accept")
    public R accept(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {

        // 从RequestBody读出参数
        int userId = (int) json.get("userId");
        int questId = (int) json.get("questId");

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

        // 查询当前是否有任务在进行中
        List<UserQuest> current = userQuestService.list(new QueryWrapper<UserQuest>().eq("user_id", userId).eq("quest_state", 0).or().eq("quest_state", 1)); // 0为未开始的任务，1为进行中的任务，均为当前任务
        if (current.size() == 0) { // 判断是否存在当前任务
            // 新建用户任务并设置内容
            UserQuest userQuest = new UserQuest();
            userQuest.setUserId(userId);
            userQuest.setQuestId(questId);
            userQuest.setQuestState(0);
            int userQuestId = userQuestMapper.insert(userQuest); // 插入

            Map<String, Object> map=new HashMap<>();
            map.put("userQuestId", userQuestId);
            return R.ok(map);
        } else {
            Map<String, Object> map=new HashMap<>();
            map.put("message", "当前已有已接受的任务");
            return R.ok(map);
        }

    }



    /**
     * 根据id更新用户任务（开始）
     */
    @RequestMapping("/my/start")
    public R start(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {

        // 从RequestBody读出参数
        int userQuestId = (int) json.get("userQuestId");
        String startTimeStr = (String) json.get("startTimeStr");

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userQuestService.getById(userQuestId).getUserId() != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }

        // String 转为 LocalDateTime
        System.out.println("startTimeStr: " + startTimeStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);
        System.out.println("startTime: " + startTime);

        // 从数据库中查找任务
        UserQuest userQuest = userQuestService.getById(userQuestId);

        // 更新任务
        userQuest.setQuestState(1);// 设置任务状态
        userQuest.setStartTime(startTime);// 设置开始时间
        userQuestService.updateById(userQuest);

        Map<String, Object> map=new HashMap<>();
        map.put("message", "success");
        return R.ok(map);
    }

    /**
     * 根据id更新用户任务（完成）
     */
    @RequestMapping("/my/complete")
    public R complete(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        // 从RequestBody读出参数
        int userQuestId = (int) json.get("userQuestId");
        String endTimeStr = (String) json.get("endTimeStr");

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userQuestService.getById(userQuestId).getUserId() != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }


        System.out.println("endTimeStr: " + endTimeStr);
        // String 转为 LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);
        System.out.println("endTime: " + endTime);

        // 从数据库中查找任务
        UserQuest userQuest = userQuestService.getById(userQuestId);

        // 计算总时间
        Duration duration = Duration.between(userQuest.getStartTime(), endTime);
        LocalTime totalTime = LocalTime.ofNanoOfDay(duration.toNanos()); // 转为纳秒

        // 更新任务
        userQuest.setQuestState(2);// 设置任务状态
        userQuest.setEndTime(endTime);// 设置结束时间
        userQuest.setTotalTime(totalTime);// 设置总时间
        System.out.println("totalTime: " + totalTime);
        userQuestService.updateById(userQuest); // 根据id更新任务

        // 更新用户成就
        Integer userId = userQuest.getUserId(); // 获取userId
        Achievement achievement = achievementService.getOne(new QueryWrapper<Achievement>().eq("user_id", userId).last("limit 1")); // 获取用户成就
        achievement.setQuestAmount(achievement.getQuestAmount() + 1); // 任务数量+1
        int questId = userQuest.getQuestId(); // 获取questId
        String city = questService.getById(questId).getCity(); // 获取城市名
        MPJLambdaWrapper<UserQuest> mpjLambdaWrapper = new MPJLambdaWrapper<UserQuest>()
                .select(UserQuest::getId)
                .eq(UserQuest::getUserId, userId)
                .eq(UserQuest::getQuestState, 2)
                .leftJoin(Quest.class, Quest::getId, UserQuest::getQuestId)
                .eq(Quest::getCity, city); // 查询是否在该城市完成过任务
        List<Map<String, Object>> resultList = userQuestMapper.selectJoinMaps(mpjLambdaWrapper);
        if (resultList.size() == 0) {
            achievement.setCityAmount(achievement.getCityAmount() + 1);
        }
        achievementService.updateById(achievement);

        Map<String, Object> map=new HashMap<>();
        map.put("message", "success");
        return R.ok(map);
    }


    /**
     * 根据id更新用户任务（放弃）
     */
    @RequestMapping("/my/giveUp")
    public R giveUp(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        // 从RequestBody读出参数
        int userQuestId = (int) json.get("userQuestId");
        String endTimeStr = (String) json.get("endTimeStr");

        // 权限验证
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = JwtUtils.validateJWT(token).getClaims();
            if(claims != null) {
                String openid = claims.getId();
                System.out.println("openId: " + openid);
                if (userQuestService.getById(userQuestId).getUserId() != userService.getOne(new QueryWrapper<User>().eq("openid", openid).last("limit 1")).getId()) {
                    return R.error(500,"无权限访问！");
                }
            } else {
                return R.error(500,"鉴权失败！");
            }
        } else {
            return R.error(500,"无权限访问！");
        }

        // String 转为 LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr, formatter);

        // 从数据库中查找任务
        UserQuest userQuest = userQuestService.getById(userQuestId);
        // 更新任务
        userQuest.setQuestState(3);// 设置任务状态
        userQuest.setStartTime(endTime);// 设置结束（放弃）时间
        userQuestService.updateById(userQuest);

        Map<String, Object> map=new HashMap<>();
        map.put("message", "success");
        return R.ok(map);
    }


    /**
     * 根据user_id查询用户当前的任务
     * 用于：微信小程序首页
     */
    @RequestMapping("/my/findCurrentQuest")
    public R findCurrent(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
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

        // 查询当前的任务
        UserQuest userQuest = userQuestService.getOne(new QueryWrapper<UserQuest>().eq("user_id", userId).eq("quest_state", 0).or().eq("quest_state", 1).last("limit 1")); // 0为未开始的任务，1为进行中的任务，均为当前任务
        if (userQuest == null) { // 判断是否存在当前任务
            Map<String, Object> map=new HashMap<>();
            map.put("userQuest", null);
            map.put("quest", null);
            map.put("reward", null);
            map.put("bestTime", "暂无已接受的任务");
            return R.ok(map);
        } else {
            System.out.println("userQuest: " + userQuest);
            Integer questId = userQuest.getQuestId(); // 获取当前的任务的id
            System.out.println("questId: " + questId);
            Quest quest = questService.getById(questId);// 获取当前任务
            System.out.println("quest: " + quest);
            Integer rewardId = quest.getRewardId(); // 获取当前任务的奖励的id
            Reward reward = rewardService.getById(rewardId); // 获取当前任务的奖励

            // 查询历史最佳用时
            UserQuest bestUserQuest = userQuestService.getOne(new QueryWrapper<UserQuest>().eq("user_id", userId).eq("quest_id", questId).eq("quest_state", 2).orderByAsc("totalTime").last("limit 1")); // 查询历史最佳用时任务
            System.out.println(bestUserQuest);



            // 需要返回的数据
            Map<String, Object> map = new HashMap<>();
            map.put("userQuest", userQuest);
            map.put("quest", quest);
            map.put("reward", reward);
            if (bestUserQuest != null) {
                map.put("bestTime", bestUserQuest.getTotalTime());
            } else {
                map.put("bestTime", "暂无记录");
            }
            return R.ok(map);
        }
    }


}
