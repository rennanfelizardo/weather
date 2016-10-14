package weather.rennanfelizardo.com.br.weather.services;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import weather.rennanfelizardo.com.br.weather.models.City;

/**
 * Created by rennanfelizardo on 11/10/16.
 */

public interface OpenWeatherService {

    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY  = "e8003d199204e03c3c560faad85b86b9";

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("weather?")
    Call<City> getCityById(@Query("id") int cityId, @Query("units") String units, @Query("appid") String API_KEY);


    @GET("weather?")
    Call<City> getCityByName(@Query("q") String cityName, @Query("units") String units, @Query("appid") String API_KEY);
}
