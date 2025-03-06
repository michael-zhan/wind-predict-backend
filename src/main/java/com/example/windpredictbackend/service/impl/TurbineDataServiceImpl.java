package com.example.windpredictbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.windpredictbackend.model.TurbineData;
import com.example.windpredictbackend.model.dto.TurbineQueryRequest;
import com.example.windpredictbackend.service.TurbineDataService;
import com.example.windpredictbackend.mapper.TurbineDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Michael
* @description 针对表【turbine_data】的数据库操作Service实现
* @createDate 2025-03-06 14:53:48
*/
@Service
public class TurbineDataServiceImpl extends ServiceImpl<TurbineDataMapper, TurbineData>
    implements TurbineDataService{

    @Resource
    private TurbineDataMapper turbineDataMapper;

    @Override
    public List<TurbineData> queryTurbineDataByTurbId(String turbId) {
        return turbineDataMapper.selectByTurbId(turbId);
    }

    @Override
    public List<TurbineData> queryTurbineDataByTurbIdAndTimeRange(String turbId, String startTime, String endTime) {
        Map<String,Object> params=new HashMap<>();
        params.put("turbId",turbId);
        params.put("startTime",startTime);
        params.put("endTime",endTime);
        return turbineDataMapper.selectByTurbIdAndDateTimeRange(params);
    }

    @Override
    public QueryWrapper<TurbineData> getQueryWrapper(TurbineQueryRequest turbineQueryRequest) {
        QueryWrapper<TurbineData> wrapper = new QueryWrapper<>();
        wrapper.eq(turbineQueryRequest.getTurb_id()!=null,"turb_id",turbineQueryRequest.getTurb_id());
        wrapper.eq(turbineQueryRequest.getPrepower()!=null,"pre_power",turbineQueryRequest.getPrepower());
        wrapper.eq(turbineQueryRequest.getTemperature()!=null,"temperature",turbineQueryRequest.getTemperature());
        return wrapper;
    }


}




