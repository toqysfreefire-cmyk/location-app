package com.example.virtualcontainer.hook

import android.location.LocationListener
import android.os.Handler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object LocationHook {

    // সিস্টেমের LocationManager-এর কল হুক করার জন্য ডায়নামিক প্রক্সি
    fun hookLocationManager(originalManagerObject: Any): Any {
        val interfaceClass = Class.forName("android.location.ILocationManager")
        
        return Proxy.newProxyInstance(
            interfaceClass.classLoader,
            arrayOf(interfaceClass)
        ) { _, method, args ->
            if (!FakeLocationData.isSpoofingEnabled) {
                return@NewProxyInstance method.invoke(originalManagerObject, *(args ?: arrayOf()))
            }

            // যখনই অ্যাপ getLastKnownLocation বা similar মেথড কল করবে
            when (method.name) {
                "getLastLocation", "getLastKnownLocation" -> {
                    // আসল লোকেশনের বদলে ফেক লোকেশন রিটার্ন করুন
                    return@NewProxyInstance FakeLocationData.getMockLocation()
                }
                "requestLocationUpdates" -> {
                    // যদি অ্যাপ লাইভ লোকেশন আপডেট চায়, তবে ফেক লোকেশন পুশ করুন
                    for (arg in args!!) {
                        if (arg is LocationListener) {
                            Handler().post {
                                arg.onLocationChanged(FakeLocationData.getMockLocation())
                            }
                        }
                    }
                    return@NewProxyInstance true
                }
                else -> {
                    return@NewProxyInstance method.invoke(originalManagerObject, *(args ?: arrayOf()))
                }
            }
        }
    }
}