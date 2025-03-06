package com.example.windpredictbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Michael
 */
@SpringBootApplication
@MapperScan("com/example/windpredictbackend/mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
public class WindPredictBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WindPredictBackendApplication.class, args);
    }

}
