package com.gritacademy.api_v5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.concurrent.thread

class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        val cityText = view.findViewById<TextView>(R.id.cityText)
        val weatherText = view.findViewById<TextView>(R.id.weatherText)
        val historyButton = view.findViewById<Button>(R.id.historyButton)

        val city = arguments?.getString("city") ?: "Okänd stad"
        cityText.text = city
        weatherText.text = "Hämtar väder..."

        thread {
            try {
                val encodedCity = URLEncoder.encode(city, "UTF-8")

                val locationUrl = URL(
                    "https://geocoding-api.open-meteo.com/v1/search?name=$encodedCity&count=1&language=sv&format=json"
                )

                val locationConnection = locationUrl.openConnection() as HttpURLConnection
                val locationData = locationConnection.inputStream.bufferedReader().readText()
                locationConnection.disconnect()

                val locationJson = JSONObject(locationData)
                val results = locationJson.optJSONArray("results")

                if (results == null || results.length() == 0) {
                    requireActivity().runOnUiThread {
                        weatherText.text = "Staden hittades inte."
                    }
                    return@thread
                }

                val location = results.getJSONObject(0)
                val latitude = location.getDouble("latitude")
                val longitude = location.getDouble("longitude")

                val weatherUrl = URL(
                    "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&current=temperature_2m,wind_speed_10m,weather_code"
                )

                val weatherConnection = weatherUrl.openConnection() as HttpURLConnection
                val weatherData = weatherConnection.inputStream.bufferedReader().readText()
                weatherConnection.disconnect()

                val weatherJson = JSONObject(weatherData)
                val current = weatherJson.getJSONObject("current")

                val temperature = current.getDouble("temperature_2m")
                val wind = current.getDouble("wind_speed_10m")

                requireActivity().runOnUiThread {
                    weatherText.text =
                        "Temperatur: $temperature °C\nVind: $wind km/h"
                }

            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    weatherText.text = "Kunde inte hämta vädret."
                }
            }
        }

        historyButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}