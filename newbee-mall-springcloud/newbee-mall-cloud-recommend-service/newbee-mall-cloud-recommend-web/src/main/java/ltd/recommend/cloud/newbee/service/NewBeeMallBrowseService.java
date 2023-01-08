package ltd.recommend.cloud.newbee.service;

import ltd.recommend.cloud.newbee.controller.vo.NewBeeMallIndexConfigGoodsVO;
import ltd.recommend.cloud.newbee.entity.Browse;
import ltd.recommend.cloud.newbee.entity.LoginMallUser;

import java.util.List;

public interface NewBeeMallBrowseService {

    public void addBrowse(Browse browse);

    public List<NewBeeMallIndexConfigGoodsVO> getRecommendGoodsByUserId(Long userId);

}
