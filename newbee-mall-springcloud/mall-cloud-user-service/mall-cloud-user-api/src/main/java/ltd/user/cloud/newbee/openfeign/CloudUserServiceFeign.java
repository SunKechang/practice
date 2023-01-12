
package ltd.user.cloud.newbee.openfeign;

import ltd.user.cloud.newbee.dto.MallUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ltd.common.cloud.newbee.dto.Result;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "newbee-mall-cloud-user-service", path = "/users")
public interface CloudUserServiceFeign {

    @GetMapping(value = "/admin/{token}")
    Result getAdminUserByToken(@PathVariable(value = "token") String token);

    @GetMapping(value = "/mall/getDetailByToken")
    Result<MallUserDTO> getMallUserByToken(@RequestParam(value = "token") String token);
}
