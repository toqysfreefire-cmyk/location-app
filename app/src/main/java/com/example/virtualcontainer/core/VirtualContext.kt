package com.example.virtualcontainer.core

import android.content.Context
import android.content.ContextWrapper
import android.location.LocationManager
import com.example.virtualcontainer.hook.LocationHook

class VirtualContext(base: Context) : ContextWrapper(base) {

    override fun getSystemService(name: String): Any? {
        if (Context.LOCATION_SERVICE == name) {
            val realManager = super.getSystemService(name)
            if (realManager != null) {
                // LocationManager কল হলে আমাদের হুক করা অবজেক্ট রিটার্ন করবে
                return LocationHook.hookLocationManager(realManager)
            }
        }
        return super.getSystemService(name)
    }
}