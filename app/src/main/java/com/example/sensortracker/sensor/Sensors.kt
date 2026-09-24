package com.example.sensortracker.sensor

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor

class AccelerometerSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorName = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_ACCELEROMETER
)

class GyroscopeSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorName = PackageManager.FEATURE_SENSOR_GYROSCOPE,
    sensorType = Sensor.TYPE_GYROSCOPE
)

class LinearAccelerometerSensor(
    context: Context
): AndroidSensor(
    context = context,
    sensorName = PackageManager.FEATURE_SENSOR_ACCELEROMETER,
    sensorType = Sensor.TYPE_LINEAR_ACCELERATION
)