package ltd.recommend.cloud.newbee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.recommend.cloud.newbee.entity.Browse;
import ltd.recommend.cloud.newbee.service.MallBrowseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(value = "v1", tags = "新蜂商城首页接口")
@RequestMapping("/mall/index")
public class MallBrowseController {

    @Resource
    MallBrowseService browseService;

    @PostMapping("/addBrowse")
    @ApiOperation(value = "增加浏览记录", notes = "记录浏览分类id")
    public Result addBrowse(@RequestParam @ApiParam(value = "用户id") Long userId,
                            @RequestParam @ApiParam(value = "分类id") Long categoryId) {
        Browse browse = new Browse();
        browse.setCategoryId(categoryId);
        browse.setUserId(userId);
        browseService.addBrowse(browse);
        return ResultGenerator.genSuccessResult();
    }
}
