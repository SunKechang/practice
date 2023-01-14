package ltd.user.cloud.newbee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.user.cloud.newbee.config.annotation.TokenToAdminUser;
import ltd.user.cloud.newbee.controller.param.AdminLoginParam;
import ltd.user.cloud.newbee.controller.param.UpdateAdminNameParam;
import ltd.user.cloud.newbee.controller.param.UpdateAdminPasswordParam;
import ltd.user.cloud.newbee.controller.param.UserListParam;
import ltd.user.cloud.newbee.dao.MallUserMapper;
import ltd.user.cloud.newbee.entity.AdminUser;
import ltd.common.cloud.newbee.pojo.AdminUserToken;
import ltd.user.cloud.newbee.entity.MallUser;
import ltd.user.cloud.newbee.service.AdminUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Api(value = "v1", tags = "商城管理员操作相关接口")
@RestController
public class NewBeeMallCloudAdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(NewBeeMallCloudAdminUserController.class);

    @Resource
    private AdminUserService adminUserService;

    @ApiOperation(value = "登录接口", notes = "返回token")
    @PostMapping("/users/admin/login")
    public Result<String> login(@RequestBody @Valid AdminLoginParam adminLoginParam) {
//        System.out.println(adminLoginParam.getUserName());
        String loginResult = adminUserService.login(adminLoginParam.getUserName(), adminLoginParam.getPasswordMd5());
        logger.info("manage login api,adminName={},loginResult={}", adminLoginParam.getUserName(), loginResult);

        //登录成功
        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == 32) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        Result result=ResultGenerator.genFailResult(loginResult);
        result.setData("fail");
        return result;
    }
    //    @RequestMapping(value = "/users/admin/profile", method = RequestMethod.GET)
    @ApiOperation(value = "获取管理员信息接口")
    @GetMapping("/users/admin/profile")
    public Result profile(@TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        AdminUser adminUserEntity = adminUserService.getUserDetailById(adminUser.getAdminUserId());
        System.out.println(adminUser.getAdminUserId());
        if (adminUserEntity != null) {
            adminUserEntity.setLoginPassword("******");
            Result result = ResultGenerator.genSuccessResult();
            result.setData(adminUserEntity);
            return result;
        }
        return ResultGenerator.genFailResult("无此用户数据");
    }

    @ApiOperation(value = "修改管理员密码接口")
    @RequestMapping(value = "/users/admin/password", method = RequestMethod.PUT)
    public Result passwordUpdate(@RequestBody @Valid UpdateAdminPasswordParam adminPasswordParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (adminUserService.updatePassword(adminUser.getAdminUserId(), adminPasswordParam.getOriginalPassword(), adminPasswordParam.getNewPassword())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("DB ERROR");
        }
    }

    @ApiOperation(value = "修改管理员信息接口")
    @RequestMapping(value = "/users/admin/name", method = RequestMethod.PUT)
    public Result nameUpdate(@RequestBody @Valid UpdateAdminNameParam adminNameParam, @TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        if (adminUserService.updateName(adminUser.getAdminUserId(), adminNameParam.getLoginUserName(), adminNameParam.getNickName())) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("DB ERROR");
        }
    }

    @ApiOperation(value = "管理员退出登录的接口")
    @RequestMapping(value = "/users/admin/logout", method = RequestMethod.DELETE)
    public Result logout(@TokenToAdminUser AdminUserToken adminUser) {
        logger.info("adminUser:{}", adminUser.toString());
        adminUserService.logout(adminUser.getToken());
        return ResultGenerator.genSuccessResult();
    }

    @ApiOperation(value = "根据token获取管理员信息的接口", notes = "OpenFeign调用")
    @RequestMapping(value = "/users/admin/{token}", method = RequestMethod.GET)
    public Result<AdminUser> getAdminUserByToken(@PathVariable("token") String token) {
        AdminUser adminUser = adminUserService.getUserDetailByToken(token);
        if (adminUser != null) {
            adminUser.setLoginPassword("******");
            Result result = ResultGenerator.genSuccessResult();
            result.setData(adminUser);
            return result;
        }
        return ResultGenerator.genFailResult("无此用户数据");
    }


    @ApiOperation(value= "根据当前页和页大小获取一页的用户信息")
    @GetMapping("/users/admin/getMallUsers")
    public Result getMallUsers(int pageNumber,int pageSize){
        UserListParam userListParam = adminUserService.getMallUsers(pageNumber,pageSize);
        Result result=ResultGenerator.genSuccessResult();
        result.setData(userListParam);
        return result;
    }

    @PutMapping("/users/admin/forbid")
    public Result forbid(@RequestBody Map<String, Object> map){
        Object idsObj = map.get("ids");
        List<Object> idsList = (List<Object>) idsObj;
        Long[] ids = idsList.stream().map(id -> Long.parseLong(id.toString())).toArray(Long[]::new);
        String data= adminUserService.forbid(ids);
        Result result;
        if(Objects.equals(data, "success")){
            result=ResultGenerator.genSuccessResult();
        }else{
            result=ResultGenerator.genFailResult(data);
        }
        result.setData(data);
        return result;
    }
    @PutMapping("/users/admin/open")
    public Result open(@RequestBody Map<String, Object> map){
        Object idsObj = map.get("ids");
        List<Object> idsList = (List<Object>) idsObj;
        Long[] ids = idsList.stream().map(id -> Long.parseLong(id.toString())).toArray(Long[]::new);
        String data= adminUserService.open(ids);
        Result result;
        if(Objects.equals(data, "success")){
            result=ResultGenerator.genSuccessResult();
        }else{
            result=ResultGenerator.genFailResult(data);
        }
        result.setData(data);
        return result;
    }
}
