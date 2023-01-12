
package ltd.goods.cloud.newbee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.common.cloud.newbee.exception.NewBeeMallException;
import ltd.goods.cloud.newbee.controller.vo.MallIndexCategoryVO;
import ltd.goods.cloud.newbee.service.MallCategoryService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "新蜂商城分类页面接口")
@RequestMapping("/categories/mall")
public class MallGoodsCategoryController {

    @Resource
    private MallCategoryService mallCategoryService;

    @GetMapping("/listAll")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<MallIndexCategoryVO>> getCategories() {
        List<MallIndexCategoryVO> categories = mallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
