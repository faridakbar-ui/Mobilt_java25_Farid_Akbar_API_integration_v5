package com.gritacademy.api_v5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        val historyText = view.findViewById<TextView>(R.id.historyText)

        val database = FirebaseDatabase.getInstance(
            "https://apiv5weather-default-rtdb.europe-west1.firebasedatabase.app/"
        )

        database.getReference("history")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val history = StringBuilder()

                    for (item in snapshot.children) {
                        val city = item.getValue(String::class.java)
                        if (city != null) {
                            history.append(city).append("\n")
                        }
                    }

                    if (history.isEmpty()) {
                        historyText.text = "Ingen sökhistorik ännu."
                    } else {
                        historyText.text = history.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    historyText.text = "Kunde inte hämta sökhistoriken."
                }
            })

        return view
    }
}