package ir.sbpro.springdb;

import ir.sbpro.springdb.db_updater.ActiveSalesUpdate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringdbApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringdbApplication.class, args);
        /*ActiveSalesUpdate activeSalesUpdate = new ActiveSalesUpdate();
        activeSalesUpdate.update();*/
    }
}
