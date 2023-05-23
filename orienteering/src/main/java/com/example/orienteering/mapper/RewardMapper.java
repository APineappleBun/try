package com.example.orienteering.mapper;

import com.example.orienteering.entity.Reward;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【reward(奖励表)】的数据库操作Mapper
* @createDate 2023-04-25 23:23:47
* @Entity com.example.orienteering.entity.Reward
*/
@Mapper
public interface RewardMapper extends BaseMapper<Reward> {

}




