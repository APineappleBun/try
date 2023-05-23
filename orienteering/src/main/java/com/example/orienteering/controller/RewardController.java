package com.example.orienteering.controller;

import com.example.orienteering.entity.R;
import com.example.orienteering.entity.Reward;
import com.example.orienteering.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
