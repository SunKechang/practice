
package ltd.goods.cloud.newbee.dao;

import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.goods.cloud.newbee.entity.Good;
import ltd.goods.cloud.newbee.entity.NewBeeMallGoods;
import ltd.goods.cloud.newbee.entity.StockNumDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewBeeMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(NewBeeMallGoods record);

    int insertSelective(NewBeeMallGoods record);

    NewBeeMallGoods selectByPrimaryKey(Long goodsId);

    NewBeeMallGoods selectByCategoryIdAndName(@Param("goodsName") String goodsName, @Param("goodsCategoryId") Long goodsCategoryId);

    int updateByPrimaryKeySelective(NewBeeMallGoods record);

    int updateByPrimaryKeyWithBLOBs(NewBeeMallGoods record);

    int updateByPrimaryKey(NewBeeMallGoods record);

    List<NewBeeMallGoods> findNewBeeMallGoodsList(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoods(PageQueryUtil pageUtil);

    List<NewBeeMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<NewBeeMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("newBeeMallGoodsList") List<NewBeeMallGoods> newBeeMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

    List<Good> listNewGoods(@Param("num") int num);

    List<Good> listSingleGoods(@Param("categoryId")Long categoryId, @Param("num") int num);

}