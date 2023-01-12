package ltd.goods.cloud.newbee.entity;

import lombok.Data;

@Data
public class Good {

    private Long goodsId;
    private String goodsName;
    private String goodsIntro;
    private String goodsCoverImg;
    private Integer sellingPrice;
    private String tag;

}
