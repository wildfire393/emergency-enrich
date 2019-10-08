package data;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Persistence Configuration for JPA entity and data repository scan base packages
 *
 * @author James
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "repositories")
@EntityScan(basePackages = "data")
public class PersistenceConfig {
}
