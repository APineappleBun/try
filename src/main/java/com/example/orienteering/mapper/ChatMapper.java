package com.example.orienteering.mapper;

import com.example.orienteering.entity.Chat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【chat(聊天消息表)】的数据库操作Mapper
* @createDate 2023-05-09 11:22:58
* @Entity com.example.orienteering.entity.Chat
*/
@Mapper
public interface ChatMapper extends BaseMapper<Chat> {

}




