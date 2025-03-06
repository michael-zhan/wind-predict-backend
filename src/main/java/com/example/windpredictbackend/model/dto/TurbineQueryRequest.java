package com.example.windpredictbackend.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.windpredictbackend.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Michael
 */
@Data
public class TurbineQueryRequest extends PageRequest {

    /**
     *
     */
    private Integer turb_id;

    /**
     *
     */
    private Date data_time;

    /**
     *
     */
    private Double wind_speed;

    /**
     *
     */
    private Double prepower;

    /**
     *
     */
    private Double wind_direction;

    /**
     *
     */
    private Double temperature;

    /**
     *
     */
    private Double humidity;

    /**
     *
     */
    private Double pressure;

    /**
     *
     */
    private Double round_a_ws;

    /**
     *
     */
    private Double round_a_power;

    /**
     *
     */
    private Double yd15;

}
