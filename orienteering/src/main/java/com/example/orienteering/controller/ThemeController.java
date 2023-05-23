package com.example.orienteering.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.*;
import com.example.orienteering.mapper.PuzzleMapper;
import com.example.orienteering.service.PuzzleService;
import com.example.orienteering.service.ThemeService;
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
 * 主题Controller
 */

@RestController
@RequestMapping("/theme")
public class ThemeController {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private PuzzleService puzzleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PuzzleMapper puzzleMapper;

    /**
     * 查找所有主题
     */
    @GetMapping("/findAll")
    public R findAll() {
        List<Theme> themeList = themeService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("themeList", themeList);
        return R.ok(map);
    }

    /**
     * 根据id查询主题详情
     */
    @GetMapping("/detail")
    public R detail(int themeId) {
        Theme theme = themeService.getById(themeId);
        Map<String, Object> map=new HashMap<>();
        map.put("theme", theme);
        return R.ok(map);
    }


    /**
     * 根据themeId和userId查询主题收集进度详情（用于设置黑白显示）
     */
    @RequestMapping("/my/progressDetail")
    public R progressDetail(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
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

        // 查询主题下所有拼图
        List<Puzzle> puzzleList = puzzleService.list(new QueryWrapper<Puzzle>().eq("theme_id", themeId).orderByAsc("id"));

        // join查询puzzle的收集情况
        MPJLambdaWrapper<Puzzle> mpjLambdaWrapper = new MPJLambdaWrapper<Puzzle>()
                .selectAs(Puzzle::getId, "puzzleId")
                .eq(Puzzle::getThemeId, themeId)
                .leftJoin(Quest.class, Quest::getPuzzleId, Puzzle::getId) // 1vs多
                .leftJoin(UserQuest.class, UserQuest::getQuestId, Quest::getId) // 1vs多
                .eq(UserQuest::getUserId, userId)
                .eq(UserQuest::getQuestState, 2)
                .groupBy(Puzzle::getId)
                .orderByAsc(Puzzle::getId);
        List<Map<String, Object>> resultList = puzzleMapper.selectJoinMaps(mpjLambdaWrapper);

        // 根据收集情况设置主题下各puzzle的isCollected
        for (int i = 0; i < resultList.size(); i++) {
            Map<String, Object> map = resultList.get(i);
            puzzleList.get(i).setCollected(true);
        }

        System.out.println(puzzleList);

        Map<String, Object> map=new HashMap<>();
        map.put("puzzleList", puzzleList);
        return R.ok(map);
    }



}
