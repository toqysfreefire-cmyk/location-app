package com.example.virtualcontainer.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.virtualcontainer.R
import com.example.virtualcontainer.hook.FakeLocationData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etLat = findViewById<EditText>(R.id.etLatitude)
        val etLon = findViewById<EditText>(R.id.etLongitude)
        val btnSave = findViewById<Button>(R.id.btnSaveLocation)

        // বর্তমান ফেক লোকেশন সেট করা
        etLat.setText(FakeLocationData.latitude.toString())
        etLon.setText(FakeLocationData.longitude.toString())

        btnSave.setOnClickListener {
            val lat = etLat.text.toString().toDoubleOrNull()
            val lon = etLon.text.toString().toDoubleOrNull()

            if (lat != null && lon != null) {
                FakeLocationData.latitude = lat
                FakeLocationData.longitude = lon
                Toast.makeText(this, "فেক লোকেশন সফলভাবে আপডেট করা হয়েছে!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "সঠিক কোঅর্ডিনেট লিখুন", Toast.LENGTH_SHORT).show()
            }
        }
    }
}