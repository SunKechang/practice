package ltd.user.cloud.newbee.service.impl;

import com.zhenzi.sms.ZhenziSmsClient;
import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.dto.PageResult;
import ltd.common.cloud.newbee.exception.NewBeeMallException;
import ltd.common.cloud.newbee.pojo.MallUserToken;
import ltd.common.cloud.newbee.util.MD5Util;
import ltd.common.cloud.newbee.util.NumberUtil;
import ltd.common.cloud.newbee.util.SystemUtil;
import ltd.user.cloud.newbee.controller.param.MallUserUpdateParam;
import ltd.user.cloud.newbee.dao.MallUserMapper;
import ltd.user.cloud.newbee.entity.MallUser;
import ltd.user.cloud.newbee.service.NewBeeMallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 22729
 */
@Service
public class NewBeeMallUserServiceImpl implements NewBeeMallUserService {

    @Autowired
    private MallUserMapper mallUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.selectByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        registerUser.setSex("none");
        registerUser.setIntroduceSign("...");
        registerUser.setTelephone(loginName);
        registerUser.setMail("none");
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        registerUser.setIsDeleted(new Byte("0"));
        registerUser.setLockedFlag(new Byte("0"));
        registerUser.setCreateTime(new Date());
        if (mallUserMapper.insertSelective(registerUser) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5) {
        MallUser user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
        if (user != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", user.getUserId());
            MallUserToken mallUserToken = new MallUserToken();
            mallUserToken.setUserId(user.getUserId());
            mallUserToken.setToken(token);
            // 操作redis字符串
            ValueOperations<String, MallUserToken> setToken = redisTemplate.opsForValue();
            setToken.set(token, mallUserToken, 7 * 24 * 60 * 60, TimeUnit.SECONDS);//过期时间7天
            return token;

        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    /**
     * 获取token值
     *
     * @param timeStr
     * @param userId
     * @return
     */
    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

    @Override
    public Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId) {
        MallUser user = mallUserMapper.selectByPrimaryKey(userId);
        if (user == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        user.setNickName(mallUser.getNickName());
        //若密码为空字符，则表明用户不打算修改密码，使用原密码保存
//        if (!MD5Util.MD5Encode("", "UTF-8").equals(mallUser.getPasswordMd5())) {
//            user.setPasswordMd5(mallUser.getPasswordMd5());
//        }
//        System.out.println(mallUser.getSex());
        user.setMail(mallUser.getMail());
        user.setSex(mallUser.getSex());
        user.setTelephone(mallUser.getTelephone());
        user.setIntroduceSign(mallUser.getIntroduceSign());
        if (mallUserMapper.updateByPrimaryKeySelective(user) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public MallUser getUserDetailByToken(String token) {
        ValueOperations<String, MallUserToken> opsForMallUserToken = redisTemplate.opsForValue();
        MallUserToken mallUserToken = opsForMallUserToken.get(token);
        if (mallUserToken != null) {
            MallUser mallUser = mallUserMapper.selectByPrimaryKey(mallUserToken.getUserId());
            if (mallUser == null) {
                NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
            }
            if (mallUser.getLockedFlag().intValue() == 1) {
                NewBeeMallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
            }
            return mallUser;
        }
        NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        return null;
    }

    @Override
    public Boolean logout(String token) {
        redisTemplate.delete(token);
        return true;
    }

    @Override
    public PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil) {
        List<MallUser> mallUsers = mallUserMapper.findMallUserList(pageUtil);
        int total = mallUserMapper.getTotalMallUsers(pageUtil);
        PageResult pageResult = new PageResult(mallUsers, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean lockUsers(Long[] ids, int lockStatus) {
        if (ids.length < 1) {
            return false;
        }
        return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
    }

    @Override
    public String send(String phoneNumber, String code){
        // 使用自己的 AppId 和 AppSecret
        ZhenziSmsClient client = new ZhenziSmsClient("https://sms_developer.zhenzikj.com", "110913", "14d24011-9542-4cf4-a3a3-16aa541284ce");
        Map<String, Object> params = new HashMap<>();
        params.put("number", phoneNumber);
        // 修改为自己的templateId
        params.put("templateId", "8182");
        String[] templateParams = new String[2];
        templateParams[0] = code;
        templateParams[1] = "5";
        params.put("templateParams", templateParams);
        try {
            String result = client.send(params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "kong";
    }
}
