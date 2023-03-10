
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
        //?????????????????????
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
        //?????????????????????
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
            //???????????? ??????????????????vo
            orderListVOS = BeanUtil.copyList(mallOrders, MallOrderListVO.class);
            //?????????????????????????????????
            for (MallOrderListVO mallOrderListVO : orderListVOS) {
                mallOrderListVO.setOrderStatusString(NewBeeMallOrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(mallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = mallOrders.stream().map(MallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<MallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(MallOrderItem::getOrderId));
                for (MallOrderListVO mallOrderListVO : orderListVOS) {
                    //????????????????????????????????????????????????
                    if (itemByOrderIdMap.containsKey(mallOrderListVO.getOrderId())) {
                        List<MallOrderItem> orderItemListTemp = itemByOrderIdMap.get(mallOrderListVO.getOrderId());
                        //???NewBeeMallOrderItem?????????????????????NewBeeMallOrderItemVO????????????
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
            //?????????????????????userId???????????????????????????
            if (!userId.equals(mallOrder.getUserId())) {
                NewBeeMallException.fail(ServiceResultEnum.NO_PERMISSION_ERROR.getResult());
            }
            //??????????????????
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
            //?????????????????????userId???????????????????????????
            if (!userId.equals(mallOrder.getUserId())) {
                return ServiceResultEnum.NO_PERMISSION_ERROR.getResult();
            }
            //?????????????????? ???????????????????????????????????????
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
            //?????????????????? ??????????????????????????????????????????
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
        //?????????????????????feign????????????
        Result<List<NewBeeMallShoppingCartItemDTO>> cartItemDTOListResult = shopCartService.listByCartItemIds(cartItemIds);
        if (cartItemDTOListResult == null || cartItemDTOListResult.getResultCode() != 200) {
            NewBeeMallException.fail("????????????");
        }
        List<NewBeeMallShoppingCartItemDTO> itemsForSave = cartItemDTOListResult.getData();
        if (CollectionUtils.isEmpty(itemsForSave)) {
            //?????????
            NewBeeMallException.fail("????????????");
        }
        List<Long> itemIdList = itemsForSave.stream().map(NewBeeMallShoppingCartItemDTO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = itemsForSave.stream().map(NewBeeMallShoppingCartItemDTO::getGoodsId).collect(Collectors.toList());
        //??????????????????feign????????????
        Result<List<NewBeeMallGoodsDTO>> goodsDTOListResult = goodsService.listByGoodsIds(goodsIds);
        if (goodsDTOListResult == null || goodsDTOListResult.getResultCode() != 200) {
            NewBeeMallException.fail("????????????");
        }
        List<NewBeeMallGoodsDTO> newBeeMallGoods = goodsDTOListResult.getData();
        //?????????????????????????????????
        List<NewBeeMallGoodsDTO> goodsListNotSelling = newBeeMallGoods.stream()
                .filter(newBeeMallGoodsTemp -> newBeeMallGoodsTemp.getGoodsSellStatus() != 0)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
            //goodsListNotSelling ????????????????????????????????????
            NewBeeMallException.fail(goodsListNotSelling.get(0).getGoodsName() + "??????????????????????????????");
        }
        Map<Long, NewBeeMallGoodsDTO> newBeeMallGoodsMap = newBeeMallGoods.stream().collect(Collectors.toMap(NewBeeMallGoodsDTO::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //??????????????????
        for (NewBeeMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
            //?????????????????????????????????????????????????????????????????????????????????????????????
            if (!newBeeMallGoodsMap.containsKey(cartItemDTO.getGoodsId())) {
                NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //????????????????????????????????????????????????????????????
            if (cartItemDTO.getGoodsCount() > newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getStockNum()) {
                NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //???????????????
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(newBeeMallGoods)) {

            //?????????????????????feign????????????
            Result<Boolean> deleteByCartItemIdsResult = shopCartService.deleteByCartItemIds(itemIdList);
            if (deleteByCartItemIdsResult != null && deleteByCartItemIdsResult.getResultCode() == 200) {


                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(itemsForSave, StockNumDTO.class);
                UpdateStockNumDTO updateStockNumDTO = new UpdateStockNumDTO();
                updateStockNumDTO.setStockNumDTOS(stockNumDTOS);

                //??????????????????feign??????????????????
                Result<Boolean> updateStockResult = goodsService.updateStock(updateStockNumDTO);
                if (updateStockResult == null || updateStockResult.getResultCode() != 200) {
                    NewBeeMallException.fail(ServiceResultEnum.PARAM_ERROR.getResult());
                }
                if (!updateStockResult.getData()) {
                    NewBeeMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //???????????????
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //????????????
                MallOrder mallOrder = new MallOrder();
                mallOrder.setOrderNo(orderNo);
                mallOrder.setUserId(mallUserId);
                //??????
                for (NewBeeMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
                    priceTotal += cartItemDTO.getGoodsCount() * newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getSellingPrice();
                }
                if (priceTotal < 1) {
                    NewBeeMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                mallOrder.setTotalPrice(priceTotal);
                String extraInfo = "";
                mallOrder.setExtraInfo(extraInfo);
                //???????????????????????????????????????
                if (mallOrderMapper.insertSelective(mallOrder) > 0) {
                    //??????????????????????????????????????????????????????
                    MallOrderAddress mallOrderAddress = new MallOrderAddress();
                    BeanUtil.copyProperties(address, mallOrderAddress);
                    mallOrderAddress.setOrderId(mallOrder.getOrderId());
                    //??????????????????????????????????????????????????????
                    List<MallOrderItem> mallOrderItems = new ArrayList<>();
                    for (NewBeeMallShoppingCartItemDTO cartItemDTO : itemsForSave) {
                        MallOrderItem mallOrderItem = new MallOrderItem();
                        //??????BeanUtil????????????cartItemDTO?????????????????????newBeeMallOrderItem?????????
                        BeanUtil.copyProperties(cartItemDTO, mallOrderItem);
                        mallOrderItem.setGoodsCoverImg(newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getGoodsCoverImg());
                        mallOrderItem.setGoodsName(newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getGoodsName());
                        mallOrderItem.setSellingPrice(newBeeMallGoodsMap.get(cartItemDTO.getGoodsId()).getSellingPrice());
                        //NewBeeMallOrderMapper??????insert()??????????????????useGeneratedKeys??????orderId???????????????
                        mallOrderItem.setOrderId(mallOrder.getOrderId());
                        mallOrderItems.add(mallOrderItem);
                    }
                    //??????????????????
                    if (mallOrderItemMapper.insertBatch(mallOrderItems) > 0 && mallOrderAddressMapper.insertSelective(mallOrderAddress) > 0) {
                        //???????????????????????????????????????????????????Controller???????????????????????????
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
        //????????????orderStatus>=0????????????????????????????????????????????????
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
        //????????????????????? ???????????? ???????????????????????????
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
                //?????????????????? ?????????????????????????????? ?????????????????????????????????
                if (mallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //????????????????????????????????????
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "?????????????????????????????????????????????????????????";
                } else {
                    return "????????????????????????????????????????????????????????????????????????????????????";
                }
            }
        }
        //?????????????????? ??????????????????
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //????????????????????? ???????????? ???????????????????????????
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
                //?????????????????? ???????????????????????? ?????????????????????????????????
                if (mallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //????????????????????????????????????
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "????????????????????????????????????????????????????????????????????????";
                } else {
                    return "?????????????????????????????????????????????????????????????????????????????????????????????";
                }
            }
        }
        //?????????????????? ??????????????????
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //????????????????????? ???????????? ???????????????????????????
        List<MallOrder> orders = mallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (MallOrder mallOrder : orders) {
                // isDeleted=1 ????????????????????????
                if (mallOrder.getIsDeleted() == 1) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                    continue;
                }
                //??????????????????????????????????????????
                if (mallOrder.getOrderStatus() == 4 || mallOrder.getOrderStatus() < 0) {
                    errorOrderNos += mallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //?????????????????? ???????????????????????? ?????????????????????????????????
                if (mallOrderMapper.closeOrder(Arrays.asList(ids), NewBeeMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //????????????????????????????????????
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "??????????????????????????????";
                } else {
                    return "??????????????????????????????????????????";
                }
            }
        }
        //?????????????????? ??????????????????
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    public List<MallOrderItemVO> getOrderItems(Long orderId) {
        MallOrder mallOrder = mallOrderMapper.selectByPrimaryKey(orderId);
        if (mallOrder != null) {
            List<MallOrderItem> orderItems = mallOrderItemMapper.selectByOrderId(mallOrder.getOrderId());
            //?????????????????????
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<MallOrderItemVO> mallOrderItemVOS = BeanUtil.copyList(orderItems, MallOrderItemVO.class);
                return mallOrderItemVOS;
            }
        }
        return null;
    }
}
