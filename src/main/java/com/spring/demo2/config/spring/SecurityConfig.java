package com.spring.demo2.config.spring;

import com.spring.demo2.config.spring.loginConfig.AuthenticationProvider;
import com.spring.demo2.config.spring.loginConfig.JwtAuthenticationFilter;
import com.spring.demo2.config.spring.loginConfig.JwtAuthorizationFilter;
import com.spring.demo2.config.spring.loginConfig.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
     securedEnabled = true
    ,jsr250Enabled = true
    ,prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private AuthenticationProvider authenticationProvider;

  public SecurityConfig( AuthenticationProvider authenticationProvider) {
    this.authenticationProvider = authenticationProvider;
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
    jwtAuthenticationFilter.setFilterProcessesUrl("/login");
    jwtAuthenticationFilter.setUsernameParameter("userId");
    jwtAuthenticationFilter.setPasswordParameter("userPw");

    jwtAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
    //jwtAuthenticationFilter.setAuthenticationFailureHandler();

    jwtAuthenticationFilter.afterPropertiesSet();

    return jwtAuthenticationFilter;
  }
  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
    return new JwtAuthorizationFilter(authenticationManager());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider);
  }

  /**
   * 스프링 시큐리티 Role 무시 Url
   * @param web
   * @throws Exception
   */
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/resources/**")
        .antMatchers("/css/**")
        .antMatchers("/js/**")
        .antMatchers("/img/**")
        .antMatchers("/favicon/**");
  }

  /**
   * 스프링 시큐리티 Role
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .headers().frameOptions().disable()
        .and()
          .authorizeRequests()
            .antMatchers("/login*/**", "/logout/**").permitAll()
            .anyRequest().hasRole("ADMIN")
        .and()
          .logout().logoutUrl("/logout")
        .and()
        /*
        .formLogin()
          .loginPage("/loginPage")
          .usernameParameter("userId")
          .passwordParameter("userPw")
        .and()
         */
        /*
        .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
         */
          .addFilter(jwtAuthenticationFilter())
          .addFilter(jwtAuthorizationFilter())
    ;
  }

  @Bean
  public AuthenticationSuccessHandler authenticationSuccessHandler() {
    SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    successHandler.setDefaultTargetUrl("/index");
    return successHandler;
  }


}
