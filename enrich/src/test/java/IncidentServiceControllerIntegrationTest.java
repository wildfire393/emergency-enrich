import application.EmergencyEnrichConfiguration;
import external.WeatherIntegration;
import net.minidev.json.parser.ParseException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.stream.Stream;

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
        Assert.assertNotEquals(weather, new JSONObject("{\"Error\":\"Weather data unavailable for specified time and location\"}"));
    }

    /**
     * Test incident submission
     */
    @Test
    public void testIncidentSubmission () throws IOException, ParseException, URISyntaxException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get("resources/F01705150050.json"), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        URL url = new URL(BASE_URL+"submit");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(RequestMethod.POST.toString());
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        con.setDoOutput(true);

        String jsonInputString = contentBuilder.toString();
        try (OutputStream os = con.getOutputStream()){
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8.name());
            os.write(input, 0, input.length);
        }

        BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8.name()));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        System.out.println(response.toString());

    }
}
