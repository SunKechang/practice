package ltd.user.cloud.newbee.controller;

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
import ltd.user.cloud.newbee.controller.vo.MallUserVO;
import ltd.user.cloud.newbee.entity.MallUser;
//import ltd.user.cloud.newbee.service.CodeService;
import ltd.user.cloud.newbee.service.MailService;
import ltd.user.cloud.newbee.service.MallUserService;
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
public class MallCloudPersonalController {

    @Resource
    private MallUserService mallUserService;


    @Autowired
    private MailService mailService;

    private static final Logger logger = LoggerFactory.getLogger(MallCloudPersonalController.class);

    @PostMapping("/login")
    @ApiOperation(value = "登录接口", notes = "返回token")
    public Result<String> login(@RequestBody @Valid MallUserLoginParam mallUserLoginParam) {
        // 判断是否是11位手机号
        if (!NumberUtil.isPhone(mallUserLoginParam.getLoginName())){
            return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = mallUserService.login(mallUserLoginParam.getLoginName(), mallUserLoginParam.getPasswordMd5());

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
        Boolean logoutResult = mallUserService.logout(loginMallUserToken.getToken());

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
        String registerResult = mallUserService.register(mallUserRegisterParam.getLoginName(), mallUserRegisterParam.getPassword());

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
        Boolean flag = mallUserService.updateUserInfo(mallUserUpdateParam, loginMallUserToken.getUserId());
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
    public Result<MallUserVO> getUserDetail(@TokenToMallUser MallUserToken loginMallUserToken) {
        MallUserVO mallUserVO = new MallUserVO();
        MallUser userDetailByToken = mallUserService.getUserDetailByToken(loginMallUserToken.getToken());
        BeanUtil.copyProperties(userDetailByToken, mallUserVO);
        System.out.print("获得个人信息---");
        System.out.println(mallUserVO);
        return ResultGenerator.genSuccessResult(mallUserVO);
    }

    @RequestMapping(value = "/getDetailByToken", method = RequestMethod.GET)
    public Result getMallUserByToken(@RequestParam("token") String token) {
        MallUser userDetailByToken = mallUserService.getUserDetailByToken(token);
        if (userDetailByToken != null) {
            Result result = ResultGenerator.genSuccessResult();
            result.setData(userDetailByToken);
            return result;
        }
        return ResultGenerator.genFailResult("无此用户数据");
    }


    @GetMapping("/simple")
    public Map<String, Object> sendSimpleMail() {
        Map<String, Object> map = new HashMap<>();
        try {
            //参数就是接收邮件的邮箱，根据自己实际填写
            System.out.println("测试发送");
            mailService.simpleMail("1750359399@qq.com");
            System.out.println("测试是否发送");
            map.put("res", "success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            map.put("res", "error");
        }
        return map;
    }

    /**
     * 发送手机验证码
     * @return
     */
    @PostMapping("/code")
    public String sendCode(String phone){
        Random random = new Random();
        int res = 1000+random.nextInt(9000);
        String code=Integer.toString(res);
        return mallUserService.send(phone,code);
    }
}
