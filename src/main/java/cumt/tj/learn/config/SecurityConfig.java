package cumt.tj.learn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by sky on 17-5-1.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //csrf保护是默认打开的，关闭打开的csrf保护
                .csrf()
//                .disable()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                // 	requests matched against /css/** and /index are fully accessible，前端静态资源不用验证
                .antMatchers("/css/**", "/app/index.html").permitAll()
                //requests matched against /user/** require a user to be authenticated and must be associated to the USER role，访问以/user/开头的url，需要拥有“USER"角色
                .antMatchers("/app/home/home.html").hasRole("USER")
                .and()
                .formLogin()
                //form-based authentication is enabled with a custom login page and failure url，表单认证开启，从"/login"页面登录，登录失败返回“login-error”页面
                //loginPage指定当需要进行认证的时候，重定向的url。即当需要认证的时候，重定向到/login，让用户输入登录信息进行认证
                //failureUrl指定当认证失败的时候，重定向的url。即当认证失败的时候，重定向到/login-error，无情告诉用户登录失败
                //loginProcessingUrl指定处理认证请求的url。即前端的登录请求需要传到"login"，才会得到spring security的处理
                .loginProcessingUrl("/login")
                .successForwardUrl("/app/home/home.html")
                .loginPage("/app/login/login.html")
                .failureUrl("/login-error");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                //写死的认证，用户名为“user”，且密码为“password”的拥有“USER"身份
                .withUser("user").password("password").roles("USER");
    }
}
