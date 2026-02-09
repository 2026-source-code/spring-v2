package com.example.boardv1._core.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<FirstFilter> firstFilter() {
        FilterRegistrationBean<FirstFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new FirstFilter());
        bean.addUrlPatterns("/*"); // 모든 요청에 대해 검사
        bean.setOrder(1); // 필터 순서
        return bean;
    }

    // @Bean
    public FilterRegistrationBean<LoginFilter> loginFilter() {
        FilterRegistrationBean<LoginFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LoginFilter());
        bean.addUrlPatterns("/boards/*"); // 모든 요청에 대해 검사
        bean.addUrlPatterns("/replies/*"); // 모든 요청에 대해 검사

        bean.setOrder(2); // 필터 순서
        return bean;
    }

}
