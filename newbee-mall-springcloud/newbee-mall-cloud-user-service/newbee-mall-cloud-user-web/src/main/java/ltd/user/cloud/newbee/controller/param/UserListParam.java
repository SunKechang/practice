package ltd.user.cloud.newbee.controller.param;

import lombok.Data;
import ltd.user.cloud.newbee.entity.MallUser;

import java.util.List;

@Data
public class UserListParam {
    private List<MallUser> list;

    private int totalCount;

    private int currPage;
}
