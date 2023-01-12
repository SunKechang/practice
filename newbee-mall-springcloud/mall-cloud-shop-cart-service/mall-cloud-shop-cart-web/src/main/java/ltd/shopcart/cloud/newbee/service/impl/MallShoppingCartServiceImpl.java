/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2022 程序员十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.shopcart.cloud.newbee.service.impl;

import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.PageResult;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.exception.NewBeeMallException;
import ltd.common.cloud.newbee.util.BeanUtil;
import ltd.goods.cloud.newbee.dto.NewBeeMallGoodsDTO;
import ltd.goods.cloud.newbee.openfeign.NewBeeCloudGoodsServiceFeign;
import ltd.shopcart.cloud.newbee.controller.param.SaveCartItemParam;
import ltd.shopcart.cloud.newbee.controller.param.UpdateCartItemParam;
import ltd.shopcart.cloud.newbee.controller.vo.MallShoppingCartItemVO;
import ltd.shopcart.cloud.newbee.dao.MallShoppingCartItemMapper;
import ltd.shopcart.cloud.newbee.entity.MallShoppingCartItem;
import ltd.shopcart.cloud.newbee.service.MallShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MallShoppingCartServiceImpl implements MallShoppingCartService {

    @Autowired
    private MallShoppingCartItemMapper mallShoppingCartItemMapper;

    @Autowired
    private NewBeeCloudGoodsServiceFeign goodsService;

    @Override
    public String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        MallShoppingCartItem temp = mallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            NewBeeMallException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }
        Result<NewBeeMallGoodsDTO> goodsDetailResult = goodsService.getGoodsDetail(saveCartItemParam.getGoodsId());
        //商品为空
        if (goodsDetailResult == null || goodsDetailResult.getResultCode() != 200) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = mallShoppingCartItemMapper.selectCountByUserId(userId);
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() > 5) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > 20) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        MallShoppingCartItem mallShoppingCartItem = new MallShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, mallShoppingCartItem);
        mallShoppingCartItem.setUserId(userId);
        //保存记录
        if (mallShoppingCartItemMapper.insertSelective(mallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNewBeeMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        MallShoppingCartItem mallShoppingCartItemUpdate = mallShoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
        if (mallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!mallShoppingCartItemUpdate.getUserId().equals(userId)) {
            NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        //超出单个商品的最大数量
        if (updateCartItemParam.getGoodsCount() > 5) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //当前登录账号的userId与待修改的cartItem中userId不同，返回错误
        if (!mallShoppingCartItemUpdate.getUserId().equals(userId)) {
            return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
        }
        //数值相同，则不执行数据操作
        if (updateCartItemParam.getGoodsCount().equals(mallShoppingCartItemUpdate.getGoodsCount())) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        mallShoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        mallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (mallShoppingCartItemMapper.updateByPrimaryKeySelective(mallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public MallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId) {
        MallShoppingCartItem mallShoppingCartItem = mallShoppingCartItemMapper.selectByPrimaryKey(newBeeMallShoppingCartItemId);
        if (mallShoppingCartItem == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return mallShoppingCartItem;
    }

    @Override
    public Boolean deleteById(Long shoppingCartItemId, Long userId) {
        MallShoppingCartItem mallShoppingCartItem = mallShoppingCartItemMapper.selectByPrimaryKey(shoppingCartItemId);
        if (mallShoppingCartItem == null) {
            return false;
        }
        //userId不同不能删除
        if (!userId.equals(mallShoppingCartItem.getUserId())) {
            return false;
        }
        return mallShoppingCartItemMapper.deleteByPrimaryKey(shoppingCartItemId) > 0;
    }

    @Override
    public List<MallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId) {
        List<MallShoppingCartItemVO> mallShoppingCartItemVOS = new ArrayList<>();
        List<MallShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectByUserId(newBeeMallUserId, 20);
        return getNewBeeMallShoppingCartItemVOS(mallShoppingCartItemVOS, mallShoppingCartItems);
    }

    @Override
    public List<MallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long newBeeMallUserId) {
        List<MallShoppingCartItemVO> mallShoppingCartItemVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            NewBeeMallException.fail("购物项不能为空");
        }
        List<MallShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.selectByUserIdAndCartItemIds(newBeeMallUserId, cartItemIds);
        if (CollectionUtils.isEmpty(mallShoppingCartItems)) {
            NewBeeMallException.fail("购物项不能为空");
        }
        if (mallShoppingCartItems.size() != cartItemIds.size()) {
            NewBeeMallException.fail("参数异常");
        }
        return getNewBeeMallShoppingCartItemVOS(mallShoppingCartItemVOS, mallShoppingCartItems);
    }

    @Override
    public List<MallShoppingCartItem> getCartItemsByCartIds(List<Long> cartItemIds) {
        return mallShoppingCartItemMapper.selectByCartItemIds(cartItemIds);
    }

    @Override
    public int deleteCartItemsByCartIds(List<Long> cartItemIds) {
        return mallShoppingCartItemMapper.deleteBatch(cartItemIds);
    }

    /**
     * 数据转换
     *
     * @param mallShoppingCartItemVOS
     * @param mallShoppingCartItems
     * @return
     */
    private List<MallShoppingCartItemVO> getNewBeeMallShoppingCartItemVOS(List<MallShoppingCartItemVO> mallShoppingCartItemVOS, List<MallShoppingCartItem> mallShoppingCartItems) {
        if (!CollectionUtils.isEmpty(mallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> newBeeMallGoodsIds = mallShoppingCartItems.stream().map(MallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            Result<List<NewBeeMallGoodsDTO>> newBeeMallGoodsDTOResult = goodsService.listByGoodsIds(newBeeMallGoodsIds);
            //商品为空
            if (newBeeMallGoodsDTOResult == null || newBeeMallGoodsDTOResult.getResultCode() != 200) {
                NewBeeMallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
            }
            Map<Long, NewBeeMallGoodsDTO> newBeeMallGoodsDTOMap = new HashMap<>();
            List<NewBeeMallGoodsDTO> newBeeMallGoodsDTOS = newBeeMallGoodsDTOResult.getData();
            if (!CollectionUtils.isEmpty(newBeeMallGoodsDTOS)) {
                newBeeMallGoodsDTOMap = newBeeMallGoodsDTOS.stream().collect(Collectors.toMap(NewBeeMallGoodsDTO::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (MallShoppingCartItem mallShoppingCartItem : mallShoppingCartItems) {
                MallShoppingCartItemVO mallShoppingCartItemVO = new MallShoppingCartItemVO();
                BeanUtil.copyProperties(mallShoppingCartItem, mallShoppingCartItemVO);
                if (newBeeMallGoodsDTOMap.containsKey(mallShoppingCartItem.getGoodsId())) {
                    NewBeeMallGoodsDTO newBeeMallGoodsDtoTemp = newBeeMallGoodsDTOMap.get(mallShoppingCartItem.getGoodsId());
                    mallShoppingCartItemVO.setGoodsCoverImg(newBeeMallGoodsDtoTemp.getGoodsCoverImg());
                    String goodsName = newBeeMallGoodsDtoTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    mallShoppingCartItemVO.setGoodsName(goodsName);
                    mallShoppingCartItemVO.setSellingPrice(newBeeMallGoodsDtoTemp.getSellingPrice());
                    mallShoppingCartItemVOS.add(mallShoppingCartItemVO);
                }
            }
        }
        return mallShoppingCartItemVOS;
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
        List<MallShoppingCartItemVO> mallShoppingCartItemVOS = new ArrayList<>();
        List<MallShoppingCartItem> mallShoppingCartItems = mallShoppingCartItemMapper.findMyNewBeeMallCartItems(pageUtil);
        int total = mallShoppingCartItemMapper.getTotalMyNewBeeMallCartItems(pageUtil);
        PageResult pageResult = new PageResult(getNewBeeMallShoppingCartItemVOS(mallShoppingCartItemVOS, mallShoppingCartItems), total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
