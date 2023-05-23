package com.example.orienteering.mapper;

import com.example.orienteering.entity.Puzzle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orienteering.entity.UserQuest;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sigmaliu
* @description 针对表【puzzle(拼图表)】的数据库操作Mapper
* @createDate 2023-04-25 23:23:42
* @Entity com.example.orienteering.entity.Puzzle
*/
@Mapper
public interface PuzzleMapper extends BaseMapper<Puzzle>, MPJBaseMapper<Puzzle> {

}




