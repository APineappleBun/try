package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.Theme;
import com.example.orienteering.service.ThemeService;
import com.example.orienteering.mapper.ThemeMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【theme(主题表)】的数据库操作Service实现
* @createDate 2023-04-25 23:23:50
*/
@Service
public class ThemeServiceImpl extends ServiceImpl<ThemeMapper, Theme>
    implements ThemeService{

}




