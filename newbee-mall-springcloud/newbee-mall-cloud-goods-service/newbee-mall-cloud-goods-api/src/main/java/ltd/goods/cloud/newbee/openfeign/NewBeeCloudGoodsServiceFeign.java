
package ltd.goods.cloud.newbee.openfeign;

import ltd.common.cloud.newbee.dto.Result;
import ltd.goods.cloud.newbee.dto.GoodDTO;
import ltd.goods.cloud.newbee.dto.NewBeeMallGoodsDTO;
import ltd.goods.cloud.newbee.dto.UpdateStockNumDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "newbee-mall-cloud-goods-service", path = "/goods")
public interface NewBeeCloudGoodsServiceFeign {

    @GetMapping(value = "/admin/goodsDetail")
    Result<NewBeeMallGoodsDTO> getGoodsDetail(@RequestParam(value = "goodsId") Long goodsId);

    @GetMapping(value = "/admin/listByGoodsIds")
    Result<List<NewBeeMallGoodsDTO>> listByGoodsIds(@RequestParam(value = "goodsIds") List<Long> goodsIds);

    @PutMapping(value = "/admin/updateStock")
    Result<Boolean> updateStock(@RequestBody UpdateStockNumDTO updateStockNumDTO);

    @GetMapping(value = "/mall/listNewGoods")
    Result<List<GoodDTO>> listNewGoods(@RequestParam(value = "num") Integer num);

    @GetMapping(value = "/mall/listSingleCateGoods")
    Result<List<GoodDTO>> listSingleCateGoods(@RequestParam(value = "category") Long category,
                                              @RequestParam(value = "num") Integer num);
}
