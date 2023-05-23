package com.example.orienteering.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.Achievement;
import com.example.orienteering.entity.R;
import com.example.orienteering.entity.User;
import com.example.orienteering.service.AchievementService;
import com.example.orienteering.service.UserService;
import com.example.orienteering.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成就Controller
 */

@RestController
@RequestMapping("/achievement")
public class AchievementController {

    @Autowired
    private AchievementService achievementService;

    @Autowired
    private UserService userService;


    @GetMapping("/findAll")
    public R findAll() {
        List<Achievement> allAchievementList = achievementService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("message", allAchievementList);
        return R.ok(map);
    }

    /**
     * 根据用户id查询成就
     */
//    @GetMapping("/my/detail")
//    public R findCurrent(int userId){
    @RequestMapping("/my/detail")
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

        System.out.println("userId: " + userId);
        Achievement achievement = achievementService.getOne(new QueryWrapper<Achievement>().eq("user_id", userId).last("limit 1")); // 根据用户id获取用户的成就
        System.out.println("achievement: " + achievement);
        int ranking = achievementService.count(new QueryWrapper<Achievement>().gt("quest_amount", achievement.getQuestAmount())); // 实时查询用户排名（基于大于当前用户的questAmount）
        System.out.println("ranking: " + ranking);
        achievement.setRanking(ranking);
        Map<String, Object> map=new HashMap<>();
        map.put("achievement", achievement);
        return R.ok(map);
    }

}
