
package ltd.recommend.cloud.newbee.config;

import ltd.common.cloud.newbee.pojo.AdminUserToken;
import ltd.recommend.cloud.newbee.entity.LoginAdminUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableOpenApi
public class RecommendServiceSwagger3Config{

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .ignoredParameterTypes(AdminUserToken.class, LoginAdminUser.class)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ltd.recommend.cloud.newbee.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(getGlobalRequestParameters());
    }

    //生成全局通用参数
    private List<RequestParameter> getGlobalRequestParameters() {
        List<RequestParameter> parameters = new ArrayList<>();
        parameters.add(new RequestParameterBuilder()
                .name("token")
                .description("登录认证token")
                .required(false) // 非必传
                .in(ParameterType.HEADER) //请求头中的参数，其它类型可以点进ParameterType类中查看
                .query(q -> q.model(m -> m.scalarModel(ScalarType.STRING)))
                .build());
        return parameters;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("newbee-mall-cloud-recommend-service接口文档")
                .description("swagger接口文档")
                .version("2.0")
                .build();
    }
}