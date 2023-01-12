package ltd.recommend.cloud.newbee.entity;

import lombok.Data;

@Data
public class LoginMallUser {
    private Long userId;

    private String nickName;

    private String loginName;

    private String introduceSign;
}
