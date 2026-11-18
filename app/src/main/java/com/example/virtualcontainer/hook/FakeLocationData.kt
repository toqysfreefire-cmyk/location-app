package com.example.virtualcontainer.hook

import android.location.Location
import android.location.LocationManager
import android.os.SystemClock

object FakeLocationData {
    var latitude: Double = 23.8103  // ডিফল্ট: ঢাকা
    var longitude: Double = 90.4125
    var isSpoofingEnabled: Boolean = true

    fun getMockLocation(): Location {
        val location = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = FakeLocationData.latitude
            longitude = FakeLocationData.longitude
            altitude = 10.0
            accuracy = 3.0f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
        }
        return location
    }
}