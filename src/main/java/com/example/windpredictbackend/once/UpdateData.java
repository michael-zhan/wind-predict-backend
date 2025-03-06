package com.example.windpredictbackend.once;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.windpredictbackend.exception.BusinessException;
import com.example.windpredictbackend.exception.ErrorCode;
import com.example.windpredictbackend.exception.ThrowUtils;
import com.example.windpredictbackend.model.TurbineData;
import com.example.windpredictbackend.service.TurbineDataService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Michael
 */
@Component
public class UpdateData {

    @Resource
    private RedisTemplate<String,String> redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private TurbineDataService turbineDataService;
    private static final Cache<String,String> LOCAL_CACHE= Caffeine.newBuilder().
            initialCapacity(1024).maximumSize(10000L).expireAfterWrite(5, TimeUnit.MINUTES).build();

    /**
     * 分布式锁控制缓存预热
     */
    @Scheduled
    public void updateCache(){
        QueryWrapper<TurbineData> queryWrapper = new QueryWrapper<>();
        String key=String.format("michael:wind:predict:%s",queryWrapper);

        RLock lock = redissonClient.getLock(key);
        try {
            if(lock.tryLock(0,30L,TimeUnit.MINUTES)){
                // 查数据库
                Page<TurbineData> page = turbineDataService.page(new Page<>(1, 10), queryWrapper);
                String cacheStr = JSONUtil.toJsonStr(page);
                // 写缓存
                LOCAL_CACHE.put(key,cacheStr);
                ValueOperations<String, String> ops = redisTemplate.opsForValue();
                ops.set(key,cacheStr,10,TimeUnit.MINUTES);
            }
        } catch (InterruptedException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }finally {
            if(lock.isHeldByCurrentThread()){
                lock.unlock();
            }
        }

    }

}
