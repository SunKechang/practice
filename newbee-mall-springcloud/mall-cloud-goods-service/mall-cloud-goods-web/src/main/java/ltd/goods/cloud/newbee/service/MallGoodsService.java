
package ltd.goods.cloud.newbee.service;

import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.PageResult;
import ltd.goods.cloud.newbee.entity.Good;
import ltd.goods.cloud.newbee.entity.MallGoods;
import ltd.goods.cloud.newbee.entity.StockNumDTO;

import java.util.List;

public interface MallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveNewBeeMallGoods(MallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param mallGoodsList
     * @return
     */
    void batchSaveNewBeeMallGoods(List<MallGoods> mallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateNewBeeMallGoods(MallGoods goods);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    MallGoods getNewBeeMallGoodsById(Long id);

    /**
     * 获取商品数据
     *
     * @param goodsIds
     * @return
     */
    List<MallGoods> getNewBeeMallGoodsByIds(List<Long> goodsIds);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil);

    Boolean updateStockNum(List<StockNumDTO> stockNumDTOS);

    List<Good> listNewGoods(Integer num);

    List<Good> listSingleCateGoods(long category, int num);

    void insertBrowse(long userId, long categoryId);
}
