package com.gritacademy.api_v5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val cityInput = view.findViewById<EditText>(R.id.cityInput)
        val searchButton = view.findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener {
            val city = cityInput.text.toString().trim()

            if (city.isEmpty()) {
                Toast.makeText(requireContext(), "Skriv en stad", Toast.LENGTH_SHORT).show()
            } else {
                val fragment = WeatherFragment()
                val bundle = Bundle()
                bundle.putString("city", city)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }
}