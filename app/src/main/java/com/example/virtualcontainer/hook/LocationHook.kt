package com.example.virtualcontainer.hook

import android.location.LocationListener
import android.os.Handler
import android.os.Looper
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object LocationHook {

    // সিস্টেমের LocationManager-এর কল হুক করার জন্য ডায়নামিক প্রক্সি
    fun hookLocationManager(originalManagerObject: Any): Any {
        val interfaceClass = Class.forName("android.location.ILocationManager")
        
        return Proxy.newProxyInstance(
            interfaceClass.classLoader,
            arrayOf(interfaceClass),
            object : InvocationHandler {
                override fun invoke(proxy: Any?, method: Method, args: Array<out Any?>?): Any? {
                    val safeArgs = args?.toList()?.toTypedArray() ?: emptyArray()

                    if (!FakeLocationData.isSpoofingEnabled) {
                        return method.invoke(originalManagerObject, *safeArgs)
                    }

                    return when (method.name) {
                        "getLastLocation", "getLastKnownLocation" -> {
                            FakeLocationData.getMockLocation()
                        }
                        "requestLocationUpdates" -> {
                            if (args != null) {
                                for (arg in args) {
                                    if (arg is LocationListener) {
                                        Handler(Looper.getMainLooper()).post {
                                            arg.onLocationChanged(FakeLocationData.getMockLocation())
                                        }
                                    }
                                }
                            }
                            true
                        }
                        else -> {
                            method.invoke(originalManagerObject, *safeArgs)
                        }
                    }
                }
            }
        )
    }
}
