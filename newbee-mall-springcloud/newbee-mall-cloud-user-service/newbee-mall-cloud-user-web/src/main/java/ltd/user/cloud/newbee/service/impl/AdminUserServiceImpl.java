
package ltd.user.cloud.newbee.service.impl;

import ltd.common.cloud.newbee.dto.PageQueryUtil;
import ltd.common.cloud.newbee.util.NumberUtil;
import ltd.common.cloud.newbee.util.SystemUtil;
import ltd.user.cloud.newbee.controller.param.UserListParam;
import ltd.user.cloud.newbee.dao.AdminUserMapper;
import ltd.user.cloud.newbee.dao.MallUserMapper;
import ltd.user.cloud.newbee.entity.AdminUser;
import ltd.common.cloud.newbee.pojo.AdminUserToken;
import ltd.user.cloud.newbee.entity.MallUser;
import ltd.user.cloud.newbee.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Resource
    private MallUserMapper mallUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String login(String userName, String password) {
        AdminUser loginAdminUser = adminUserMapper.login(userName, password);
        if (loginAdminUser != null) {
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", loginAdminUser.getAdminUserId());
            AdminUserToken adminUserToken = new AdminUserToken();
            adminUserToken.setAdminUserId(loginAdminUser.getAdminUserId());
            adminUserToken.setToken(token);
            ValueOperations<String, AdminUserToken> setToken = redisTemplate.opsForValue();
            setToken.set(token, adminUserToken, 2 * 24 * 60 * 60, TimeUnit.SECONDS);//过期时间 48 小时
            return token;
        }
        return "登录失败";
    }


    /**
     * 获取token值
     *
     * @param timeStr
     * @param userId
     * @return
     */
    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(6);
        return SystemUtil.genToken(src);
    }


    @Override
    public AdminUser getUserDetailById(Long loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }

    @Override
    public AdminUser getUserDetailByToken(String token) {
        ValueOperations<String, AdminUserToken> opsForAdminUserToken = redisTemplate.opsForValue();
        AdminUserToken adminUserToken = opsForAdminUserToken.get(token);
        if (adminUserToken != null) {
            return adminUserMapper.selectByPrimaryKey(adminUserToken.getAdminUserId());
        }
        return null;
    }

    @Override
    public Boolean updatePassword(Long loginUserId, String originalPassword, String newPassword) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            //比较原密码是否正确
            if (originalPassword.equals(adminUser.getLoginPassword())) {
                //设置新密码并修改
                adminUser.setLoginPassword(newPassword);
                if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                    //修改成功且清空当前token则返回true
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean updateName(Long loginUserId, String loginUserName, String nickName) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            //设置新名称并修改
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }

    @Override
    public Boolean logout(String token) {
        redisTemplate.delete(token);
        return true;
    }

    @Override
    public UserListParam getMallUsers(int pageNumber, int pageSize){
        UserListParam userListParam=new UserListParam();
        Map<String,Object> map=new HashMap<>();
        map.put("page",pageNumber);
        map.put("limit",pageSize);
        PageQueryUtil pageQueryUtil=new PageQueryUtil(map);
        List<MallUser> mallUserList=mallUserMapper.findMallUserList(pageQueryUtil);
        int count=mallUserMapper.getTotalMallUsers(pageQueryUtil);
        userListParam.setList(mallUserList);
        userListParam.setTotalCount(count);
        userListParam.setCurrPage(pageNumber);
        return userListParam;
    }

    public String forbid(Long ids[]){
        int count=mallUserMapper.lockUserBatch(ids,1);
        if(count>=1){
            return "success";
        }else{
            return "fail";
        }
    }
    public String open(Long ids[]){
        int count=mallUserMapper.lockUserBatch(ids,0);
        if(count>=1){
            return "success";
        }else{
            return "fail";
        }
    }
}
