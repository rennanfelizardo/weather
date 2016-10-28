package weather.rennanfelizardo.com.br.weather;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weather.rennanfelizardo.com.br.weather.models.Days;
import weather.rennanfelizardo.com.br.weather.models.Forecast;
import weather.rennanfelizardo.com.br.weather.services.OpenWeatherService;

public class MainActivity extends AppCompatActivity {

    private TextView    cityView;
    private TextView    temperatureView;
    private TextView    humidityView;
    private Button      button;
    private TextInputEditText cityInput;
    private ImageView   temperatureImage;
    private TextView    weather;
    private TextView    windView;
    private TextView    maxTemperature;
    private TextView    minTemperature;
    private CardView    cardView;
    private LinearLayout mainActvity;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityView            =   (TextView) findViewById(R.id.city);
        temperatureView     =   (TextView) findViewById(R.id.temperature);
        humidityView        =   (TextView) findViewById(R.id.humidity);
        button              =   (Button) findViewById(R.id.buttonSearch);
        cityInput           =   (TextInputEditText) findViewById(R.id.cityInput);
        temperatureImage    =   (ImageView) findViewById(R.id.temperatureImage);
        weather             =   (TextView) findViewById(R.id.weather);
        windView            =   (TextView) findViewById(R.id.wind);
        maxTemperature      =   (TextView) findViewById(R.id.max_temperature);
        minTemperature      =   (TextView) findViewById(R.id.min_temperature);
        cardView            =   (CardView) findViewById(R.id.card);
        mainActvity         =   (LinearLayout) findViewById(R.id.activity_main);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cityName = cityInput.getText().toString();

                if (validateCityName()){
                    OpenWeatherService service = OpenWeatherService.retrofit.create(OpenWeatherService.class);

                    Call<Forecast> call = service.getCityByName(cityName, "metric", "8", OpenWeatherService.API_KEY);

                    call.enqueue(new Callback<Forecast>() {
                        @Override
                        public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                            cityView.setText(cityName);

                            List<Days> daysList = response.body().getList();

                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(daysList.get(0).getDt()*1000); //convert unix timestamp to datetime
                            //check if the first day from response is the same from the current date
                            int day = (cal.get(cal.DAY_OF_WEEK) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK))? 0 : 1;

                            temperatureView.setText(String.format("%s", Math.round(daysList.get(day).getTemp().getDay())));
                            humidityView.setText(daysList.get(day).getHumidity() + "%");
                            //conversion from m/s to km/h
                            windView.setText(String.format("%s", Math.round(daysList.get(day).getSpeed() * 3.6)) + " km/h");
                            maxTemperature.setText(String.format("%s", Math.round(daysList.get(day).getTemp().getMax())) + "º");
                            minTemperature.setText(String.format("%s", Math.round(daysList.get(day).getTemp().getMin())) + "º");

                            setIconAndWeather(daysList.get(1).getWeather().get(day).getIcon());
                            cardView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<Forecast> call, Throwable t) {
                            Snackbar.make(mainActvity, getString(R.string.err_cant_load), Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validateCityName(){
        String cityName = cityInput.getText().toString();
        cityName = removeAccents(cityName);

        Pattern pattern = Pattern.compile("[a-zA-Z ]+");
        Matcher matcher = pattern.matcher(cityName);

        if (TextUtils.isEmpty(cityName)){
            cityInput.setError(getString(R.string.err_empty_field));
            return false;

        }else if (!matcher.matches()){
            cityInput.setError(getString(R.string.err_only_letters));
            return false;
        }

        return true;
    }

    private String removeAccents(String cityName){
        return Normalizer.normalize(cityName, Normalizer.Form.NFD)
                    .replaceAll("[\\p{M}]", "");
    }

    private void setIconAndWeather(String icon) {
        switch (icon){
            case "01d":
                temperatureImage.setImageResource(R.drawable.clear_day);
                weather.setText(getString(R.string.clear_sky));
                break;
            case "01n":
                temperatureImage.setImageResource(R.drawable.clear_night);
                weather.setText(getString(R.string.clear_sky));
                break;
            case "02d":
                temperatureImage.setImageResource(R.drawable.partly_cloudy);
                weather.setText(getString(R.string.few_clouds));
                break;
            case "02n":
                temperatureImage.setImageResource(R.drawable.partly_cloudy_night);
                weather.setText(getString(R.string.few_clouds));
                break;
            case "03d":
                temperatureImage.setImageResource(R.drawable.mostly_cloudy);
                weather.setText(getString(R.string.scattered_clouds));
                break;
            case "03n":
                temperatureImage.setImageResource(R.drawable.mostly_cloudy_night);
                weather.setText(getString(R.string.scattered_clouds));
                break;
            case "04d":
                temperatureImage.setImageResource(R.drawable.cloudy_weather);
                weather.setText(getString(R.string.broken_clouds));
                break;
            case "04n":
                temperatureImage.setImageResource(R.drawable.cloudy_weather);
                weather.setText(getString(R.string.broken_clouds));
                break;
            case "09d":
                temperatureImage.setImageResource(R.drawable.rainy_day);
                weather.setText(getString(R.string.shower_rain));
                break;
            case "09n":
                temperatureImage.setImageResource(R.drawable.rainy_night);
                weather.setText(getString(R.string.shower_rain));
                break;
            case "10d":
                temperatureImage.setImageResource(R.drawable.rainy_weather);
                weather.setText(getString(R.string.rain));
                break;
            case "10n":
                temperatureImage.setImageResource(R.drawable.rainy_weather);
                weather.setText(getString(R.string.rain));
                break;
            case "11d":
                temperatureImage.setImageResource(R.drawable.storm_weather_day);
                weather.setText(getString(R.string.thunderstorm));
                break;
            case "11n":
                temperatureImage.setImageResource(R.drawable.storm_weather_night);
                weather.setText(getString(R.string.thunderstorm));
                break;
            case "13d":
                temperatureImage.setImageResource(R.drawable.snow_day);
                weather.setText(getString(R.string.snow));
                break;
            case "13n":
                temperatureImage.setImageResource(R.drawable.snow_night);
                weather.setText(getString(R.string.snow));
                break;
            case "50d":
                temperatureImage.setImageResource(R.drawable.haze_day);
                weather.setText(getString(R.string.mist));
                break;
            case "50n":
                temperatureImage.setImageResource(R.drawable.haze_night);
                weather.setText(getString(R.string.mist));
                break;

        }
    }
}
