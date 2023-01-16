
package ltd.shopcart.cloud.newbee.openfeign;

import ltd.common.cloud.newbee.dto.Result;
import ltd.shopcart.cloud.newbee.dto.NewBeeMallShoppingCartItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "newbee-mall-cloud-shop-cart-service", path = "/shop-cart")
public interface NewBeeCloudShopCartServiceFeign {

    @GetMapping(value = "/listByCartItemIds")
    Result<List<NewBeeMallShoppingCartItemDTO>> listByCartItemIds(@RequestParam(value = "cartItemIds") List<Long> cartItemIds);

    @DeleteMapping(value = "/deleteByCartItemIds")
    Result<Boolean> deleteByCartItemIds(@RequestParam(value = "cartItemIds") List<Long> cartItemIds);
}
