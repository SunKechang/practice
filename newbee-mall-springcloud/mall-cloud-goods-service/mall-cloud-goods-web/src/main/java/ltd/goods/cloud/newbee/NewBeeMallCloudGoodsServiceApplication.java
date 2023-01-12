
package ltd.goods.cloud.newbee;

import ltd.recommend.cloud.newbee.openfeign.CloudRecommendServiceFeign;
import ltd.user.cloud.newbee.openfeign.CloudUserServiceFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("ltd.goods.cloud.newbee.dao")
@EnableFeignClients(basePackageClasses = {CloudUserServiceFeign.class,
        CloudRecommendServiceFeign.class})
public class NewBeeMallCloudGoodsServiceApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(NewBeeMallCloudGoodsServiceApplication.class, args);
    }

}
