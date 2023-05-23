package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.Friend;
import com.example.orienteering.service.FriendService;
import com.example.orienteering.mapper.FriendMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【friend(好友表)】的数据库操作Service实现
* @createDate 2023-05-09 14:16:35
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

}




