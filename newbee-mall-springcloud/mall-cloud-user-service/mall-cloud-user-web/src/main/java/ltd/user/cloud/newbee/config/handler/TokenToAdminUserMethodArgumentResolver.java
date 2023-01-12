
package ltd.user.cloud.newbee.config.handler;

import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.exception.NewBeeMallException;
import ltd.user.cloud.newbee.config.annotation.TokenToAdminUser;
import ltd.common.cloud.newbee.pojo.AdminUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenToAdminUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private RedisTemplate redisTemplate;

    public TokenToAdminUserMethodArgumentResolver() {
    }

    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToAdminUser.class)) {
            return true;
        }
        return false;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToAdminUser.class) instanceof TokenToAdminUser) {
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == 32) {
                ValueOperations<String, AdminUserToken> opsForAdminUserToken = redisTemplate.opsForValue();
                AdminUserToken adminUserToken = opsForAdminUserToken.get(token);
                if (adminUserToken == null) {
                    NewBeeMallException.fail(ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult());
                }
                return adminUserToken;
            } else {
                NewBeeMallException.fail(ServiceResultEnum.ADMIN_NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }

}
