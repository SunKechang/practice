
package ltd.order.cloud.newbee.controller.param;

import lombok.Data;

import java.io.Serializable;

@Data
public class BatchIdParam implements Serializable {
    //id数组
    Long[] ids;
}
