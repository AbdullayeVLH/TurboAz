package az.code.turboplus;

import az.code.turboplus.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TurboPlusApplication implements CommandLineRunner {

    @Autowired
    ScheduleService service;

    public static void main(String[] args) {
        SpringApplication.run(TurboPlusApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {

    }
}
