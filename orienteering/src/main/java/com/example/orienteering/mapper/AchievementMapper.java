package com.example.orienteering.mapper;

import com.example.orienteering.entity.Achievement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【achievement(用户成就表)】的数据库操作Mapper
* @createDate 2023-04-30 17:09:51
* @Entity com.example.orienteering.entity.Achievement
*/
@Mapper
public interface AchievementMapper extends BaseMapper<Achievement> {

}




