package ltd.recommend.cloud.newbee.openfeign;

import ltd.common.cloud.newbee.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "newbee-mall-cloud-recommend-service", path = "/mall")
public interface CloudRecommendServiceFeign {

    @PostMapping(value = "/index/addBrowse")
    void addBrowse(@RequestParam(value = "userId") Long userId, @RequestParam(value = "categoryId") Long categoryId);
}
