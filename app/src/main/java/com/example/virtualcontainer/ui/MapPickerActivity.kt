package com.example.virtualcontainer.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.virtualcontainer.R
import com.example.virtualcontainer.hook.FakeLocationData

class MapPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // সিম্পল লেআউট ইনিশিয়ালাইজেশন
        val btnConfirm = Button(this).apply {
            text = "এই লোকেশন সেট করুন (Save Location)"
            setOnClickListener {
                // উদাহরণস্বরূপ ঢাকা বিশ্ববিদ্যালয়ের কোঅর্ডিনেট
                FakeLocationData.latitude = 23.7272
                FakeLocationData.longitude = 90.4032
                Toast.makeText(this@MapPickerActivity, "লোকেশন সফলভাবে সিলেক্ট করা হয়েছে!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        setContentView(btnConfirm)
    }
}