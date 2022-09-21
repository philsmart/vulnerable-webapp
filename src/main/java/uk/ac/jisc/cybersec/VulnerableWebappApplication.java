
package uk.ac.jisc.cybersec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

/**
 * Entry point to the vulnerable web application.
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan
public class VulnerableWebappApplication {

    public static void main(final String[] args) {
        SpringApplication.run(VulnerableWebappApplication.class, args);
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
