
package ltd.goods.cloud.newbee;

import ltd.user.cloud.newbee.openfeign.NewBeeCloudUserServiceFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("ltd.goods.cloud.newbee.dao")
@EnableFeignClients(basePackageClasses = {NewBeeCloudUserServiceFeign.class,
        ltd.recommend.cloud.newbee.openfeign.NewBeeCloudRecommendServiceFeign.class})
public class NewBeeMallCloudGoodsServiceApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(NewBeeMallCloudGoodsServiceApplication.class, args);
    }

}
