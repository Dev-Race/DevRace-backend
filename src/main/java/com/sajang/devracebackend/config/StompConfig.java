package com.sajang.devracebackend.config;

import com.sajang.devracebackend.security.jwt.JwtChannelInterceptor;
import com.sajang.devracebackend.security.jwt.TokenProvider;
import com.sajang.devracebackend.security.jwt.handler.JwtStompExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final TokenProvider tokenProvider;

    @Value("${spring.rabbitmq.username}")
    private String rabbitUser;
    @Value("${spring.rabbitmq.password}")
    private String rabbitPw;
    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;
    @Value("${spring.rabbitmq.stomp-port}")
    private int stompPort;
    private String rabbitVirtualHost = "/";


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableStompBrokerRelay("/exchange")
                .setClientLogin(rabbitUser)
                .setClientPasscode(rabbitPw)
                .setSystemLogin(rabbitUser)
                .setSystemPasscode(rabbitPw)
                .setRelayHost(rabbitHost)
                .setRelayPort(stompPort)
                .setVirtualHost(rabbitVirtualHost);

        registry.setPathMatcher(new AntPathMatcher("."));
        registry.setApplicationDestinationPrefixes("/pub");
    }

    // 웹소켓 핸드셰이크 커넥션을 생성할 경로
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
        // .withSockJS();

        registry.setErrorHandler(new JwtStompExceptionHandler());  // 소켓 통신 중, 예외가 발생했을 때 JwtStompExceptionHandler로 제어권이 넘어감.
    }

    // 프론트엔드->백엔드로 실시간 Stomp 요청을 주었을때 채널 인터셉터
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new JwtChannelInterceptor(tokenProvider));
    }
}