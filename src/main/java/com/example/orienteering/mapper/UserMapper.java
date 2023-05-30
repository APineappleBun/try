package com.example.orienteering.mapper;

import com.example.orienteering.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2023-04-29 22:44:29
* @Entity com.example.orienteering.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




