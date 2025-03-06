package com.example.windpredictbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.windpredictbackend.model.TurbineData;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.windpredictbackend.model.dto.TurbineQueryRequest;

import java.util.List;

/**
* @author Michael
* @description 针对表【turbine_data】的数据库操作Service
* @createDate 2025-03-06 14:53:48
*/
public interface TurbineDataService extends IService<TurbineData> {

    /**
     * 根据风机ID查询数据
     * @return
     */
    List<TurbineData> queryTurbineDataByTurbId(String turbId);

    List<TurbineData> queryTurbineDataByTurbIdAndTimeRange(String turbId,String startTime,String endTime);

    QueryWrapper<TurbineData> getQueryWrapper(TurbineQueryRequest turbineQueryRequest);

}
