package dan.tp2021.productos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DanMsProductosApplication {

	public static void main(String[] args) {
		SpringApplication.run(DanMsProductosApplication.class, args);
	}

}
