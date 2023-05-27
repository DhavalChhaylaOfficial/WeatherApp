package com.example.weatherapp;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText cityInput;
    private Button fetchButton;
    private ProgressBar progressBar;
    private TextView weatherText;
    private ImageView weatherIcon;
    private SharedPreferences sharedPreferences;
    private static final String API_KEY = "efddeb09d94f05c40fad8b660300a185";
    private static final String PREFS_NAME = "WeatherPrefs";
    private static final String PREF_LAST_SEARCHED_CITY = "lastSearchedCity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        cityInput = findViewById(R.id.editText_city);
        fetchButton = findViewById(R.id.button_fetch);
        progressBar = findViewById(R.id.progress_bar);
        weatherText = findViewById(R.id.text_weather);
        weatherIcon = findViewById(R.id.image_weather);

        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityInput.getText().toString().trim();
                if (!city.isEmpty()) {
                    fetchWeatherData(city);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Retrieve the last searched city and display its weather information
        String lastSearchedCity = sharedPreferences.getString(PREF_LAST_SEARCHED_CITY, "");
        if (!lastSearchedCity.isEmpty()) {
            cityInput.setText(lastSearchedCity);
            fetchWeatherData(lastSearchedCity);
        }
    }

    private void fetchWeatherData(String city) {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;

        progressBar.setVisibility(View.VISIBLE);
        weatherText.setVisibility(View.GONE);
        weatherIcon.setVisibility(View.GONE);

        new WeatherTask().execute(apiUrl);
    }

    private class WeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                connection.disconnect();

                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            progressBar.setVisibility(View.GONE);
            weatherText.setVisibility(View.VISIBLE);
            weatherIcon.setVisibility(View.VISIBLE);

            if (jsonResponse != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonResponse);

                    // Extract weather information from the JSON response
                    String weatherDescription = jsonObject.getJSONArray("weather")
                            .getJSONObject(0).getString("description");
                    double temperature = jsonObject.getJSONObject("main").getDouble("temp");
                    double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
                    double windSpeed = jsonObject.getJSONObject("wind").getDouble("speed");

                    // Convert temperature from Kelvin to Celsius
                    double temperatureCelsius = temperature - 273.15;

                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    String formattedTemperature = decimalFormat.format(temperatureCelsius);

                    // Display weather information on the screen
                    String weatherInfo = "Description: " + weatherDescription
                            + "\nTemperature: " + formattedTemperature + " °C"
                            + "\nHumidity: " + humidity + " %"
                            + "\nWind Speed: " + windSpeed + " m/s";
                    weatherText.setText(weatherInfo);

                    // Retrieve the weather icon code
                    String iconCode = jsonObject.getJSONArray("weather")
                            .getJSONObject(0).getString("icon");

                    // Load and display the weather icon
                    String iconUrl = "https://openweathermap.org/img/w/" + iconCode + ".png";
                    new LoadImageTask().execute(iconUrl);

                    // Save the last searched city
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PREF_LAST_SEARCHED_CITY, cityInput.getText().toString().trim());
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error parsing JSON response", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                // Display the weather icon image
                weatherIcon.setImageBitmap(bitmap);
            }
        }
    }
}