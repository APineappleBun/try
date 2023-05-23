package com.example.orienteering.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.*;
import com.example.orienteering.mapper.QuestMapper;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拼图Controller
 */

@RestController
@RequestMapping("/puzzle")
public class PuzzleController {

    @Autowired
    private PuzzleService puzzleService;

    @Autowired
    private UserQuestService userQuestService;

    @Autowired
    private QuestMapper questMapper;

    @Autowired
    private QuestService questService;

    @Autowired
    private UserService userService;

    /**
     * 查询所有puzzle
     */
    @GetMapping("/findAll")
    public R findAll() {
        List<Puzzle> puzzleList = puzzleService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("puzzleList", puzzleList);
        return R.ok(map);
    }

    /**
     * 根据id查询puzzle详情
     */
    @GetMapping("/detail")
    public R detail(int puzzleId){
        Puzzle puzzle = puzzleService.getById(puzzleId);
        Map<String, Object> map=new HashMap<>();
        map.put("puzzle", puzzle);
        return R.ok(map);
    }

    /**
     * 根据puzzleId查询所有任务
     */
    @GetMapping("/findAllQuest")
    public R findAllQuest(int puzzleId) {
        List<Quest> questList = questService.list(new QueryWrapper<Quest>().eq("puzzle_id", puzzleId));
        Map<String, Object> map=new HashMap<>();
        map.put("questList", questList);
        return R.ok(map);
    }

    /**
     * 根据puzzleId和userId查询历史完成任务
     */
    @RequestMapping("/my/getHistory")
//    public R getHistory(int userId, int puzzleId) {
    public R getHistory(@RequestBody Map<String, Object> json, @RequestHeader(value = "token") String token) {
        int userId = (int) json.get("userId");
        int puzzleId = (int) json.get("puzzleId");

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

        // 根据puzzleId和userId查询对应的UserQuest
        MPJLambdaWrapper<Quest> mpjLambdaWrapper = new MPJLambdaWrapper<Quest>()
                .selectAs(UserQuest::getId, "userQuestId")
                .eq(Quest::getPuzzleId, puzzleId)
                .leftJoin(UserQuest.class, UserQuest::getQuestId, Quest::getId) // 1vs多
                .eq(UserQuest::getUserId, userId)
                .eq(UserQuest::getQuestState, 2); // 已完成
        MPJLambdaWrapper<Quest> mpjLambdaWrapper2 = mpjLambdaWrapper.orderByAsc(UserQuest::getId);

        List<Map<String, Object>> resultList = questMapper.selectJoinMaps(mpjLambdaWrapper2); // 查询结果列表
        int currentAmount = resultList.size();

        // 根据userQuestId从数据库中查找userQuest
        List<UserQuest> userQuestList = new ArrayList<>();
        for (Map<String, Object> map: resultList) {
            Integer userQuestId = (Integer) map.get("userQuestId");
            userQuestList.add(userQuestService.getById(userQuestId));
        }
        System.out.println(userQuestList);

        // 最佳记录
        MPJLambdaWrapper<Quest> mpjLambdaWrapper3 = mpjLambdaWrapper.orderByAsc(UserQuest::getTotalTime).last("limit 1");
        int bestUserQuestId = (int) questMapper.selectJoinMap(mpjLambdaWrapper3).get("userQuestId");
        UserQuest bestUserQuest = userQuestService.getById(bestUserQuestId);

        Map<String, Object> map=new HashMap<>();
        map.put("userQuestList", userQuestList);
        map.put("bestUserQuest", bestUserQuest);
        map.put("currentAmount", currentAmount);
        return R.ok(map);
    }

}
