package com.example.windpredictbackend.mapper;

import com.example.windpredictbackend.model.TurbineData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author Michael
* @description 针对表【turbine_data】的数据库操作Mapper
* @createDate 2025-03-06 14:53:48
* @Entity com.example.windpredictbackend.model.TurbineData
*/
public interface TurbineDataMapper extends BaseMapper<TurbineData> {
    /**
     * 根据turb_id查询数据
     * @param turbId
     * @return
     */
    List<TurbineData> selectByTurbId(String turbId);

    /**
     * 根据turbId和时间查询数据
     * Map中应有三对K-V，K值分别为：turbId，startTime，endTime，
     * @param params
     * @return
     */
    List<TurbineData> selectByTurbIdAndDateTimeRange(Map<String,Object> params);

}




