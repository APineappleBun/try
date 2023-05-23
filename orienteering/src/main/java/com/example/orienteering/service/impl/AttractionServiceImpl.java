package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.Attraction;
import com.example.orienteering.service.AttractionService;
import com.example.orienteering.mapper.AttractionMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【attraction(景点表)】的数据库操作Service实现
* @createDate 2023-04-25 23:23:39
*/
@Service
public class AttractionServiceImpl extends ServiceImpl<AttractionMapper, Attraction>
    implements AttractionService{

}




