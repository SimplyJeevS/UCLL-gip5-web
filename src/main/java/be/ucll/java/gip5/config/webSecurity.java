package be.ucll.java.gip5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class webSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity security) throws Exception
    {
        security
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity security) throws Exception
    {
        security
                .ignoring().antMatchers(
                "/VAADIN/**",
                "/frontend/**",
                "/webjars/**",
                "/images/**",
                "/frontend-es5/**", "/frontend-es6/**");
    }
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}