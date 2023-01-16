
package ltd.goods.cloud.newbee.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.cloud.newbee.enums.NewBeeMallCategoryLevelEnum;
import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.common.cloud.newbee.util.BeanUtil;
import ltd.goods.cloud.newbee.config.annotation.TokenToAdminUser;
import ltd.goods.cloud.newbee.controller.param.BatchIdParam;
import ltd.goods.cloud.newbee.controller.param.GoodsCategoryAddParam;
import ltd.goods.cloud.newbee.controller.param.GoodsCategoryEditParam;
import ltd.goods.cloud.newbee.entity.GoodsCategory;
import ltd.goods.cloud.newbee.entity.LoginAdminUser;
import ltd.goods.cloud.newbee.service.NewBeeMallCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

@RestController
@Api(value = "v1", tags = "后台管理系统分类模块接口")
@RequestMapping("/categories/admin")
public class NewBeeAdminGoodsCategoryController {

    private static final Logger logger = LoggerFactory.getLogger(NewBeeAdminGoodsCategoryController.class);

    @Resource
    private NewBeeMallCategoryService newBeeMallCategoryService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation(value = "商品分类列表", notes = "根据级别和上级分类id查询")
    public Result list(@RequestParam(required = false) @ApiParam(value = "页码") Integer pageNumber,
                       @RequestParam(required = false) @ApiParam(value = "每页条数") Integer pageSize,
                       @RequestParam(required = false) @ApiParam(value = "分类级别") Integer categoryLevel,
                       @RequestParam(required = false) @ApiParam(value = "上级分类的id") Long parentId, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (pageNumber == null || pageNumber < 1 || pageSize == null || pageSize < 10 || categoryLevel == null || categoryLevel < 0 || categoryLevel > 3 || parentId == null || parentId < 0) {
            return ResultGenerator.genFailResult("分页参数异常！");
        }
        Map params = new HashMap(8);
        params.put("page", pageNumber);
        params.put("limit", pageSize);
        params.put("categoryLevel", categoryLevel);
        params.put("parentId", parentId);
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(newBeeMallCategoryService.getCategorisPage(pageUtil));
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list4Select", method = RequestMethod.GET)
    @ApiOperation(value = "商品分类列表", notes = "用于三级分类联动效果制作")
    public Result listForSelect(@RequestParam("categoryId") Long categoryId, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (categoryId == null || categoryId < 1) {
            return ResultGenerator.genFailResult("缺少参数！");
        }
        GoodsCategory category = newBeeMallCategoryService.getGoodsCategoryById(categoryId);
        //既不是一级分类也不是二级分类则为不返回数据
        if (category == null || category.getCategoryLevel() == NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        Map categoryResult = new HashMap(4);
        if (category.getCategoryLevel() == NewBeeMallCategoryLevelEnum.LEVEL_ONE.getLevel()) {
            //如果是一级分类则返回当前一级分类下的所有二级分类，以及二级分类列表中第一条数据下的所有三级分类列表
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                categoryResult.put("secondLevelCategories", secondLevelCategories);
                categoryResult.put("thirdLevelCategories", thirdLevelCategories);
            }
        }
        if (category.getCategoryLevel() == NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel()) {
            //如果是二级分类则返回当前分类下的所有三级分类列表
            List<GoodsCategory> thirdLevelCategories = newBeeMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
            categoryResult.put("thirdLevelCategories", thirdLevelCategories);
        }
        return ResultGenerator.genSuccessResult(categoryResult);
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增分类", notes = "新增分类")
    public Result save(@RequestBody @Valid GoodsCategoryAddParam goodsCategoryAddParam, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtil.copyProperties(goodsCategoryAddParam, goodsCategory);
        String result = newBeeMallCategoryService.saveCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation(value = "修改分类信息", notes = "修改分类信息")
    public Result update(@RequestBody @Valid GoodsCategoryEditParam goodsCategoryEditParam, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        GoodsCategory goodsCategory = new GoodsCategory();
        BeanUtil.copyProperties(goodsCategoryEditParam, goodsCategory);
        String result = newBeeMallCategoryService.updateGoodsCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "获取单条分类信息", notes = "根据id查询")
    public Result info(@PathVariable("id") Long id, @TokenToAdminUser LoginAdminUser adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        GoodsCategory goodsCategory = newBeeMallCategoryService.getGoodsCategoryById(id);
        if (goodsCategory == null) {
            return ResultGenerator.genFailResult("未查询到数据");
        }
        return ResultGenerator.genSuccessResult(goodsCategory);
    }

    /**
     * 分类删除
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除分类信息", notes = "批量删除分类信息")
    public Result delete(@RequestBody BatchIdParam batchIdParam, @TokenToAdminUser LoginAdminUser adminUser) {
        System.out.println(batchIdParam);
        logger.info("adminUser:{}", adminUser.toString());
        if (batchIdParam == null || batchIdParam.getIds().length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (newBeeMallCategoryService.deleteBatch(batchIdParam.getIds())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    /**
     * oss图传
     */
    @GetMapping("/oss/policy")
    public Map<String,String> policy(){
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessId = "LTAI5tQ65XNhmcrpQbjBYWND";
        String accessKey = "2DKF6FGVgQYepS2MuBVWvcCnJdcNQ6";
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        // 填写Bucket名称，例如examplebucket。
        String bucket = "wuyanzu-imgs";
        // 填写Host地址，格式为https://bucketname.endpoint。
        String host = "https://"+bucket+"."+endpoint;
//        // 设置上传回调URL，即回调服务器地址，用于处理应用服务器与OSS之间的通信。OSS会在文件上传完成后，把文件上传信息通过此回调URL发送给应用服务器。
//        String callbackUrl = "https://192.168.0.0:8888";
        // 设置上传到OSS文件的前缀，可置空此项。置空后，文件将上传至Bucket的根目录下。
        String dir = "main-img/";

        // 创建ossClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessId", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            return respMap;
            // respMap.put("expire", formatISO8601Date(expiration));

//            JSONObject jasonCallback = new JSONObject();
//            jasonCallback.put("callbackUrl", callbackUrl);
//            jasonCallback.put("callbackBody",
//                    "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
//            jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
//            String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
//            respMap.put("callback", base64CallbackBody);
//
//            JSONObject ja1 = JSONObject.fromObject(respMap);
//            // System.out.println(ja1.toString());
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "GET, POST");
//            response(request, response, ja1.toString());

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        }
        return null;
    }
}
