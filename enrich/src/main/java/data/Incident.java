package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

/**
 * Contains all information about a 911 Incident.
 * Also provides the ability to translate to and from a JSON string.
 *
 * @author James
 */
@Entity
@JsonDeserialize(builder = Incident.Builder.class)
@TypeDefs({
        @TypeDef(name="json-node", typeClass = JsonNodeStringType.class)
})
public class Incident {

    /** ID Column */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /** Incident JSON column */
    @Column(columnDefinition = "clob")
    private JSONObject incidentJson;

    /** Associated Weather column */
    @Column(columnDefinition = "clob")
    private JSONObject weather;

    private Timestamp timestamp;

    /**
     * Constructor for Incident
     * @param incidentJson The incident JSON. Never {@code null}
     * @param timestamp The timestamp the incident took place. Never {@code null}
     */
    public Incident (@JsonProperty("incident") JSONObject incidentJson,
                     @JsonProperty("weather") JSONObject weather,
                     @JsonProperty("timestamp") Timestamp timestamp){
        this.id = UUID.randomUUID();
        this.incidentJson = incidentJson;
        this.weather = weather;
        this.timestamp = timestamp;
    }

    /**
     * Constructor used by JPA
     */
    private Incident(){};

    /**
     * Constructs an Incident from a Builder
     * @param builder The builder to construct from
     */
    private Incident (Builder builder){
        this.id = builder.id;
        this.incidentJson = builder.incidentJson;
        this.weather = builder.weather;
        this.timestamp = builder.timestamp;
    }

    /**
     * Get the UUID of the Incident.
     * @return the ID
     */
    public UUID getId(){
        return id;
    }

    /**
     * Set the UUID for the Incident.
     * @param id The UUID to set. Cannot be null.
     */
    public void setId(UUID id){
        this.id = Objects.requireNonNull(id);
    }

    /**
     * Get the incident JSON
     * @return The incident JSON
     */
    public JSONObject getIncidentJson (){
        return incidentJson;
    }

    /**
     * Set the incident JSON
     * @param incidentJson The JSON to set. Cannot be null.
     */
    public void setIncidentJson(JSONObject incidentJson){
        this.incidentJson = Objects.requireNonNull(incidentJson);
    }

    /**
     * Get the associated weather JSON
     * @return The weather JSON
     */
    public JSONObject getWeather (){
        return weather;
    }

    /**
     * Set the associated weather JSON
     * @param weather The weather JSON to set. Cannot be null.
     */
    public void setWeather(JSONObject weather){
        this.incidentJson = Objects.requireNonNull(incidentJson);
    }

    /**
     * Get the timestamp.
     * @return The timestamp.
     */
    public Timestamp getTimestamp (){
        return timestamp;
    }

    /**
     * Set the timetsamp
     * @param timestamp The timestamp to set. Cannot be null.
     */
    public void setTimestamp(Timestamp timestamp){
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    /**
     * Builder to build {@link Incident}
     */
    public static final class Builder {
        private UUID id;
        private JSONObject incidentJson;
        private JSONObject weather;
        private Timestamp timestamp;

        /**
         * Builder with all parameters
         * @param id - The UUID of the Subscription to build.
         * @param incidentJson - The JSON of the incident.
         * @param weather - the JSON of the weather.
         * @param timestamp - the Timestamp of the incident
         */
        public Builder(UUID id, JSONObject incidentJson, JSONObject weather, Timestamp timestamp){
            this.id = id;
            this.incidentJson = incidentJson;
            this.weather = weather;
            this.timestamp = timestamp;
        }

        /**
         * Default JPA constructor
         */
        public Builder (){}

        /**
         * Build Incident from other Incident
         * @param incident - The other Incident
         */
        private Builder(Incident incident){
            this.id = incident.id;
            this.incidentJson = incident.incidentJson;
            this.weather = incident.weather;
            this.timestamp = incident.timestamp;
        }

        /**
         * Add or modify the id
         * @param id - The id
         * @return The builder
         */
        public Builder withId(UUID id){
            this.id = id;
            return this;
        }

        /**
         * Add or  modify the incidentJSON
         * @param incidentJson - The incident's JSON
         * @return The builder
         */
        public Builder withIncidentJson(JSONObject incidentJson){
            this.incidentJson = incidentJson;
            return this;
        }

        /**
         * Add or modify the weather JSON
         * @param weather - The weather JSON
         * @return The builder
         */
        public Builder withWeather(JSONObject weather){
            this.weather = weather;
            return this;
        }

        /**
         * Add or modify the timestamp
         * @param timestamp - The timestamp
         * @return The builder
         */
        public Builder withTimestamp(Timestamp timestamp){
            this.timestamp = timestamp;
            return this;
        }
    }
}
