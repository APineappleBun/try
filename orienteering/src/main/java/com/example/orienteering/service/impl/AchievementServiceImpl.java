package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.Achievement;
import com.example.orienteering.service.AchievementService;
import com.example.orienteering.mapper.AchievementMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【achievement(用户成就表)】的数据库操作Service实现
* @createDate 2023-04-30 17:09:51
*/
@Service
public class AchievementServiceImpl extends ServiceImpl<AchievementMapper, Achievement>
    implements AchievementService{

}




