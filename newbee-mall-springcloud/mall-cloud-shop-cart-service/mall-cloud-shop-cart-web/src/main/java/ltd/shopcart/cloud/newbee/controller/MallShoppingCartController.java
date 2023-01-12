/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2022 程序员十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package ltd.shopcart.cloud.newbee.controller;

import io.seata.core.context.RootContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.PageResult;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.common.cloud.newbee.exception.NewBeeMallException;
import ltd.common.cloud.newbee.pojo.MallUserToken;
import ltd.shopcart.cloud.newbee.config.annotation.TokenToMallUser;
import ltd.shopcart.cloud.newbee.controller.param.SaveCartItemParam;
import ltd.shopcart.cloud.newbee.controller.param.UpdateCartItemParam;
import ltd.shopcart.cloud.newbee.controller.vo.MallShoppingCartItemVO;
import ltd.shopcart.cloud.newbee.entity.MallShoppingCartItem;
import ltd.shopcart.cloud.newbee.service.MallShoppingCartService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "v1", tags = "新蜂商城购物车相关接口")
public class MallShoppingCartController {

    @Resource
    private MallShoppingCartService mallShoppingCartService;

    @GetMapping("/shop-cart/page")
    @ApiOperation(value = "购物车列表(每页默认5条)", notes = "传参为页码")
    public Result<PageResult<List<MallShoppingCartItemVO>>> cartItemPageList(Integer pageNumber, @TokenToMallUser MallUserToken loginMallUserToken) {
        Map params = new HashMap(8);
        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        params.put("userId", loginMallUserToken.getUserId());
        params.put("page", pageNumber);
        params.put("limit", 5);
        //封装分页请求参数
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(mallShoppingCartService.getMyShoppingCartItems(pageUtil));
    }

    @GetMapping("/shop-cart")
    @ApiOperation(value = "购物车列表(网页移动端不分页)", notes = "")
    public Result<List<MallShoppingCartItemVO>> cartItemList(@TokenToMallUser MallUserToken loginMallUserToken) {
        return ResultGenerator.genSuccessResult(mallShoppingCartService.getMyShoppingCartItems(loginMallUserToken.getUserId()));
    }

    @PostMapping("/shop-cart")
    @ApiOperation(value = "添加商品到购物车接口", notes = "传参为商品id、数量")
    public Result saveNewBeeMallShoppingCartItem(@RequestBody SaveCartItemParam saveCartItemParam,
                                                 @TokenToMallUser MallUserToken loginMallUserToken) {
        String saveResult = mallShoppingCartService.saveNewBeeMallCartItem(saveCartItemParam, loginMallUserToken.getUserId());
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ApiOperation(value = "修改购物项数据", notes = "传参为购物项id、数量")
    public Result updateNewBeeMallShoppingCartItem(@RequestBody UpdateCartItemParam updateCartItemParam,
                                                   @TokenToMallUser MallUserToken loginMallUserToken) {
        String updateResult = mallShoppingCartService.updateNewBeeMallCartItem(updateCartItemParam, loginMallUserToken.getUserId());
        //修改成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //修改失败
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
    @ApiOperation(value = "删除购物项", notes = "传参为购物项id")
    public Result updateNewBeeMallShoppingCartItem(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId,
                                                   @TokenToMallUser MallUserToken loginMallUserToken) {
        MallShoppingCartItem newBeeMallCartItemById = mallShoppingCartService.getNewBeeMallCartItemById(newBeeMallShoppingCartItemId);
        if (!loginMallUserToken.getUserId().equals(newBeeMallCartItemById.getUserId())) {
            return ResultGenerator.genFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        Boolean deleteResult = mallShoppingCartService.deleteById(newBeeMallShoppingCartItemId, loginMallUserToken.getUserId());
        //删除成功
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        //删除失败
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    @ApiOperation(value = "根据购物项id数组查询购物项明细", notes = "确认订单页面使用")
    public Result<List<MallShoppingCartItemVO>> toSettle(Long[] cartItemIds, @TokenToMallUser MallUserToken loginMallUserToken) {
        if (cartItemIds.length < 1) {
            NewBeeMallException.fail("参数异常");
        }
        int priceTotal = 0;
        List<MallShoppingCartItemVO> itemsForSettle = mallShoppingCartService.getCartItemsForSettle(Arrays.asList(cartItemIds), loginMallUserToken.getUserId());
        if (CollectionUtils.isEmpty(itemsForSettle)) {
            //无数据则抛出异常
            NewBeeMallException.fail("参数异常");
        } else {
            //总价
            for (MallShoppingCartItemVO mallShoppingCartItemVO : itemsForSettle) {
                priceTotal += mallShoppingCartItemVO.getGoodsCount() * mallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                NewBeeMallException.fail("价格异常");
            }
        }
        return ResultGenerator.genSuccessResult(itemsForSettle);
    }

    @GetMapping("/shop-cart/listByCartItemIds")
    @ApiOperation(value = "购物车列表", notes = "")
    public Result<List<MallShoppingCartItem>> cartItemListByIds(@RequestParam("cartItemIds") List<Long> cartItemIds) {
        if (CollectionUtils.isEmpty(cartItemIds)) {
            return ResultGenerator.genFailResult("error param");
        }
        return ResultGenerator.genSuccessResult(mallShoppingCartService.getCartItemsByCartIds(cartItemIds));
    }

    @DeleteMapping("/shop-cart/deleteByCartItemIds")
    @ApiOperation(value = "批量删除购物项", notes = "")
    public Result<Boolean> deleteByCartItemIds(@RequestParam("cartItemIds") List<Long> cartItemIds, HttpServletRequest request) {
        System.out.println("RootContext.getXID()="+ RootContext.getXID());
        if (CollectionUtils.isEmpty(cartItemIds)) {
            return ResultGenerator.genFailResult("error param");
        }
        return ResultGenerator.genSuccessResult(mallShoppingCartService.deleteCartItemsByCartIds(cartItemIds) > 0);
    }
}
