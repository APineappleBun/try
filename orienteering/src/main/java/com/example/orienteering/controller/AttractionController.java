package com.example.orienteering.controller;

import com.example.orienteering.entity.Attraction;
import com.example.orienteering.entity.R;
import com.example.orienteering.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 景点Controller
 */

@RestController
@RequestMapping("/attraction")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    /**
     * 查找所有Attraction
     */
    @GetMapping("/findAll")
    public R findAll() {
        List<Attraction> allAttractionList = attractionService.list();
        Map<String, Object> map = new HashMap<>();
        map.put("message", allAttractionList);
        return R.ok(map);
    }

    /**
     * 根据id查询景点
     */
    @GetMapping("/detail")
    public R detail(int attractionId){
        Attraction attraction = attractionService.getById(attractionId);
        Map<String, Object> map=new HashMap<>();
        map.put("attraction", attraction);
        return R.ok(map);
    }

    /**
     * 随机获取一个Attraction
     */
    @GetMapping("/findRandomOne")
    public R findRandomOne() {
        int attractionAmount = attractionService.count();
        int id = (int) Math.floor(Math.random() * (attractionAmount - 1) + 1);// 随机
        Attraction attraction = attractionService.getById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("attraction", attraction);
        return R.ok(map);
    }


}
