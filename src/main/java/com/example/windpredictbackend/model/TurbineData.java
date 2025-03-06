package com.example.windpredictbackend.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName turbine_data
 */
@TableName(value ="turbine_data")
@Data
public class TurbineData implements Serializable {
    /**
     * 
     */
    @TableId(value = "turb_id")
    private Integer turb_id;

    /**
     * 
     */
    @TableId(value = "data_time")
    private Date data_time;

    /**
     * 
     */
    @TableField(value = "wind_speed")
    private Double wind_speed;

    /**
     * 
     */
    @TableField(value = "prepower")
    private Double prepower;

    /**
     * 
     */
    @TableField(value = "wind_direction")
    private Double wind_direction;

    /**
     * 
     */
    @TableField(value = "temperature")
    private Double temperature;

    /**
     * 
     */
    @TableField(value = "humidity")
    private Double humidity;

    /**
     * 
     */
    @TableField(value = "pressure")
    private Double pressure;

    /**
     * 
     */
    @TableField(value = "round_a_ws")
    private Double round_a_ws;

    /**
     * 
     */
    @TableField(value = "round_a_power")
    private Double round_a_power;

    /**
     * 
     */
    @TableField(value = "yd15")
    private Double yd15;

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
        TurbineData other = (TurbineData) that;
        return (this.getTurb_id() == null ? other.getTurb_id() == null : this.getTurb_id().equals(other.getTurb_id()))
            && (this.getData_time() == null ? other.getData_time() == null : this.getData_time().equals(other.getData_time()))
            && (this.getWind_speed() == null ? other.getWind_speed() == null : this.getWind_speed().equals(other.getWind_speed()))
            && (this.getPrepower() == null ? other.getPrepower() == null : this.getPrepower().equals(other.getPrepower()))
            && (this.getWind_direction() == null ? other.getWind_direction() == null : this.getWind_direction().equals(other.getWind_direction()))
            && (this.getTemperature() == null ? other.getTemperature() == null : this.getTemperature().equals(other.getTemperature()))
            && (this.getHumidity() == null ? other.getHumidity() == null : this.getHumidity().equals(other.getHumidity()))
            && (this.getPressure() == null ? other.getPressure() == null : this.getPressure().equals(other.getPressure()))
            && (this.getRound_a_ws() == null ? other.getRound_a_ws() == null : this.getRound_a_ws().equals(other.getRound_a_ws()))
            && (this.getRound_a_power() == null ? other.getRound_a_power() == null : this.getRound_a_power().equals(other.getRound_a_power()))
            && (this.getYd15() == null ? other.getYd15() == null : this.getYd15().equals(other.getYd15()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTurb_id() == null) ? 0 : getTurb_id().hashCode());
        result = prime * result + ((getData_time() == null) ? 0 : getData_time().hashCode());
        result = prime * result + ((getWind_speed() == null) ? 0 : getWind_speed().hashCode());
        result = prime * result + ((getPrepower() == null) ? 0 : getPrepower().hashCode());
        result = prime * result + ((getWind_direction() == null) ? 0 : getWind_direction().hashCode());
        result = prime * result + ((getTemperature() == null) ? 0 : getTemperature().hashCode());
        result = prime * result + ((getHumidity() == null) ? 0 : getHumidity().hashCode());
        result = prime * result + ((getPressure() == null) ? 0 : getPressure().hashCode());
        result = prime * result + ((getRound_a_ws() == null) ? 0 : getRound_a_ws().hashCode());
        result = prime * result + ((getRound_a_power() == null) ? 0 : getRound_a_power().hashCode());
        result = prime * result + ((getYd15() == null) ? 0 : getYd15().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", turb_id=").append(turb_id);
        sb.append(", data_time=").append(data_time);
        sb.append(", wind_speed=").append(wind_speed);
        sb.append(", prepower=").append(prepower);
        sb.append(", wind_direction=").append(wind_direction);
        sb.append(", temperature=").append(temperature);
        sb.append(", humidity=").append(humidity);
        sb.append(", pressure=").append(pressure);
        sb.append(", round_a_ws=").append(round_a_ws);
        sb.append(", round_a_power=").append(round_a_power);
        sb.append(", yd15=").append(yd15);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}