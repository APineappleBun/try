package com.example.orienteering.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.orienteering.entity.*;
import com.example.orienteering.service.AttractionService;
import com.example.orienteering.service.PuzzleService;
import com.example.orienteering.service.QuestService;
import com.example.orienteering.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务Controller
 */

@RestController
@RequestMapping("/quest")
public class QuestController  {

    @Autowired
    private QuestService questService;
    @Autowired
    private RewardService rewardService;
    @Autowired
    private PuzzleService puzzleService;
    @Autowired
    private AttractionService attractionService;
    /**
     * 查询轮播任务
     */
    @GetMapping("/findSwiper")
    public R findSwiper() {
        List<Quest> swiperQuestList = questService.list(new QueryWrapper<Quest>().eq("is_swiper", 1).last("limit 3"));
//        List<String> swiperImageList = new ArrayList<>();
//        List<String> swiperIdList = new ArrayList<>();
        List<Map<String, String>> swiperList = new ArrayList<>();
        for (Quest q: swiperQuestList) {
//            swiperImageList.add("http://localhost:8088" + q.getImage());
//            swiperIdList.add(String.valueOf(q.getId()));
            Map<String,String> temp = new HashMap<>();
            temp.put("url", q.getImage());
            temp.put("id", String.valueOf(q.getId()));
            swiperList.add(temp);
        }
        Map<String, Object> map = new HashMap<>();
//        map.put("swiperImageList", swiperImageList);
//        map.put("swiperIdList", swiperIdList);
        map.put("swiperList", swiperList);
        return R.ok(map);
    }

    @GetMapping("/findAll")
    public R findAll() {
        List<Quest> questList = questService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("questList", questList);
        return R.ok(map);
    }

    /**
     * 根据id查询任务
     */
    @GetMapping("/detail")
    public R detail(int questId){
        Quest quest = questService.getById(questId);
        Reward reward = rewardService.getById(quest.getRewardId());
        Puzzle puzzle = puzzleService.getById(quest.getPuzzleId());
        Attraction attraction = attractionService.getById(quest.getAttractionId());
        Map<String, Object> map=new HashMap<>();
        map.put("quest", quest);
        map.put("reward", reward);
        map.put("puzzle", puzzle);
        map.put("attraction", attraction);
        return R.ok(map);
    }
}
