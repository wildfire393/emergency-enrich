package external;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

/**
 * Class for interfacing with the DarkSky Weather API to pull weather for a given date and lat/lon
 *
 * @author James
 */
public class WeatherIntegration {

    private static final String SECRET_KEY = "a5e2fe894380bf21db84ca167725bacf";
    private static final String BASE_URL = "https://api.darksky.net/forecast/";

    /**
     * Get the weather data for the specified location and time
     * @param lat - The latitude to get weather data for
     * @param lon - The longitude to get weather data for
     * @param timestamp - The timestamp to get weather data for
     * @return The JSON Weather Data from DarkSky
     */
    public static JSONObject getWeather(String lat, String lon, Timestamp timestamp) {
        try {
            URL url = new URL(BASE_URL + SECRET_KEY + "/" + lat + "," + lon + "," + timestamp.getTime());

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(RequestMethod.GET.toString());

            con.connect();

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8.name()));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JSONObject json = new JSONObject(response.toString());
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONObject("Weather data unavailable for specified time and location");
        }
    }
}
