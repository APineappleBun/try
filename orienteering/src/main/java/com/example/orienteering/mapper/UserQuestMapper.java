package com.example.orienteering.mapper;

import com.example.orienteering.entity.UserQuest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【user_quest(用户任务表)】的数据库操作Mapper
* @createDate 2023-04-27 15:45:49
* @Entity com.example.orienteering.entity.UserQuest
*/
@Mapper
public interface UserQuestMapper extends BaseMapper<UserQuest>, MPJBaseMapper<UserQuest> {

}




