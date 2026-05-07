package com.yunbi.booking.booking_engine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookingEngineOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("콘서트 티켓팅 시스템 API 명세서")
                        .description("대규모 트래픽과 동시성 제어(Pessimistic Lock)를 고려하여 설계된 콘서트 예매 시스템의 API 문서입니다. (개발: yunbi)")
                        .version("v1.0.0"));
    }
}
