//package com.example.seckilldemo.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//
//import java.util.Arrays;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        // 允许的来源
//        config.setAllowedOrigins(Arrays.asList("http://192.168.0.131:5174"));
//        // 允许的方法
//        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        // 允许的头部
//        config.setAllowedHeaders(Arrays.asList("*"));
//        // 是否允许发送 Cookie
//        config.setAllowCredentials(true);
//
//        source.registerCorsConfiguration("/api/**", config);
//        return new CorsFilter(source);
//    }
//}