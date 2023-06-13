package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class MainActivity2 extends AppCompatActivity implements LocationListener {

    private static final String TAG = MainActivity2.class.getSimpleName();
    private static final String API_KEY = "efddeb09d94f05c40fad8b660300a185";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?";

    private LocationManager locationManager;
    private RequestQueue requestQueue;
    private TextView locationTextView;
    private TextView descriptionTextView;
    private TextView temperatureTextView;
    private TextView humidityTextView;
    private TextView windSpeedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        locationTextView = findViewById(R.id.locationTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        humidityTextView = findViewById(R.id.humidityTextView);
        windSpeedTextView = findViewById(R.id.windSpeedTextView);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        requestQueue = Volley.newRequestQueue(this);

        // Request location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void getWeatherData(double latitude, double longitude) {
        String url = API_URL + "lat=" + latitude + "&lon=" + longitude + "&appid=" + API_KEY;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response and update the UI
                            String locationName = response.getString("name");
                            String description = response.getJSONArray("weather")
                                    .getJSONObject(0)
                                    .getString("description");
                            double temperature = response.getJSONObject("main").getDouble("temp");
                            int humidity = response.getJSONObject("main").getInt("humidity");
                            double windSpeed = response.getJSONObject("wind").getDouble("speed");

                            // Update the UI
                            updateUI(locationName, description, temperature, humidity, windSpeed);
                        } catch (JSONException e) {
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "API request error: " + error.getMessage());
                        // Display an error message
                        Toast.makeText(MainActivity2.this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void updateUI(String locationName, String description, double temperature, int humidity, double windSpeed) {
        double temperatureCelsius = temperature - 273.15; // Convert from Kelvin to Celsius

        String temperatureString = "Temperature: "+ String.format(Locale.getDefault(), "%.1f°C", temperatureCelsius);
        String humidityString = getString(R.string.humidity_label, humidity);
        String windSpeedString = getString(R.string.wind_speed_label, windSpeed);

        locationTextView.setText(locationName);
        descriptionTextView.setText(description);
        temperatureTextView.setText(temperatureString);
        humidityTextView.setText(humidityString);
        windSpeedTextView.setText(windSpeedString);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        getWeatherData(latitude, longitude);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {}

    @Override
    public void onProviderDisabled(@NonNull String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, request location updates
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                }
            } else {
                // Permission denied, show a message or handle it accordingly
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}