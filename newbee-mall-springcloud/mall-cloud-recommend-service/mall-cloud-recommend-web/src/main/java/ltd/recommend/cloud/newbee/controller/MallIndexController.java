
package ltd.recommend.cloud.newbee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.common.cloud.newbee.enums.IndexConfigTypeEnum;
import ltd.common.cloud.newbee.pojo.MallUserToken;
import ltd.recommend.cloud.newbee.config.annotation.TokenToMallUser;
import ltd.recommend.cloud.newbee.controller.vo.IndexInfoVO;
import ltd.recommend.cloud.newbee.controller.vo.MallIndexCarouselVO;
import ltd.recommend.cloud.newbee.controller.vo.MallIndexConfigGoodsVO;
import ltd.recommend.cloud.newbee.service.MallBrowseService;
import ltd.recommend.cloud.newbee.service.MallCarouselService;
import ltd.recommend.cloud.newbee.service.MallIndexConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "新蜂商城首页接口")
@RequestMapping("/mall/index")
public class MallIndexController {

    @Resource
    private MallCarouselService mallCarouselService;

    @Resource
    private MallIndexConfigService mallIndexConfigService;

    @Resource
    private MallBrowseService browseService;

    @GetMapping("/recommondInfos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图、新品、推荐等")
    public Result recommondInfos(@TokenToMallUser MallUserToken mallUser) {
        IndexInfoVO indexInfoVO = new IndexInfoVO();
        List<MallIndexCarouselVO> carousels = mallCarouselService.getCarouselsForIndex(5);
        List<MallIndexConfigGoodsVO> hotGoodses = mallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), 4);
        List<MallIndexConfigGoodsVO> newGoodses = mallIndexConfigService.listNewGoods(8);
//        List<NewBeeMallIndexConfigGoodsVO> recommendGoodses = newBeeMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), 10);
        List<MallIndexConfigGoodsVO> recommendGoodses;
        //如果用户未登录，则随便推荐
        if(mallUser == null) {
            recommendGoodses = browseService.getRecommendGoodsByUserId(0L);
        } else {    //否则按照用户的浏览记录推荐
            recommendGoodses = browseService.getRecommendGoodsByUserId(mallUser.getUserId());
        }
        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodses);
        return ResultGenerator.genSuccessResult(indexInfoVO);
    }
}
