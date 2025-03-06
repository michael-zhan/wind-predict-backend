package com.example.windpredictbackend.util;

import java.io.*;

public class PythonUtil {
    /**
     * 执行python脚本
     *
     * @param
     * @return
     * @throws IOException
     */
    public static void execPython(String scriptPath,String workPath) throws IOException, InterruptedException {

        //创建ProcessBuilder对象
        ProcessBuilder pb = new ProcessBuilder("python", scriptPath);

        //设置工作目录
        pb.directory(new File(workPath));

        //启动进程并等待脚本执行完成
        Process process = pb.start();
        process.waitFor();
        process.destroy();


    }
}
