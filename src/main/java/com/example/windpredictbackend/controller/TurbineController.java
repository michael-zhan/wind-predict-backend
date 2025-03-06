package com.example.windpredictbackend.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.windpredictbackend.common.BaseResponse;
import com.example.windpredictbackend.common.ResultUtils;
import com.example.windpredictbackend.exception.ErrorCode;
import com.example.windpredictbackend.exception.ThrowUtils;
import com.example.windpredictbackend.model.TurbineData;
import com.example.windpredictbackend.model.dto.TurbineQueryRequest;
import com.example.windpredictbackend.service.TurbineDataService;
import com.example.windpredictbackend.util.DataUtil;
import com.example.windpredictbackend.util.PythonUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Michael
 */
@RestController
@RequestMapping("/turbine")
public class TurbineController {


    @Resource
    private TurbineDataService turbineDataService;
    @Resource
    private RedisTemplate<String,String> redisTemplate;
    private static final Cache<String,String> LOCAL_CACHE= Caffeine.newBuilder().initialCapacity(1024).maximumSize(10000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();

    @GetMapping("predict")
    public BaseResponse predict(String turbId, String startTime, String endTime, @Value("${outfile.path}") String path,
                                @Value("${outfile.firstturbid}") String firstTurbId, @Value("${outfile.modelpath}") String modelPath,
                                @Value("${outfile.modelinfilepath}") String modeInfilePath, @Value("${outfile.modelinfilepathself}") String folderPath,
                                @Value("${outfile.pathself}") String pathSelf, @Value("${outfile.workpath}") String workPath) {
        cleanFolder(pathSelf);

        if(turbId==null|| "".equals(turbId)){
            //对所有风机进行预测并返回
            String data="";
            String[] turbIdsFormat =new String[10];
            for(int i=0;i<10;i++){
                int fId= Integer.parseInt(firstTurbId);
                turbId=new Integer(i+fId).toString();
                List<TurbineData> turbineDataList = turbineDataService.queryTurbineDataByTurbIdAndTimeRange(turbId, startTime, endTime);
                if(CollUtil.isNotEmpty(turbineDataList)){
                    String turbIdFormat = listToCSV(turbineDataList,modeInfilePath);
                    turbIdsFormat[i]=turbIdFormat;
                }
            }

            startPredict(modelPath,workPath);

            for(int i=0;i<10;i++){
                data += DataUtil.convertCsvToJson(path+turbIdsFormat[i]+"out.csv");
            }
            if(data.equals("")){
                return ResultUtils.error(ErrorCode.PARAMS_ERROR,"预测失败，数据不存在");
            }

            cleanFolder(pathSelf);
            return ResultUtils.success(data);
        }else {
            //对指定风机进行预测并返回
            List<TurbineData> turbineDataList = turbineDataService.queryTurbineDataByTurbIdAndTimeRange(turbId, startTime, endTime);
            if (turbineDataList == null || turbineDataList.isEmpty()) {
                return ResultUtils.error(ErrorCode.PARAMS_ERROR,"预测失败，数据不存在");
            } else {
                String turbIdFormat = listToCSV(turbineDataList,modeInfilePath);
                startPredict(modelPath,workPath);
                String data = DataUtil.convertCsvToJson(path+turbIdFormat+"out.csv");
                cleanFolder(pathSelf);
                return ResultUtils.success(data);
            }
        }
    }

    /**
     * 开始预测
     * 输出的文件在/pred路径下，但是如果有文件名重复，则后者不会覆盖
     */
    public void startPredict(String modelPath,String workPath) {
        //执行python脚本
        try {
            PythonUtil.execPython(modelPath,workPath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除文件夹下面的文件
     */
    public void cleanFolder(String folderPath){
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        // 递归删除子文件夹中的文件
                        cleanFolder(file.getAbsolutePath());
                    } else {
                        // 删除文件
                        file.delete();
                    }
                }
            }
        }
    }


    /**
     * 将list转成CSV文件存储在infile中，作为python脚本的输入
     * 如果路径重复则会覆盖
     * @param dataList
     */
    public String listToCSV(List<TurbineData> dataList,String modelInfilePath) {
        Integer turbId = dataList.get(0).getTurb_id();
        String turbIdFormat = String.format("%04d", turbId);

        String outputFilePath = modelInfilePath+turbIdFormat+"in.csv";
        try {
            FileWriter fileWriter = new FileWriter(outputFilePath);

            // 设置CSV文件的列名
            fileWriter.append("TurbID,DATATIME,WINDSPEED,PREPOWER,WINDDIRECTION,TEMPERATURE,HUMIDITY,PRESSURE,\"ROUND(A.WS,1)\",\"ROUND(A.POWER,0)\",YD15\n");

            // 逐行遍历查询结果并写入CSV文件
            for (TurbineData data : dataList) {
                // 从Data对象中获取相应的列值
                String column1 = DataUtil.fillNull(data.getTurb_id());
                String column2 = DataUtil.formatTime(DataUtil.fillNull(data.getData_time()));
                String column3 = DataUtil.fillNull(data.getWind_speed());
                String column4 = DataUtil.fillNull(data.getPrepower());
                String column5 = DataUtil.fillNull(data.getWind_direction());
                String column6=DataUtil.fillNull(data.getTemperature());
                String column7=DataUtil.fillNull(data.getHumidity());
                String column8=DataUtil.fillNull(data.getPressure());
                String column9=DataUtil.fillNull(data.getRound_a_ws());
                String column10=DataUtil.fillNull(data.getRound_a_power());
                String column11=DataUtil.fillNull(data.getYd15());
                // 写入CSV文件
                fileWriter.append(column1).append(",").append(column2).append(",").append(column3).append(",").append(column4).append(",").append(column5).append(",").
                        append(column6).append(",").append(column7).append(",").append(column8).append(",").append(column9).append(",").append(column10).
                        append(",").append(column11).append("\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("转成csv时出错");
        }

        return turbIdFormat;
    }


    @GetMapping("/list/page")
    public BaseResponse<Page<TurbineData>> listTurbinePage(@RequestBody TurbineQueryRequest turbineQueryRequest, HttpServletRequest request){
        ThrowUtils.throwIf(turbineQueryRequest==null,ErrorCode.PARAMS_ERROR);
        QueryWrapper<TurbineData> queryWrapper = turbineDataService.getQueryWrapper(turbineQueryRequest);
        String queryStr= JSONUtil.toJsonStr(queryWrapper);
        String key=String.format("michael:wind:predict:%s",queryStr);

        //1. 先从 caffeine 中查询
        String cacheValue = LOCAL_CACHE.getIfPresent(key);
        if(StrUtil.isNotBlank(cacheValue)){
            Page<TurbineData> page = JSONUtil.toBean(cacheValue, Page.class);
            return ResultUtils.success(page);
        }
        //2. 如果没有，再从redis中查询
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        if(redisTemplate.hasKey(key)){
            String cacheStr = ops.get(key);
            Page<TurbineData> page = JSONUtil.toBean(cacheStr, Page.class);
            // 写到 caffeine 中
            LOCAL_CACHE.put(key,cacheStr);
            return ResultUtils.success(page);
        }
        //3. 数据库查询结果
        Page<TurbineData> page = turbineDataService.page(new Page<>(turbineQueryRequest.getCurrent(), turbineQueryRequest.getPageSize()), queryWrapper);
        // 不校验是否为null，即使为null也写入缓存，避免缓存穿透
        String cacheStr = JSONUtil.toJsonStr(page);
        //4. 写入到 Caffeine 和 Redis 中
        ops.set(key,cacheStr,10, TimeUnit.MINUTES);
        LOCAL_CACHE.put(key,cacheStr);
        return ResultUtils.success(page);
    }

}
