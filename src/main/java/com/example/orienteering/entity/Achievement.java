package com.example.orienteering.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 用户成就表
 * @TableName achievement
 */
@TableName(value ="achievement")
@Data
public class Achievement implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 越野数量
     */
    @TableField(value = "quest_amount")
    private Integer questAmount;

    /**
     * 城市数量
     */
    @TableField(value = "city_amount")
    private Integer cityAmount;

    /**
     * 成就说明
     */
    @TableField(value = "description")
    private String description;

    /**
     * 用户编号
     */
    @TableField(value = "user_id")
    private Integer userId;

    @TableField(select = false)
    private Integer ranking;

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
        Achievement other = (Achievement) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getQuestAmount() == null ? other.getQuestAmount() == null : this.getQuestAmount().equals(other.getQuestAmount()))
            && (this.getCityAmount() == null ? other.getCityAmount() == null : this.getCityAmount().equals(other.getCityAmount()))
            && (this.getDescription() == null ? other.getDescription() == null : this.getDescription().equals(other.getDescription()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getQuestAmount() == null) ? 0 : getQuestAmount().hashCode());
        result = prime * result + ((getCityAmount() == null) ? 0 : getCityAmount().hashCode());
        result = prime * result + ((getDescription() == null) ? 0 : getDescription().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", questAmount=").append(questAmount);
        sb.append(", cityAmount=").append(cityAmount);
        sb.append(", description=").append(description);
        sb.append(", userId=").append(userId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}