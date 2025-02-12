package com.example.petcare.global.audit;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .version("v0.1")
                        .title("프로젝트 이름")
                        .description("API DOC")
                );
    }

    // Session 있는 상태로 검사
//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .addSecurityItem(new SecurityRequirement().addList("sessionAuth"))
//                .components(new Components()
//                        .addSecuritySchemes("sessionAuth", new SecurityScheme()
//                                .type(SecurityScheme.Type.APIKEY)
//                                .in(SecurityScheme.In.COOKIE)
//                                .name("JSESSIONID")));
//    }
}
