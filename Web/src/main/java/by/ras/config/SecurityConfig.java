package by.ras.config;

import by.ras.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                //.passwordEncoder()
                ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf()
//                 .disable()
//                 ;
        http
                .authorizeRequests()
                .antMatchers("/market/registration", "/test").anonymous()

                .antMatchers("/market/products/*").authenticated()
                .antMatchers("/market/admin/*").hasAuthority("ADMIN")
                .antMatchers("/market/user/*").hasAuthority("CLIENT")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/").defaultSuccessUrl("/", true).failureUrl("/?error").permitAll()
                .and()
                //.invalidateHttpSession(true)
                .logout().invalidateHttpSession(true).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error/403")

//                .and()
//                .userDetailsService(userDetailsService)
        ;

//        http.formLogin()
//                // указываем страницу с формой логина
//                .loginPage("/")
//                // указываем action с формы логина
//                .loginProcessingUrl("/")
//                // указываем URL при неудачном логине
//                .failureUrl("/login?error")
//                // Указываем параметры логина и пароля с формы логина
////                .usernameParameter("j_username")
////                .passwordParameter("j_password")
//                // даем доступ к форме логина всем
//                .permitAll()
//                ;
//
//        http.logout()
//                // разрешаем делать логаут всем
//                //.permitAll()
//                // указываем URL логаута
//                .logoutUrl("/logout")
//                // указываем URL при удачном логауте
//                .logoutSuccessUrl("/login?logout")
//                // делаем не валидной текущую сессию
//                .invalidateHttpSession(true)
//                ;

        //get credetials of current user session
        //SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
