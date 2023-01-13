package ltd.user.cloud.newbee.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.dto.ResultGenerator;
import ltd.common.cloud.newbee.pojo.MallUserToken;
import ltd.common.cloud.newbee.util.BeanUtil;
import ltd.common.cloud.newbee.util.NumberUtil;
import ltd.user.cloud.newbee.config.annotation.TokenToMallUser;
import ltd.user.cloud.newbee.controller.param.MallUserLoginParam;
import ltd.user.cloud.newbee.controller.param.MallUserRegisterParam;
import ltd.user.cloud.newbee.controller.param.MallUserUpdateParam;
import ltd.user.cloud.newbee.controller.vo.NewBeeMallUserVO;
import ltd.user.cloud.newbee.entity.MallUser;
//import ltd.user.cloud.newbee.service.CodeService;
import ltd.user.cloud.newbee.service.NewBeeMallUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@Api(value = "v1", tags = "商城用户操作相关接口")
@RequestMapping("/users/mall")
public class NewBeeMallCloudPersonalController {

    @Resource
    private NewBeeMallUserService newBeeMallUserService;

    private static final Logger logger = LoggerFactory.getLogger(NewBeeMallCloudPersonalController.class);

    @PostMapping("/login")
    @ApiOperation(value = "登录接口", notes = "返回token")
    public Result<String> login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
        // 判断是否是11位手机号
        if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = newBeeMallUserService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMd5());

        logger.info("login api,loginName={},loginResult={}", mallUserLoginParam.getLoginName(), loginResult);

        //登录成功 StringUtils String的工具类
        if (!StringUtils.isEmpty(loginResult) && loginResult.length() == 32) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return ResultGenerator.genFailResult(loginResult);
    }


    @PostMapping("/logout")
    @ApiOperation(value = "登出接口", notes = "清除token")
    public Result<String> logout(@TokenToMallUser MallUserToken loginMallUserToken) {
        Boolean logoutResult = newBeeMallUserService.logout(loginMallUserToken.getToken());

        logger.info("logout api,loginMallUser={}", loginMallUserToken.getUserId());

        //登出成功
        if (logoutResult) {
            return ResultGenerator.genSuccessResult();
        }
        //登出失败
        return ResultGenerator.genFailResult("logout error");
    }


    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "")
    public Result register(@RequestBody @Valid MallUserRegisterParam mallUserRegisterParam) {
        if (!NumberUtil.isPhone(mallUserRegisterParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String registerResult = newBeeMallUserService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword());

        logger.info("register api,loginName={},loginResult={}", mallUserRegisterParam.getLoginName(), registerResult);

        //注册成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //注册失败
        return ResultGenerator.genFailResult(registerResult);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改用户信息", notes = "")
    public Result updateInfo(@RequestBody @ApiParam("用户信息") MallUserUpdateParam mallUserUpdateParam, @TokenToMallUser MallUserToken loginMallUserToken) {
        Boolean flag = newBeeMallUserService.updateUserInfo(mallUserUpdateParam, loginMallUserToken.getUserId());
        if (flag) {
            //返回成功
            Result result = ResultGenerator.genSuccessResult();
            return result;
        } else {
            //返回失败
            Result result = ResultGenerator.genFailResult("修改失败");
            return result;
        }
    }

    @GetMapping("/detail")
    @ApiOperation(value = "获取用户信息", notes = "")
    public Result<NewBeeMallUserVO> getUserDetail(@TokenToMallUser MallUserToken loginMallUserToken) {
        NewBeeMallUserVO mallUserVO = new NewBeeMallUserVO();
        MallUser userDetailByToken = newBeeMallUserService.getUserDetailByToken(loginMallUserToken.getToken());
        BeanUtil.copyProperties(userDetailByToken, mallUserVO);
        return ResultGenerator.genSuccessResult(mallUserVO);
    }

    @RequestMapping(value = "/getDetailByToken", method = RequestMethod.GET)
    public Result getMallUserByToken(@RequestParam("token") String token) {
        MallUser userDetailByToken = newBeeMallUserService.getUserDetailByToken(token);
        if (userDetailByToken != null) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(userDetailByToken);
            return result;
        }
        return ResultGenerator.genFailResult("无此用户数据");
    }

    /**
     * 发送手机验证码
     */
    @PostMapping("/code")
    public Result sendCode(@RequestBody String phone){
//        System.out.println(phone);
        Random random = new Random();
        int res = 1000+random.nextInt(9000);
        String code=Integer.toString(res);
        String a=newBeeMallUserService.send(phone,code);
        Result result = ResultGenerator.genSuccessResult();
        result.setData(a);
        return result;
    }

    /**
     * 改密码
     */
    @PostMapping("/changePwd")
    public Result changePwd(@RequestBody String pwd,@TokenToMallUser MallUserToken mallUserToken){
        Boolean flag = newBeeMallUserService.changePwd(pwd, mallUserToken.getUserId());
        Result result;
        if (flag) {
            //返回成功
            result = ResultGenerator.genSuccessResult();
            result.setData("success");
        } else {
            //返回失败
            result = ResultGenerator.genFailResult("密码必须为新密码");
        }
        return result;
    }
}
