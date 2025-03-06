package com.example.windpredictbackend.once;

import com.example.windpredictbackend.model.TurbineData;
import com.example.windpredictbackend.service.TurbineDataService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Michael
 */
@Component
public class UploadData {

    @Resource
    private TurbineDataService turbineDataService;

    /**
     * 并发线程上传数据
     */
    @Scheduled
    public void uploadData(){
        List<CompletableFuture> completableFutureList=new ArrayList<>();
        final int DATA_NUM=1000000;
        final int BATCH_SIZE=10000;

        int j=0;
        for(int i=0;i<DATA_NUM/BATCH_SIZE;i++){
            List<TurbineData> list=new ArrayList<>();
            while(true){
                TurbineData turbineData = new TurbineData();
                list.add(turbineData);
                j++;
                if(i%BATCH_SIZE==0){
                    break;
                }
            }
            CompletableFuture<Void> runFuture = CompletableFuture.runAsync(() -> {
                turbineDataService.saveBatch(list);
            });
            completableFutureList.add(runFuture);
        }

        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[]{})).join();
    }

}
