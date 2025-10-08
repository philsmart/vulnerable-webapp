
package uk.ac.jisc.cybersec.secure;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Value("${csrfEnabled:false}")
    public boolean csrfEnabled;

    @Value("${sameSiteEnabled:none}") // strict, lax, none
    public String sameSiteEnabled;

    @Value("${httpOnlyCookiesEnabled:false}")
    public boolean httpOnlyCookiesEnabled;

    @Value("${cspEnabled:false}")
    public boolean cspEnabled;

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/resources/**", "/webjars/**", "/css/**", "/lottery-winners", "/pet-rescue", "/sf-login", "/rce/**","/3pc","/iframe-me", "/img/**")
                .permitAll().anyRequest().authenticated().and().formLogin().failureUrl("/login-failed")
                .failureHandler(new AuthenticationFailureHandler() {
					
					@Override
					public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException exception) throws IOException, ServletException {
						log.info("Authentication failed!");
						response.sendRedirect("/login-failed");
						
					}
				})
                .successHandler(new AuthenticationSuccessHandler() {
					
					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						log.info("Success!");
						response.sendRedirect("/");
						
					}
				})
                .and().logout().logoutUrl("/logout").permitAll().and().csrf()
                .disable();

        log.info("Is CSRF protection enabled '{}'", csrfEnabled);
        if (csrfEnabled) {
            http.csrf();
        }
        log.info("Is CSP protection enabled '{}'", cspEnabled);
        if (cspEnabled) {
            http.headers().contentSecurityPolicy("script-src 'self'");
        }
        // Allow iframeing - bad.
        http.headers().frameOptions().disable();
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
        log.info("Samesite mode set as '{}'", sameSiteEnabled);
        if (sameSiteEnabled.equals("strict")) {
            return CookieSameSiteSupplier.ofStrict();
        } else if (sameSiteEnabled.equals("lax")) {
            return CookieSameSiteSupplier.ofLax();
        } else {
            return CookieSameSiteSupplier.ofNone();
        }

    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new MessageDigestPasswordEncoder("MD5");
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        final DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

}
