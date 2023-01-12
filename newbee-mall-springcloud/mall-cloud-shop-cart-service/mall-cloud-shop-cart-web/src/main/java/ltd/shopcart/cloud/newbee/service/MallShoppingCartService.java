
package ltd.shopcart.cloud.newbee.service;

import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.PageResult;
import ltd.shopcart.cloud.newbee.controller.param.SaveCartItemParam;
import ltd.shopcart.cloud.newbee.controller.param.UpdateCartItemParam;
import ltd.shopcart.cloud.newbee.controller.vo.MallShoppingCartItemVO;
import ltd.shopcart.cloud.newbee.entity.MallShoppingCartItem;

import java.util.List;

public interface MallShoppingCartService {

    /**
     * 保存商品至购物车中
     *
     * @param saveCartItemParam
     * @param userId
     * @return
     */
    String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId);

    /**
     * 修改购物车中的属性
     *
     * @param updateCartItemParam
     * @param userId
     * @return
     */
    String updateNewBeeMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId);

    /**
     * 获取购物项详情
     *
     * @param newBeeMallShoppingCartItemId
     * @return
     */
    MallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId);

    /**
     * 删除购物车中的商品
     *
     * @param shoppingCartItemId
     * @param userId
     * @return
     */
    Boolean deleteById(Long shoppingCartItemId, Long userId);

    /**
     * 获取我的购物车中的列表数据
     *
     * @param newBeeMallUserId
     * @return
     */
    List<MallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId);

    /**
     * 根据userId和cartItemIds获取对应的购物项记录
     *
     * @param cartItemIds
     * @param newBeeMallUserId
     * @return
     */
    List<MallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long newBeeMallUserId);


    /**
     * 根据cartItemIds获取对应的购物项记录
     *
     * @param cartItemIds
     * @return
     */
    List<MallShoppingCartItem> getCartItemsByCartIds(List<Long> cartItemIds);

    /**
     * 批量删除购物项记录
     *
     * @param cartItemIds
     * @return
     */
    int deleteCartItemsByCartIds(List<Long> cartItemIds);

    /**
     * 我的购物车(分页数据)
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyShoppingCartItems(PageQueryUtil pageUtil);
}
