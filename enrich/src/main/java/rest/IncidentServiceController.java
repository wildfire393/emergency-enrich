package rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.Incident;
import external.WeatherIntegration;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import repositories.IncidentRepository;

import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Incident Controller implementation for REST endpoints.
 * Allows creation and retrieval of 911 Incidents
 *
 * @author James
 */
@RestController
@RequestMapping("/incidents")
@CrossOrigin
public class IncidentServiceController {

    /** ObjectMapper to create JsonNodes */
    private final ObjectMapper objectMapper;

    /**DateTimeFormatter for Dates */
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    /** Repository for Incidents */
    private final IncidentRepository incidentRepository;


    /** Logger for this class*/
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Constructor for IncidentServiceController to allow dependency injections
     * @param incidentRepository The repository for persisting subscriptions. Cannot be null.
     * @param objectMapper The object mapper for serializing JSON objects
     */
    public IncidentServiceController(IncidentRepository incidentRepository, ObjectMapper objectMapper){
        this.incidentRepository = incidentRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * Submit an Incident to the service
     * @param incidentJson - The JSON of the incident being submitted
     * @return The ID corresponding to the submitted incident if it was accepted.
     */
    @PostMapping(path = "/submit")
    ResponseEntity<UUID> submit (@RequestBody JSONObject incidentJson){
        JSONObject description = incidentJson.getJSONObject("description");
        String eventOpened = description.getString("event_opened");
        Timestamp timestamp = new Timestamp((Instant.from(dtf.parse(eventOpened))).getEpochSecond());
        JSONObject address = incidentJson.getJSONObject("address");
        String lat = address.getString("latitude");
        String lon = address.getString("longitude");
        JSONObject weather = WeatherIntegration.getWeather(lat, lon, timestamp);
        Incident.Builder builder = new Incident.Builder();
        Incident incident = builder.withId(UUID.randomUUID())
                .withIncidentJson(incidentJson)
                .withWeather(weather)
                .withTimestamp(timestamp)
                .build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(incident.getId());
    }

    /**
     * Retrieve an Incident by ID
     */
    @GetMapping("/{incidentnId}")
    ResponseEntity<Incident> retrieve(@PathVariable UUID incidentId){
        Optional<Incident> oIncident = incidentRepository.findById(incidentId);
        if (!oIncident.isPresent()){
            LOG.error("No incident with id {} could be found", incidentId);
            return ResponseEntity.badRequest().build();
        }
        else {
            Incident incident = oIncident.get();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(incident);
        }
    }
}
