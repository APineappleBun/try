package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.User;
import com.example.orienteering.service.UserService;
import com.example.orienteering.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-04-29 22:44:29
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




