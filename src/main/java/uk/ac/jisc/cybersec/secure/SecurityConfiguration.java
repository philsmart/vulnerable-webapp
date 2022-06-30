
package uk.ac.jisc.cybersec.secure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Value("${csrfEnabled:false}")
    public boolean csrfEnabled;

    @Value("${sameSiteEnabled:false}")
    public boolean sameSiteEnabled;

    @Value("${httpOnlyCookiesEnabled:false}")
    public boolean httpOnlyCookiesEnabled;

    @Value("${cspEnabled:false}")
    public boolean cspEnabled;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/resources/**", "/webjars/**", "/css/**", "/lottery-winners", "/pet-rescue", "/sf-login")
                .permitAll().anyRequest().authenticated().and().formLogin().failureUrl("/login-failed")
                .defaultSuccessUrl("/").permitAll().and().logout().logoutUrl("/logout").permitAll().and().csrf()
                .disable();

        log.info("Is CSRF protection enabled '{}'", csrfEnabled);
        if (csrfEnabled) {
            http.csrf();
        }
        log.info("Is CSP protection enabled '{}'", cspEnabled);
        if (cspEnabled) {
            http.headers().contentSecurityPolicy("script-src 'self'");
        }
        return http.build();
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        log.info("Are HttpOnly session cookies set '{}'", httpOnlyCookiesEnabled);
        if (httpOnlyCookiesEnabled) {
            return tomcat -> tomcat.addContextCustomizers(context -> context.setUseHttpOnly(true));
        } else {
            return tomcat -> tomcat.addContextCustomizers(context -> context.setUseHttpOnly(false));
        }
    }

    @Bean
    public CookieSameSiteSupplier applicationCookieSameSiteSupplier() {
        log.info("Is samesite protection enabled '{}'", sameSiteEnabled);
        if (sameSiteEnabled) {
            return CookieSameSiteSupplier.ofStrict();
        } else {
            return CookieSameSiteSupplier.ofNone();
        }

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService());
        return daoAuthenticationProvider;
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

}
