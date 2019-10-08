import application.EmergencyEnrichConfiguration;
import external.WeatherIntegration;
import org.apache.logging.log4j.core.util.IOUtils;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import java.sql.Timestamp;

public class IncidentServiceControllerIntegrationTest {

    public static final String BASE_URL = "http://localhost:8080/incidents/";

    static {
        SpringApplication.run(EmergencyEnrichConfiguration.class, new String[]{});
    }

    /**
     * Test weather retrieval
     */
    @Test
    public void testWeather(){
        String lat = "37.466513";
        String lon = "-77.428683";
        Timestamp timestamp = new Timestamp(1494897378);
        JSONObject weather = WeatherIntegration.getWeather(lat, lon, timestamp);
        System.out.println(weather.toString());
        Assert.assertNotEquals(weather, new JSONObject("Weather data unavailable for specified time and location"));
    }

    /**
     * Test incident submission
     */
    @Test
    public void testIncidentSubmission(){

    }
}
