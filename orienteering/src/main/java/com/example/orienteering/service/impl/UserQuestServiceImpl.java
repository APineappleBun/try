package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.UserQuest;
import com.example.orienteering.service.UserQuestService;
import com.example.orienteering.mapper.UserQuestMapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【user_quest(用户任务表)】的数据库操作Service实现
* @createDate 2023-04-27 15:45:49
*/
@Service
public class UserQuestServiceImpl extends ServiceImpl<UserQuestMapper, UserQuest>
//public class UserQuestServiceImpl extends MPJBaseServiceImpl<UserQuestMapper, UserQuest>
    implements UserQuestService{

}




