package bg.sofia.uni.fmi.mjt;

import bg.sofia.uni.fmi.mjt.weather.WeatherForecastClient;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherCondition;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherData;
import bg.sofia.uni.fmi.mjt.weather.dto.WeatherForecast;

import bg.sofia.uni.fmi.mjt.weather.exceptions.LocationNotFoundException;
import bg.sofia.uni.fmi.mjt.weather.exceptions.WeatherForecastClientException;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class WeatherForecastClientTest {

    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> httpResponseMock;

    private WeatherForecastClient weatherForecastClient;
    private WeatherForecast randomWeatherForecast;
    private String randomWeatherForecastJson;

    @Before
    public void setUpData() {
        weatherForecastClient = new WeatherForecastClient(httpClientMock);

        WeatherCondition weatherCondition = new WeatherCondition("sunny");
        WeatherData weatherData = new WeatherData(30.5, 36.6);
        randomWeatherForecast = new WeatherForecast(new WeatherCondition[]{weatherCondition}, weatherData);
        randomWeatherForecastJson = new Gson().toJson(randomWeatherForecast);
    }

    @Test
    public void testValidCity() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponseMock.body()).thenReturn(randomWeatherForecastJson);

        WeatherForecast weatherForecastResult = weatherForecastClient.getForecast("Plovdiv");

        assertEquals("Wrong forecast.", randomWeatherForecast.toString(), weatherForecastResult.toString());
    }


    @Test(expected = LocationNotFoundException.class)
    public void testInvalidCity() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);

        WeatherForecast weatherForecastResult = weatherForecastClient.getForecast("RandomName");
    }

    @Test(expected = WeatherForecastClientException.class)
    public void testServerError() throws Exception {
        when(httpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<BodyHandler<String>>any()))
                .thenReturn(httpResponseMock);

        when(httpResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_UNAVAILABLE);

        WeatherForecast weatherForecastResult = weatherForecastClient.getForecast("Plovdiv");
    }
}
