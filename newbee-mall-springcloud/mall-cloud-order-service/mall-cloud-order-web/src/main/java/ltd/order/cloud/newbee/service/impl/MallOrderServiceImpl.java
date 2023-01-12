/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2022 程序员十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.order.cloud.newbee.service.impl;

import io.seata.spring.annotation.GlobalTransactional;
import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.PageResult;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.enums.NewBeeMallOrderStatusEnum;
import ltd.common.cloud.newbee.enums.PayStatusEnum;
import ltd.common.cloud.newbee.enums.PayTypeEnum;
import ltd.common.cloud.newbee.exception.NewBeeMallException;
import ltd.common.cloud.newbee.util.BeanUtil;
import ltd.common.cloud.newbee.util.NumberUtil;
import ltd.goods.cloud.newbee.dto.NewBeeMallGoodsDTO;
import ltd.goods.cloud.newbee.dto.StockNumDTO;
import ltd.goods.cloud.newbee.dto.UpdateStockNumDTO;
import ltd.goods.cloud.newbee.openfeign.NewBeeCloudGoodsServiceFeign;
import ltd.order.cloud.newbee.controller.vo.MallOrderDetailVO;
import ltd.order.cloud.newbee.controller.vo.MallOrderItemVO;
import ltd.order.cloud.newbee.controller.vo.MallOrderListVO;
import ltd.order.cloud.newbee.dao.MallOrderAddressMapper;
import ltd.order.cloud.newbee.dao.MallOrderItemMapper;
import ltd.order.cloud.newbee.dao.MallOrderMapper;
import ltd.order.cloud.newbee.entity.MallUserAddress;
import ltd.order.cloud.newbee.entity.MallOrder;
import ltd.order.cloud.newbee.entity.MallOrderAddress;
import ltd.order.cloud.newbee.entity.MallOrderItem;
import ltd.order.cloud.newbee.service.MallOrderService;
import ltd.shopcart.cloud.newbee.dto.NewBeeMallShoppingCartItemDTO;
import ltd.shopcart.cloud.newbee.openfeign.NewBeeCloudShopCartServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class MallOrderServiceImpl implements MallOrderService {

    @Autowired
    private MallOrderMapper mallOrderMapper;

    @Autowired
    private MallOrderItemMapper mallOrderItemMapper;

    @Autowired
    private MallOrderAddressMapper mallOrderAddressMapper;

    @Autowired
    private NewBeeCloudGoodsServiceFeign goodsService;

    @Autowired
    private NewBeeCloudShopCartServiceFeign shopCartService;

    @Override
    public MallOrderDetailVO getOrderDetailByOrderId(Long orderId) {
        MallOrder mallOrder = mallOrderMapper.selectByPrimaryKey(orderId);
        if (mallOrder == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderId(mallOrder.getOrderId());
        //获取订单项数据
        if (!CollectionUtils.isEmpty(orderItems)) {
            List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
            MallOrderDetailVO mallOrderDetailVO = new MallOrderDetailVO();
            BeanUtil.copyProperties(mallOrder, mallOrderDetailVO);
            mallOrderDetailVO.setOrderStatusString(NewBeeMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(mallOrderDetailVO.getOrderStatus()).getName());
            mallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrderDetailVO.getPayType()).getName());
            mallOrderDetailVO.setMallOrderItemVOS(mallOrderItemVOS);
            return mallOrderDetailVO;
        } else {
            NewBeeMallException.fail(ServiceResultEnum.ORDER_ITEM_NULL_ERROR.getResult());
            return null;
        }
    }

    @Override
    public MallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        MallOrder mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!userId.equals(mallOrder.getUserId())) {
            NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderId(mallOrder.getOrderId());
        //获取订单项数据
        if (CollectionUtils.isEmpty(orderItems)) {
            NewBeeMallException.fail(ServiceResultEnum.ORDER_ITEM_NOT_EXIST_ERROR.getResult());
        }
        List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
        MallOrderDetailVO mallOrderDetailVO = new MallOrderDetailVO();
        BeanUtil.copyProperties(mallOrder, mallOrderDetailVO);
        mallOrderDetailVO.setOrderStatusString(NewBeeMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(mallOrderDetailVO.getOrderStatus()).getName());
        mallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(mallOrderDetailVO.getPayType()).getName());
        mallOrderDetailVO.setMallOrderItemVOS(mallOrderItemVOS);
        return mallOrderDetailVO;
    }


    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = mallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
        List<MallOrder> mallOrders = mallOrderMapper.findNewBeeMallOrderList(pageUtil);
        List<MallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(mallOrders, MallOrderListVO.class);
            //设置订单状态中文显示值
            for (MallOrderListVO mallOrderListVO : orderListVOS) {
                mallOrderListVO.setOrderStatusString(NewBeeMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(mallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = mallOrders.stream().map(MallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<MallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(MallOrderItem::getOrderId));
                for (MallOrderListVO mallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(mallOrderListVO.getOrderId())) {
                        List<MallOrderItem> orderItemListTemp = itemByOrderIdMap.get(mallOrderListVO.getOrderId());
                        //将NewBeeMallOrderItem对象列表转换成NewBeeMallOrderItemVO对象列表
                        List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, MallOrderItemVO.class);
                        mallOrderListVO.setMallOrderItemVOS(mallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        MallOrder mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(mallOrder.getUserId())) {
                NewBeeMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //订单状态判断
            if (mallOrder.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus()
                    || mallOrder.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()
                    || mallOrder.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_EXPIRED.getOrderStatus()
                    || mallOrder.getOrderStatus().intValue() == NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            if (mallOrderMapper.closeOrder(Collections.singletonList(mallOrder.getOrderId()), NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        MallOrder mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (!userId.equals(mallOrder.getUserId())) {
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            //订单状态判断 非出库状态下不进行修改操作
            if (mallOrder.getOrderStatus().intValue() != NewBeeMallOrderStatusEnum.ORDER_EXPRESS.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            mallOrder.setOrderStatus((byte) NewBeeMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        MallOrder mallOrder = mallOrderMapper.selectByOrderNo(orderNo);
        if (mallOrder != null) {
            //订单状态判断 非待支付状态下不进行修改操作
            if (mallOrder.getOrderStatus().intValue() != NewBeeMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()) {
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            mallOrder.setOrderStatus((byte) NewBeeMallOrderStatusEnum.ORDER_PAID.getOrderStatus());
            mallOrder.setPayType((byte) payType);
            mallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            mallOrder.setPayTime(new Date());
            mallOrder.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(mallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    @Transactional
    @GlobalTransactional
    public String saveOrder(Long mallUserId, MallUserAddress address, List<Long> cartItemIds) {
        //调用购物车服务feign获取数据
        Result<List<NewBeeMallShoppingCartItemDTO>> cartItemDTOListResult = shopCartService.listByCartItemIds(cartItemIds);
        if (cartItemDTOListResult == null || cartItemDTOListResult.getResultCode() != 200) {
            NewBeeMallException.fail("参数异常");
        }
        List<NewBeeMallShoppingCartItemDTO> itemsForSave = cartItemDTOListResult.getData();
        if (CollectionUtils.isEmpty(itemsForSave)) {
            //无数据
            NewBeeMallException.fail("参数异常");
        }
        List<Long> itemIdList = itemsForSave.stream().map(NewBeeMallShoppingCartItemDTO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = itemsForSave.stream().map(NewBeeMallShoppingCartItemDTO::getGoodsId).collect(Collectors.toList());
        //调用商品服务feign获取数据
        Result<List<NewBeeMallGoodsDTO>> goodsDTOListResult = goodsService.listByGoodsIds(goodsIds);
        if (goodsDTOListResult == null || goodsDTOListResult.getResultCode() != 200) {
            NewBeeMallException.fail("参数异常");
        }
        List<NewBeeMallGoodsDTO> newBeeMallGoods = goodsDTOListResult.getData();
        //检查是否包含已下架商品
        List<NewBeeMallGoodsDTO> goodsListNotSelling = newBeeMallGoods.stream()
                .filter(newBeeMallGoodsTemp -> newBeeMallGoodsTemp.getGoodsSellStatus() != 0)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling 对象非空则表示有下架商品
            NewBeeMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
        }
        Map<Long, NewBeeMallGoodsDTO> newBeeMallGoodsMap = newBeeMallGoods.stream().collect(Collectors.toMap(NewBeeMallGoodsDTO::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //判断商品库存
        for (NewBeeMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
            //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
            if (!newBeeMallGoodsMap.containsKey(cartItemDTO.getGoodsId())) {
                NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //存在数量大于库存的情况，直接返回错误提醒
            if (cartItemDTO.getGoodsCount() > newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getStockNum()) {
                NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //删除购物项
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(newBeeMallGoods)) {

            //调用购物车服务feign删除数据
            Result<Boolean> deleteByCartItemIdsResult = shopCartService.deleteByCartItemIds(itemIdList);
            if (deleteByCartItemIdsResult != null && deleteByCartItemIdsResult.getResultCode() == 200) {


                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(itemsForSave, StockNumDTO.class);
                UpdateStockNumDTO updateStockNumDTO = new UpdateStockNumDTO();
                updateStockNumDTO.setStockNumDTOS(stockNumDTOS);

                //调用商品服务feign修改库存数据
                Result<Boolean> updateStockResult = goodsService.updateStock(updateStockNumDTO);
                if (updateStockResult == null || updateStockResult.getResultCode() != 200) {
                    NewBeeMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
                }
                if (!updateStockResult.getData()) {
                    NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //生成订单号
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //保存订单
                MallOrder mallOrder = new MallOrder();
                mallOrder.setOrderNo(orderNo);
                mallOrder.setUserId(mallUserId);
                //总价
                for (NewBeeMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
                    priceTotal += cartItemDTO.getGoodsCount() * newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getSellingPrice();
                }
                if (priceTotal < 1) {
                    NewBeeMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                mallOrder.setTotalPrice(priceTotal);
                String extraInfo = "";
                mallOrder.setExtraInfo(extraInfo);
                //生成订单项并保存订单项纪录
                if (mallOrderMapper.insertSelective(mallOrder) > 0) {
                    //生成订单收货地址快照，并保存至数据库
                    MallOrderAddress mallOrderAddress = new MallOrderAddress();
                    BeanUtil.copyProperties(address, mallOrderAddress);
                    mallOrderAddress.setOrderId(mallOrder.getOrderId());
                    //生成所有的订单项快照，并保存至数据库
                    List<MallOrderItem> mallOrderItems = new ArrayList<>();
                    for (NewBeeMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
                        MallOrderItem mallOrderItem = new MallOrderItem();
                        //使用BeanUtil工具类将cartItemDTO中的属性复制到newBeeMallOrderItem对象中
                        BeanUtil.copyProperties(cartItemDTO, mallOrderItem);
                        mallOrderItem.setGoodsCoverImg(newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getGoodsCoverImg());
                        mallOrderItem.setGoodsName(newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getGoodsName());
                        mallOrderItem.setSellingPrice(newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getSellingPrice());
                        //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
                        mallOrderItem.setOrderId(mallOrder.getOrderId());
                        mallOrderItems.add(mallOrderItem);
                    }
                    //保存至数据库
                    if (mallOrderItemMapper.insertBatch(mallOrderItems) > 0 && mallOrderAddressMapper.insertSelective(mallOrderAddress) > 0) {
                        //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
                        return orderNo;
                    }
                    NewBeeMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }


    @Override
    public PageResult getNewBeeMallOrdersPage(PageQueryUtil pageUtil) {
        List<MallOrder> mallOrders = mallOrderMapper.findNewBeeMallOrderList(pageUtil);
        int total = mallOrderMapper.getTotalNewBeeMallOrders(pageUtil);
        PageResult pageResult = new PageResult(mallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(MallOrder mallOrder) {
        MallOrder temp = mallOrderMapper.selectByPrimaryKey(mallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(mallOrder.getTotalPrice());
            temp.setUpdateTime(new Date());
            if (mallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<MallOrder> orders = mallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (MallOrder mallOrder : orders) {
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                if (mallOrder.getOrderStatus() != 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (mallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<MallOrder> orders = mallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (MallOrder mallOrder : orders) {
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                if (mallOrder.getOrderStatus() != 1 && mallOrder.getOrderStatus() != 2) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (mallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<MallOrder> orders = mallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (MallOrder mallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (mallOrder.getOrderStatus() == 4 || mallOrder.getOrderStatus() < 0) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (mallOrderMapper.closeOrder(Arrays.asList(ids), NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public List<MallOrderItemVO> getOrderItems(Long orderId) {
        MallOrder mallOrder = mallOrderMapper.selectByPrimaryKey(orderId);
        if (mallOrder != null) {
            List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderId(mallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
                return mallOrderItemVOS;
            }
        }
        return null;
    }
}
