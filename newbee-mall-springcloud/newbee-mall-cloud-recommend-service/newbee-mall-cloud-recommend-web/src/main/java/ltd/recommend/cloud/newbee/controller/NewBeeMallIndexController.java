
package ltd.recommend.cloud.newbee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.common.cloud.newbee.enums.IndexConfigTypeEnum;
import ltd.common.cloud.newbee.pojo.MallUserToken;
import ltd.recommend.cloud.newbee.config.annotation.TokenToMallUser;
import ltd.recommend.cloud.newbee.controller.vo.IndexInfoVO;
import ltd.recommend.cloud.newbee.controller.vo.NewBeeMallIndexCarouselVO;
import ltd.recommend.cloud.newbee.controller.vo.NewBeeMallIndexConfigGoodsVO;
import ltd.recommend.cloud.newbee.service.NewBeeMallBrowseService;
import ltd.recommend.cloud.newbee.service.NewBeeMallCarouselService;
import ltd.recommend.cloud.newbee.service.NewBeeMallIndexConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "新蜂商城首页接口")
@RequestMapping("/mall/index")
public class NewBeeMallIndexController {

    @Resource
    private NewBeeMallCarouselService newBeeMallCarouselService;

    @Resource
    private NewBeeMallIndexConfigService newBeeMallIndexConfigService;

    @Resource
    private NewBeeMallBrowseService browseService;

    @GetMapping("/recommondInfos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
//    @TokenToMallUser MallUserToken mallUser
    public Result recommondInfos() {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<NewBeeMallIndexCarouselVO> carousels = newBeeMallCarouselService.getCarouselsForIndex(5);
        List<NewBeeMallIndexConfigGoodsVO> hotGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), 10);
        List<NewBeeMallIndexConfigGoodsVO> newGoodses = newBeeMallIndexConfigService.listNewGoods(10);
        List<NewBeeMallIndexConfigGoodsVO> recommendGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), 10);
//        List<NewBeeMallIndexConfigGoodsVO> recommendGoodses;
        //如果用户未登录，则随便推荐
//        if(mallUser == null) {
//            recommendGoodses = browseService.getRecommendGoodsByUserId(0L);
//        } else {    //否则按照用户的浏览记录推荐
//            recommendGoodses = browseService.getRecommendGoodsByUserId(mallUser.getUserId());
//        }
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodses);
        return ResultGenerator.genSuccessResult(indexInfoVO);
    }
}
