package com.example.virtualcontainer.utils

import java.lang.reflect.Field
import java.lang.reflect.Method

object ReflectionUtils {

    fun getMethod(cls: Class<*>, vararg methodName: String): Method? {
        for (method in cls.declaredMethods) {
            for (name in methodName) {
                if (method.name == name) {
                    method.isAccessible = true
                    return method
                }
            }
        }
        return cls.superclass?.let { getMethod(it, *methodName) }
    }

    fun getField(cls: Class<*>, fieldName: String): Field? {
        try {
            val field = cls.getDeclaredField(fieldName)
            field.isAccessible = true
            return field
        } catch (e: NoSuchFieldException) {
            return cls.superclass?.let { getField(it, fieldName) }
        }
    }
}