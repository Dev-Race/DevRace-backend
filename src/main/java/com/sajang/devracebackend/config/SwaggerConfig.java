package com.sajang.devracebackend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        String authName = "JWT";  // 기능 타이틀명
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(authName);

        Components components = new Components()
                .addSecuritySchemes(
                        authName,
                        new SecurityScheme()
                                .name(authName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT")
                                .description("'Bearer '을 제외한 Access Token 입력하세요.")
                );

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Dev Race - Swagger API")
                .description("<a href=\"https://sahyunjin.notion.site/2071d1695b254b78a1367ef555d6b820?v=336213c0a3b345f28fdb4ef181044d31&pvs=4\" target=\"blank\">Rest API 명세서</a> / <a href=\"https://sahyunjin.notion.site/2e577f50c85648cdaae86aeeae66be5a?v=a717219fcb494da39e765bea246b6cfd&pvs=4\" target=\"blank\">WebSocket API 명세서</a>")
                .version("1.0.0");
    }
}
