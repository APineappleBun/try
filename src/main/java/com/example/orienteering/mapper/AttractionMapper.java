package com.example.orienteering.mapper;

import com.example.orienteering.entity.Attraction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【attraction(景点表)】的数据库操作
* @createDate 2023-04-25 23:23:39
* @Entity com.example.orienteering.entity.Attraction
*/
@Mapper
public interface AttractionMapper extends BaseMapper<Attraction> {

}




