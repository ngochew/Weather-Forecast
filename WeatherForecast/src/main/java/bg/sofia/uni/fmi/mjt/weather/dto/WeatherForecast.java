package bg.sofia.uni.fmi.mjt.weather.dto;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class WeatherForecast {

    @SerializedName("weather")
    private WeatherCondition[] weatherConditions;
    @SerializedName("main")
    private WeatherData weatherData;

    public WeatherForecast(WeatherCondition[] weatherConditions, WeatherData weatherData) {
        this.weatherConditions = weatherConditions;
        this.weatherData = weatherData;
    }

    public WeatherCondition[] getWeatherConditions() {
        return weatherConditions;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    @Override
    public String toString() {
        return "WeatherForecast : " + System.lineSeparator() +
                "weatherConditions = " + weatherConditions[0].getDescription() + System.lineSeparator() +
                "weatherData : actual temperature : " + getWeatherData().getTemp() +
                ", feels like : " + getWeatherData().getFeelsLike();
    }
}
