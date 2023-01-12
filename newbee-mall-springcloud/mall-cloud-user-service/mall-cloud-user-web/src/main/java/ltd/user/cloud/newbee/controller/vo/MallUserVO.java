package ltd.user.cloud.newbee.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MallUserVO implements Serializable {

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户登录名")
    private String loginName;

    @ApiModelProperty("个性签名")
    private String introduceSign;

    /**
     *
     * 2023-1-9
     * 李梦林 修改
      */
    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("联系电话")
    private String telephone;

    @ApiModelProperty("邮箱")
    private String mail;
}
