package ltd.recommend.cloud.newbee.service;

import ltd.recommend.cloud.newbee.controller.vo.MallIndexConfigGoodsVO;
import ltd.recommend.cloud.newbee.entity.Browse;

import java.util.List;

public interface MallBrowseService {

    public void addBrowse(Browse browse);

    public List<MallIndexConfigGoodsVO> getRecommendGoodsByUserId(Long userId);

}
