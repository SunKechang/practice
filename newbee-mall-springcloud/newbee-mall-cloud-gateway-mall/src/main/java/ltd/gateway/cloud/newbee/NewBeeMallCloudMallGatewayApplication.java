
package ltd.gateway.cloud.newbee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class NewBeeMallCloudMallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewBeeMallCloudMallGatewayApplication.class, args);
    }

}
