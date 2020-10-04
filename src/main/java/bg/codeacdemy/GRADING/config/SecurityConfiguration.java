package bg.codeacdemy.GRADING.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
  @Override
  protected void configure(HttpSecurity http) throws Exception
  {
    //Added to test in Postman
    http
        .authorizeRequests()
        .anyRequest().permitAll()
        .and()
        .httpBasic();

    http.csrf().disable();
    http.headers().frameOptions().disable();
  }
}
