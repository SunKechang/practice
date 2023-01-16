
package ltd.shopcart.cloud.newbee;

import ltd.goods.cloud.newbee.openfeign.NewBeeCloudGoodsServiceFeign;
import ltd.user.cloud.newbee.openfeign.NewBeeCloudUserServiceFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("ltd.shopcart.cloud.newbee.dao")
@EnableFeignClients(basePackageClasses ={NewBeeCloudUserServiceFeign.class, NewBeeCloudGoodsServiceFeign.class})
public class NewBeeMallCloudShopCartServiceApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        SpringApplication.run(NewBeeMallCloudShopCartServiceApplication.class, args);
    }

}
