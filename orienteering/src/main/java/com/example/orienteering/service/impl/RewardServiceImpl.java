package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.Reward;
import com.example.orienteering.service.RewardService;
import com.example.orienteering.mapper.RewardMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【reward(奖励表)】的数据库操作Service实现
* @createDate 2023-04-25 23:23:47
*/
@Service
public class RewardServiceImpl extends ServiceImpl<RewardMapper, Reward>
    implements RewardService{

}




