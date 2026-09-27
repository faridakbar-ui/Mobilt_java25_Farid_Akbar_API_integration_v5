package com.gritacademy.api_v5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        val cityText = view.findViewById<TextView>(R.id.cityText)
        val historyButton = view.findViewById<Button>(R.id.historyButton)

        val city = arguments?.getString("city") ?: "Okänd stad"
        cityText.text = city

        historyButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HistoryFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}