package com.example.orienteering.mapper;

import com.example.orienteering.entity.Theme;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【theme(主题表)】的数据库操作Mapper
* @createDate 2023-04-25 23:23:50
* @Entity com.example.orienteering.entity.Theme
*/
@Mapper
public interface ThemeMapper extends BaseMapper<Theme> {

}




