package bg.sofia.uni.fmi.mjt.weather;

import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;
import bg.sofia.uni.fmi.mjt.weather.exceptions.LocationNotFoundException;
import bg.sofia.uni.fmi.mjt.weather.exceptions.WeatherForecastClientException;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherForecastClient {

    private HttpClient weatherHttpClient;
    private String apiKey;

    public WeatherForecastClient(HttpClient weatherHttpClient) {
        this.weatherHttpClient = weatherHttpClient;
    }

    public WeatherForecastClient(HttpClient weatherHttpClient, String apiKey) {
        this.weatherHttpClient = weatherHttpClient;
        this.apiKey = apiKey;
    }

    /**
     * Fetches the weather forecast for the specified city.
     *
     * @return the forecast
     * @throws //LocationNotFoundException      if the city is not found
     * @throws //WeatherForecastClientException if information regarding the weather for this location could not be retrieved
     */
    public WeatherForecast getForecast(String city) throws WeatherForecastClientException {
        HttpResponse<String> response;
        try {
            URI uri = new URI("http", "api.openweathermap.org", "/data/2.5/weather",
                    String.format("q=%s&units=metric&lang=bg&appid=%s", city, apiKey), null);

            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
            response = weatherHttpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new WeatherForecastClientException("There was a problem with WeatherForecastClient.", e);
        }
        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), WeatherForecast.class);
        } else if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            throw new LocationNotFoundException("Couldn't find location with name : " + city);
        }

        throw new WeatherForecastClientException("There was a problem with WeatherForecastClient.");
    }
}
