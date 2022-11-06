
package uk.ac.jisc.cybersec;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import uk.ac.jisc.cybersec.controller.AttackerController;

/**
 * Entry point to the vulnerable web application.
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan
public class VulnerableWebappApplication {
	
	private static final Logger log = LoggerFactory.getLogger(AttackerController.class);
	

    public static void main(final String[] args) {
        SpringApplication.run(VulnerableWebappApplication.class, args);
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }


}
