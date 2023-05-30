package com.example.orienteering.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.*;
import com.example.orienteering.mapper.QuestMapper;
import com.example.orienteering.service.RewardService;
import com.example.orienteering.service.UserQuestService;
import com.example.orienteering.service.UserService;
import com.example.orienteering.util.JwtUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 奖励Controller
 */

@RestController
@RequestMapping("/reward")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserQuestService userQuestService;

    @Autowired
    private QuestMapper questMapper;

    @GetMapping("/findAll")
    public R findAll() {
        List<Reward> allRewardList = rewardService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("message", allRewardList);
        return R.ok(map);
    }

    /**
     * 根据id查询reward
     */
    @GetMapping("/detail")
    public R detail(int rewardId){
        Reward reward = rewardService.getById(rewardId);
        Map<String, Object> map=new HashMap<>();
        map.put("message", reward);
        return R.ok(map);
    }

    /**
     * 根据用户id查询奖励
     */
    @RequestMapping("/my/findAllReward")
    public R findAllReward(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
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

        System.out.println("userId: " + userId);
//        List<Reward> allRewardList = rewardService.list(new QueryWrapper<Reward>().eq("user_id", userId).);

        // 根据userId查询对应的UserQuest->联表获取Quest
        MPJLambdaWrapper<Quest> mpjLambdaWrapper = new MPJLambdaWrapper<Quest>()
                .selectAs(Quest::getRewardId, "rewardId")
                .selectAs(UserQuest::getEndTime, "time")
                .leftJoin(UserQuest.class, UserQuest::getQuestId, Quest::getId) // 1vs多
                .eq(UserQuest::getUserId, userId)
                .eq(UserQuest::getQuestState, 2); // 已完成

        List<Map<String, Object>> resultList = questMapper.selectJoinMaps(mpjLambdaWrapper);

        List<Reward> allRewardList = new ArrayList<>();
        for (Map map: resultList) {
            int rewardId = (int) map.get("rewardId");
            Reward reward = rewardService.getById(rewardId);
            reward.setTime((LocalDateTime) map.get("time"));
            allRewardList.add(reward);
        }

        Map<String, Object> map=new HashMap<>();
        map.put("allRewardList", allRewardList);
        return R.ok(map);
    }


}
