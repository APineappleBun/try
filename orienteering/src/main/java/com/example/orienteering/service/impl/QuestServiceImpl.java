package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.Quest;
import com.example.orienteering.service.QuestService;
import com.example.orienteering.mapper.QuestMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【quest(任务表)】的数据库操作Service实现
* @createDate 2023-04-25 23:23:45
*/
@Service
public class QuestServiceImpl extends ServiceImpl<QuestMapper, Quest>
    implements QuestService{

}




