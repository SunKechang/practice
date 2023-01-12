
package ltd.recommend.cloud.newbee;

import ltd.user.cloud.newbee.openfeign.CloudUserServiceFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("ltd.recommend.cloud.newbee.dao")
@EnableFeignClients(basePackageClasses =
        {CloudUserServiceFeign.class,
         ltd.goods.cloud.newbee.openfeign.NewBeeCloudGoodsServiceFeign.class})
public class NewBeeMallCloudRecommendServiceApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(NewBeeMallCloudRecommendServiceApplication.class, args);
    }

}
