package in.hocg.boot.web.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by hocgin on 2020/8/15
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

//    @Bean("writer1")
//    public Writer getWriter1() {
//        return new Writer("工作 1");
//    }
//
//    @Bean("writer2")
//    public Writer getWriter2() {
//        return new Writer("工作 2");
//    }

}
