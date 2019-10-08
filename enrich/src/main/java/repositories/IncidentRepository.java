package repositories;

import data.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Incident persistence repository using Spring Data
 *
 * @author James
 */
public interface IncidentRepository extends JpaRepository<Incident, UUID> {
}
