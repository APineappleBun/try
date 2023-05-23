package com.example.orienteering.mapper;

import com.example.orienteering.entity.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【friend(好友表)】的数据库操作Mapper
* @createDate 2023-05-09 14:16:35
* @Entity com.example.orienteering.entity.Friend
*/

@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

}




