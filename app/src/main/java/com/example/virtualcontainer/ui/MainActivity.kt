package com.example.virtualcontainer.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.virtualcontainer.R
import com.example.virtualcontainer.hook.FakeLocationData

class MainActivity : AppCompatActivity() {

    private lateinit var mapWebView: WebView
    private lateinit var etLat: EditText
    private lateinit var etLon: EditText
    private lateinit var tvCurrentCoords: TextView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLat = findViewById(R.id.etLatitude)
        etLon = findViewById(R.id.etLongitude)
        tvCurrentCoords = findViewById(R.id.tvCurrentCoords)
        mapWebView = findViewById(R.id.mapWebView)
        val btnSave = findViewById<Button>(R.id.btnSaveLocation)

        // বর্তমান ফেক লোকেশন ডিসপ্লেতে সেট করা
        updateDisplayCoords(FakeLocationData.latitude, FakeLocationData.longitude)

        // WebView ও ম্যাপ কনফিগারেশন
        mapWebView.settings.javaScriptEnabled = true
        mapWebView.settings.domStorageEnabled = true
        mapWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // ম্যাপ লোড শেষে প্রারম্ভিক লোকেশন সেট করা
                mapWebView.evaluateJavascript(
                    "if (typeof setCenter === 'function') { setCenter(${FakeLocationData.latitude}, ${FakeLocationData.longitude}); }",
                    null
                )
            }
        }

        // JavaScript Interface ব্রিজ সংযোগ
        mapWebView.addJavascriptInterface(WebAppInterface(), "AndroidBridge")
        mapWebView.loadUrl("file:///android_asset/map.html")

        // ম্যানুয়াল সেভ বাটন
        btnSave.setOnClickListener {
            val lat = etLat.text.toString().toDoubleOrNull()
            val lon = etLon.text.toString().toDoubleOrNull()

            if (lat != null && lon != null) {
                FakeLocationData.latitude = lat
                FakeLocationData.longitude = lon
                updateDisplayCoords(lat, lon)
                mapWebView.evaluateJavascript(
                    "if (typeof setCenter === 'function') { setCenter($lat, $lon); }",
                    null
                )
                Toast.makeText(this, "ফেক লোকেশন সফলভাবে সেট করা হয়েছে!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "সঠিক কোঅর্ডিনেট লিখুন", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateDisplayCoords(lat: Double, lon: Double) {
        runOnUiThread {
            etLat.setText(lat.toString())
            etLon.setText(lon.toString())
            tvCurrentCoords.text = "Selected: Lat: $lat, Lon: $lon"
        }
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun onLocationSelected(lat: Double, lon: Double) {
            runOnUiThread {
                FakeLocationData.latitude = lat
                FakeLocationData.longitude = lon
                updateDisplayCoords(lat, lon)
                Toast.makeText(this@MainActivity, "ম্যাপ থেকে লোকেশন সিলেক্ট করা হয়েছে!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
