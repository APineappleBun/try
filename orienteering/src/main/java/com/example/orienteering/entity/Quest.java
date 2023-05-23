package com.example.orienteering.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Data;

/**
 * 任务表
 * @TableName quest
 */
@TableName(value ="quest")
@Data
public class Quest implements Serializable {
    /**
     * 任务编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 任务终点
     */
    @TableField(value = "destination")
    private GeoPoint destination;

    /**
     * 任务地图
     */
    @TableField(value = "map")
    private String map;

    /**
     * 任务宣传图
     */
    @TableField(value = "image")
    private String image;

    /**
     * 任务说明
     */
    @TableField(value = "description")
    private String description;

    /**
     * 任务等级
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 任务时长
     */
    @TableField(value = "max_time")
    private LocalTime maxTime;

    /**
     * 任务距离
     */
    @TableField(value = "distance")
    private Integer distance;

    /**
     * 城市
     */
    @TableField(value = "city")
    private String city;

    /**
     * 主题编号
     */
    @TableField(value = "theme_id")
    private Integer themeId;

    /**
     * 奖励编号
     */
    @TableField(value = "reward_id")
    private Integer rewardId;

    /**
     * 拼图编号
     */
    @TableField(value = "puzzle_id")
    private Integer puzzleId;

    /**
     * 景点编号
     */
    @TableField(value = "attraction_id")
    private Integer attractionId;

    /**
     * 是否轮播
     */
    @TableField(value = "is_swiper")
    private Integer isSwiper;

//    @TableField(select = false, exist = false)
//    private Reward reward;
//
//    @TableField(select = false, exist = false)
//    private Attraction attraction;
//
//    @TableField(select = false, exist = false)
//    private Puzzle puzzle;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Quest other = (Quest) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getDestination() == null ? other.getDestination() == null : this.getDestination().equals(other.getDestination()))
            && (this.getMap() == null ? other.getMap() == null : this.getMap().equals(other.getMap()))
            && (this.getImage() == null ? other.getImage() == null : this.getImage().equals(other.getImage()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
            && (this.getMaxTime() == null ? other.getMaxTime() == null : this.getMaxTime().equals(other.getMaxTime()))
            && (this.getDistance() == null ? other.getDistance() == null : this.getDistance().equals(other.getDistance()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getThemeId() == null ? other.getThemeId() == null : this.getThemeId().equals(other.getThemeId()))
            && (this.getRewardId() == null ? other.getRewardId() == null : this.getRewardId().equals(other.getRewardId()))
            && (this.getPuzzleId() == null ? other.getPuzzleId() == null : this.getPuzzleId().equals(other.getPuzzleId()))
            && (this.getAttractionId() == null ? other.getAttractionId() == null : this.getAttractionId().equals(other.getAttractionId()))
            && (this.getIsSwiper() == null ? other.getIsSwiper() == null : this.getIsSwiper().equals(other.getIsSwiper()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getDestination() == null) ? 0 : getDestination().hashCode());
        result = prime * result + ((getMap() == null) ? 0 : getMap().hashCode());
        result = prime * result + ((getImage() == null) ? 0 : getImage().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getMaxTime() == null) ? 0 : getMaxTime().hashCode());
        result = prime * result + ((getDistance() == null) ? 0 : getDistance().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getThemeId() == null) ? 0 : getThemeId().hashCode());
        result = prime * result + ((getRewardId() == null) ? 0 : getRewardId().hashCode());
        result = prime * result + ((getPuzzleId() == null) ? 0 : getPuzzleId().hashCode());
        result = prime * result + ((getAttractionId() == null) ? 0 : getAttractionId().hashCode());
        result = prime * result + ((getIsSwiper() == null) ? 0 : getIsSwiper().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", destination=").append(destination);
        sb.append(", map=").append(map);
        sb.append(", image=").append(image);
        sb.append(", description=").append(description);
        sb.append(", level=").append(level);
        sb.append(", maxTime=").append(maxTime);
        sb.append(", distance=").append(distance);
        sb.append(", city=").append(city);
        sb.append(", themeId=").append(themeId);
        sb.append(", rewardId=").append(rewardId);
        sb.append(", puzzleId=").append(puzzleId);
        sb.append(", attractionId=").append(attractionId);
        sb.append(", isSwiper=").append(isSwiper);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}