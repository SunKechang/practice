
package ltd.goods.cloud.newbee.service;

import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.PageResult;
import ltd.goods.cloud.newbee.entity.Good;
import ltd.goods.cloud.newbee.entity.NewBeeMallGoods;
import ltd.goods.cloud.newbee.entity.StockNumDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewBeeMallGoodsService {
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
    String saveNewBeeMallGoods(NewBeeMallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param newBeeMallGoodsList
     * @return
     */
    void batchSaveNewBeeMallGoods(List<NewBeeMallGoods> newBeeMallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateNewBeeMallGoods(NewBeeMallGoods goods);

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
    NewBeeMallGoods getNewBeeMallGoodsById(Long id);

    /**
     * 获取商品数据
     *
     * @param goodsIds
     * @return
     */
    List<NewBeeMallGoods> getNewBeeMallGoodsByIds(List<Long> goodsIds);

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
