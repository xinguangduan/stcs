package org.stcs.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/stcs/swagger-ui/**", "/stcs/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and().csrf().disable();
        /*
         * Spring Security会对POST、PUT、PATCH等数据提交类的请求进行CSRF验证(防止跨站请求伪造攻击)，
         * Spring Security要求这些请求必须携带CSRFToken，但是目前Postman并不会利用Authorization页签中填上的Basic Auth相关信息生成CSRFToken。
         * 你得用自己的登录接口来生成，并将生成的CSRFToken添加到Postman后续的各个测试请求的Headers中(X-CSRFToken)。
         */
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("stcs")
                .password(passwordEncoder().encode("stcs"))
                .authorities("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}