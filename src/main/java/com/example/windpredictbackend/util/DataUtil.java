package com.example.windpredictbackend.util;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUtil {

    /**
     * 将csv文件转成String
     * @param csvFilePath
     * @return
     */
    public static String convertCsvToJson(String csvFilePath) {
        try {

            // 创建 BufferedReader 以读取 CSV 文件
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String[] headers={
                    "TurbId","DATATIME","ROUND(A.POWER,0)","YD15"
            };



            // 创建 Gson 对象
            Gson gson = new Gson();
            JsonArray jsonArray = new JsonArray();
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                JsonObject jsonObject = new JsonObject();
                for (int i = 0; i < headers.length; i++) {
                    jsonObject.addProperty(headers[i], values[i]);
                }

                jsonArray.add(jsonObject);
            }

            // 关闭 BufferedReader
            reader.close();

            // 转换为 JSON 字符串
            return gson.toJson(jsonArray);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 时间格式转换
     * @param originalDateString
     * @return
     */
    public static String formatTime(String originalDateString){
        String targetDateString="";
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date originalDate = originalFormat.parse(originalDateString);
            targetDateString = targetFormat.format(originalDate);
//            System.out.println("转换后的时间字符串：" + targetDateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetDateString;
    }


    public static String fillNull(Object o){
        return o==null?"":o.toString();
    }
}

