package weather.rennanfelizardo.com.br.weather;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import weather.rennanfelizardo.com.br.weather.models.City;
import weather.rennanfelizardo.com.br.weather.services.OpenWeatherService;

public class MainActivity extends AppCompatActivity {

    private TextView    cityView;
    private TextView    temperatureView;
    private TextView    humidityView;
    private Button      button;
    private TextInputEditText cityInput;
    private ImageView   temperatureImage;
    private TextView    weather;

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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String cityName = cityInput.getText().toString();

                if (validateCityName()){
                    OpenWeatherService service = OpenWeatherService.retrofit.create(OpenWeatherService.class);

                    Call<City> call = service.getCityByName(cityName, "metric", OpenWeatherService.API_KEY);

                    call.enqueue(new Callback<City>() {
                        @Override
                        public void onResponse(Call<City> call, Response<City> response) {
                            cityView.setText(getString(R.string.city) + ": " + cityName);
                            temperatureView.setText(getString(R.string.temperature) + ": "  + String.format("%s", response.body().getMain().getTemp().toString()) + "ºC");
                            humidityView.setText(getString(R.string.humidity) + ": "  + response.body().getMain().getHumidity() + "%");

                            cityView.setVisibility(View.VISIBLE);
                            temperatureView.setVisibility(View.VISIBLE);
                            humidityView.setVisibility(View.VISIBLE);

                            setIconAndWeather(response.body().getWeather().get(0).getIcon());
                            temperatureImage.setVisibility(View.VISIBLE);
                            weather.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<City> call, Throwable t) {
                            Log.e("Failure", t.getMessage());
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
                temperatureImage.setImageResource(R.drawable.ic01d);
                weather.setText(getString(R.string.clear_sky));
                break;
            case "01n":
                temperatureImage.setImageResource(R.drawable.ic01n);
                weather.setText(getString(R.string.clear_sky));
                break;
            case "02d":
                temperatureImage.setImageResource(R.drawable.ic02d);
                weather.setText(getString(R.string.few_clouds));
                break;
            case "02n":
                temperatureImage.setImageResource(R.drawable.ic02n);
                weather.setText(getString(R.string.few_clouds));
                break;
            case "03d":
                temperatureImage.setImageResource(R.drawable.ic03d);
                weather.setText(getString(R.string.scattered_clouds));
                break;
            case "03n":
                temperatureImage.setImageResource(R.drawable.ic03n);
                weather.setText(getString(R.string.scattered_clouds));
                break;
            case "04d":
                temperatureImage.setImageResource(R.drawable.ic04d);
                weather.setText(getString(R.string.broken_clouds));
                break;
            case "04n":
                temperatureImage.setImageResource(R.drawable.ic04n);
                weather.setText(getString(R.string.broken_clouds));
                break;
            case "09d":
                temperatureImage.setImageResource(R.drawable.ic09d);
                weather.setText(getString(R.string.shower_rain));
                break;
            case "09n":
                temperatureImage.setImageResource(R.drawable.ic09n);
                weather.setText(getString(R.string.shower_rain));
                break;
            case "10d":
                temperatureImage.setImageResource(R.drawable.ic10d);
                weather.setText(getString(R.string.rain));
                break;
            case "10n":
                temperatureImage.setImageResource(R.drawable.ic10n);
                weather.setText(getString(R.string.rain));
                break;
            case "11d":
                temperatureImage.setImageResource(R.drawable.ic11d);
                weather.setText(getString(R.string.thunderstorm));
                break;
            case "11n":
                temperatureImage.setImageResource(R.drawable.ic11n);
                weather.setText(getString(R.string.thunderstorm));
                break;
            case "13d":
                temperatureImage.setImageResource(R.drawable.ic13d);
                weather.setText(getString(R.string.snow));
                break;
            case "13n":
                temperatureImage.setImageResource(R.drawable.ic13n);
                weather.setText(getString(R.string.snow));
                break;
            case "50d":
                temperatureImage.setImageResource(R.drawable.ic50d);
                weather.setText(getString(R.string.mist));
                break;
            case "50n":
                temperatureImage.setImageResource(R.drawable.ic50n);
                weather.setText(getString(R.string.mist));
                break;

        }
    }
}
