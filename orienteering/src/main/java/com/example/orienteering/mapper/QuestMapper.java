package com.example.orienteering.mapper;

import com.example.orienteering.entity.Quest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orienteering.entity.UserQuest;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【quest(任务表)】的数据库操作Mapper
* @createDate 2023-04-25 23:23:45
* @Entity com.example.orienteering.entity.Quest
*/
@Mapper
public interface QuestMapper extends BaseMapper<Quest>, MPJBaseMapper<Quest> {

}




