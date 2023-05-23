package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.Chat;
import com.example.orienteering.service.ChatService;
import com.example.orienteering.mapper.ChatMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【chat(聊天消息表)】的数据库操作Service实现
* @createDate 2023-05-09 11:22:58
*/
@Service
public class ChatServiceImpl extends ServiceImpl<ChatMapper, Chat>
    implements ChatService{

}




