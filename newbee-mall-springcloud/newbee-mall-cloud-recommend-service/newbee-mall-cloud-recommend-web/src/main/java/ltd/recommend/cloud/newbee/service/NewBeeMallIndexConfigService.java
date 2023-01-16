
package ltd.recommend.cloud.newbee.service;

import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.PageResult;
import ltd.recommend.cloud.newbee.controller.vo.NewBeeMallIndexConfigGoodsVO;
import ltd.recommend.cloud.newbee.entity.IndexConfig;
import ltd.recommend.cloud.newbee.entity.LoginMallUser;

import java.util.List;

public interface NewBeeMallIndexConfigService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    Boolean deleteBatch(Long[] ids);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     *
     * @param number
     * @return
     */
    List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    List<NewBeeMallIndexConfigGoodsVO> listNewGoods(int num);
}
