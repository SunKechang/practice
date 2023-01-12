
package ltd.order.cloud.newbee;

import ltd.goods.cloud.newbee.openfeign.NewBeeCloudGoodsServiceFeign;
import ltd.shopcart.cloud.newbee.openfeign.NewBeeCloudShopCartServiceFeign;
import ltd.user.cloud.newbee.openfeign.CloudUserServiceFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("ltd.order.cloud.newbee.dao")
@EnableFeignClients(basePackageClasses ={CloudUserServiceFeign.class, NewBeeCloudGoodsServiceFeign.class, NewBeeCloudShopCartServiceFeign.class})
public class NewBeeMallCloudOrderServiceApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(NewBeeMallCloudOrderServiceApplication.class, args);
    }

}
