package com.example.orienteering.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.*;
import com.example.orienteering.service.FriendService;
import com.example.orienteering.service.QuestService;
import com.example.orienteering.service.UserQuestService;
import com.example.orienteering.service.UserService;
import com.example.orienteering.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 进度Controller
 */

@RestController
@RequestMapping("/society")
public class SocietyController {

    @Autowired
    private QuestService questService;

    @Autowired
    private UserQuestService userQuestService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;


    @GetMapping("/getUserInfo")
    public R getUserInfo(int userId) {
        User user = userService.getById(userId);
        UserRecommended userRecommended = new UserRecommended();
        userRecommended.setId(userId);
        userRecommended.setNickname(user.getNickname());
        userRecommended.setPhoneNumber(user.getPhoneNumber());
        String avatarUrl = user.getAvatarUrl();
        if (avatarUrl.startsWith("/avatar/")) {
            avatarUrl = "http://localhost:8088" + avatarUrl;
        }
        userRecommended.setAvatarUrl(avatarUrl);
        userRecommended.setGender(user.getGender());
        Map<String, Object> map = new HashMap<>();
        map.put("userInfo", userRecommended);
        return R.ok(map);
    }

    /**
     * 获取推荐列表
     */
    @RequestMapping("/my/getRecommend")
    public R getRecommend(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {// 根据当前用户最近完成的10次任务进行推荐（有待改进）
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

        // 获取当前用户最近完成的10次任务
        List<Map<String, Object>> userList = userQuestService.listMaps(new QueryWrapper<UserQuest>().select("quest_id").eq("user_id", userId).eq("quest_state", 2).groupBy("quest_id"));

        List<UserRecommended> chatList = new ArrayList<>();
        for (Map<String, Object> map: userList) {
            int questId = (int) map.get("quest_id");
            Map<String, Object> recommend = userQuestService.getMap(new QueryWrapper<UserQuest>().select("user_id").ne("user_id", userId).eq("quest_id", questId).eq("quest_state", 2).groupBy("user_id"));
            int recommendId = (int) recommend.get("user_id");
            User user = userService.getById(recommendId);
            UserRecommended userRecommended = new UserRecommended();
            userRecommended.setId(user.getId());
            userRecommended.setNickname(user.getNickname());
            userRecommended.setPhoneNumber(user.getPhoneNumber());
            String avatarUrl = user.getAvatarUrl();
            if (avatarUrl.startsWith("/avatar/")) {
                avatarUrl = "http://localhost:8088" + avatarUrl;
            }
            userRecommended.setAvatarUrl(avatarUrl);
            userRecommended.setGender(user.getGender());
            userRecommended.setQuestName(questService.getById(questId).getName());
            if (chatList.contains(userRecommended)) {
                continue;
            }
            chatList.add(userRecommended);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("chatList", chatList);
        return R.ok(map);
    }

    @RequestMapping("/my/getFriendList")
    public R getFriendList(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
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

        List<Friend> friendList = friendService.list(new QueryWrapper<Friend>().eq("user_id", userId));
        for (Friend friend: friendList) {
            User user = userService.getById(friend.getFriendUserId());
            friend.setFriendName(user.getNickname());
            String avatarUrl = user.getAvatarUrl();
            if (avatarUrl.startsWith("/avatar/")) {
                avatarUrl = "http://localhost:8088" + avatarUrl;
            }
            friend.setFriendAvatarUrl(avatarUrl);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("friendList", friendList);
        return R.ok(map);
    }
}
