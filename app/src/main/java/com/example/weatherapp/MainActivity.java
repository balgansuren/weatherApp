package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etCity;
    TextView tvResult, tvCity, tvDesc, tvTemp, tvHumidity, tvWindSpeed, tvRainper,
            tvDesc1, tvDesc2, tvDesc3, tvDesc4, tvTemp1, tvTemp2, tvTemp3, tvTemp4;
    ImageView tvIcon1, tvIcon2, tvIcon3, tvIcon4, imMain_image;
    private final String url = "https://api.openweathermap.org/data/2.5/forecast";
    private final String appid = "def6e71651bad579f8ce09c872e2ad24";
    DecimalFormat df = new DecimalFormat("#.#");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        tvCity = findViewById(R.id.tvCity);
        tvDesc = findViewById(R.id.tvDesc);
        tvTemp = findViewById(R.id.tvTemp);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvRainper = findViewById(R.id.tvRainper);
        tvWindSpeed = findViewById(R.id.tvWindSpeed);

        tvDesc1 = findViewById(R.id.tvDescDay1);
        tvTemp1 = findViewById(R.id.tvTempDay1);
        tvIcon1 = findViewById(R.id.tvIconDay1);

        tvDesc2 = findViewById(R.id.tvDescDay2);
        tvTemp2 = findViewById(R.id.tvTempDay2);
        tvIcon2 = findViewById(R.id.tvIconDay2);

        tvDesc3 = findViewById(R.id.tvDescDay3);
        tvTemp3 = findViewById(R.id.tvTempDay3);
        tvIcon3 = findViewById(R.id.tvIconDay3);

        tvDesc4 = findViewById(R.id.tvDescDay4);
        tvTemp4 = findViewById(R.id.tvTempDay4);
        tvIcon4 = findViewById(R.id.tvIconDay4);

        imMain_image = findViewById(R.id.Main_image);
        //tvResult = findViewById(R.id.tvResult);
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
        if (city.equals("")) {
            tvResult.setText("Хотын нэр оруулна уу.");
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + appid;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        JSONArray jsonArray = jsonResponse.getJSONArray("list");

                        JSONObject firstForecastItem = jsonArray.getJSONObject(0);

                        String description = firstForecastItem.getJSONArray("weather").getJSONObject(0).getString("description");
                        double temp = firstForecastItem.getJSONObject("main").getDouble("temp") - 273.15;
                        String humidity = Integer.toString(firstForecastItem.getJSONObject("main").getInt("humidity"));
                        String wind = firstForecastItem.getJSONObject("wind").getString("speed");
                        String clouds = Integer.toString(firstForecastItem.getJSONObject("clouds").getInt("all"));
                        String cityName = jsonResponse.getJSONObject("city").getString("name");

                        tvCity.setText(cityName);
                        tvTemp.setText(df.format(temp) + " °C");
                        tvDesc.setText(description);
                        tvHumidity.setText(humidity + "%");
                        tvRainper.setText(clouds + "%");
                        tvWindSpeed.setText(wind + "м/с");
                        setIconImage(description, imMain_image);

                        for (int i = 1; i <= 4; i++) {
                            JSONObject forecast = jsonArray.getJSONObject(i * 8); // udurt 3 3 tsagiin zaitai medeelle solidg

                            JSONObject mainForecast = forecast.getJSONObject("main");
                            JSONArray weatherForecastArray = forecast.getJSONArray("weather");
                            JSONObject weatherForecast = weatherForecastArray.getJSONObject(0);

                            String descriptionD = weatherForecast.getString("description");
                            double tempDay = mainForecast.getDouble("temp") - 273.15;

                            switch (i) {
                                case 1:
                                    tvTemp1.setText(df.format(tempDay) + " °C");
                                    setIconImage(descriptionD, tvIcon1);
                                    tvDesc1.setText(descriptionD);
                                    break;
                                case 2:
                                    tvTemp2.setText(df.format(tempDay) + " °C");
                                    setIconImage(descriptionD, tvIcon2);
                                    tvDesc2.setText(descriptionD);
                                    break;
                                case 3:
                                    tvTemp3.setText(df.format(tempDay) + " °C");
                                    setIconImage(descriptionD, tvIcon3);
                                    tvDesc3.setText(descriptionD);
                                    break;
                                case 4:
                                    tvTemp4.setText(df.format(tempDay) + " °C");
                                    setIconImage(descriptionD, tvIcon4);
                                    tvDesc4.setText(descriptionD);
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    String errorMessage;
                    if (error.networkResponse != null && error.networkResponse.statusCode == 404) {
                        errorMessage = "Хотын нэрээ зөв оруулна уу!";
                    } else {
                        errorMessage = "Error: " + error.toString().trim();
                    }
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    // zurg
    private void setIconImage(String description, ImageView imageView) {
        String iconResourceId = "";

        switch (description.toLowerCase()) {
            case "clear sky":
                iconResourceId = "@drawable/sun";
                break;
            case "few clouds":
                iconResourceId = "@drawable/sun_clouds";
                break;
            case "scattered clouds":
                iconResourceId = "@drawable/shattered_clouds";
                break;
            case "broken clouds":
                iconResourceId = "@drawable/broken_clouds";
                break;
            case "shower rain":
                iconResourceId = "@drawable/sun_rain";
                break;
            case "light rain":
                iconResourceId = "@drawable/light_rain";
                break;
            case "overcast clouds":
                iconResourceId = "@drawable/overcast_clouds";
                break;
            case "light snow":
                iconResourceId = "@drawable/snow";
                break;
            default:
                iconResourceId = "@drawable/d";
                break;
        }

        int resId = getResources().getIdentifier(iconResourceId, "drawable", getPackageName());
        imageView.setImageResource(resId);
    }
}