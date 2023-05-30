package com.example.orienteering.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orienteering.entity.Puzzle;
import com.example.orienteering.service.PuzzleService;
import com.example.orienteering.mapper.PuzzleMapper;
import org.springframework.stereotype.Service;

/**
* @author sigmaliu
* @description 针对表【puzzle(拼图表)】的数据库操作Service实现
* @createDate 2023-04-25 23:23:42
*/
@Service
public class PuzzleServiceImpl extends ServiceImpl<PuzzleMapper, Puzzle>
    implements PuzzleService{

}




