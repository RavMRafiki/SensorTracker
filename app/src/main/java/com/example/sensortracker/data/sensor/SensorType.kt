package com.example.sensortracker.data.sensor

import android.content.pm.PackageManager
import android.hardware.Sensor

enum class SensorType(val displayName: String, sensorName: String? = null, sensorType: Int) {
    ACCELEROMETER("Accelerometer",
        PackageManager.FEATURE_SENSOR_ACCELEROMETER,
        Sensor.TYPE_ACCELEROMETER
    ),
    GYROSCOPE(
        "Gyroscope",
        PackageManager.FEATURE_SENSOR_GYROSCOPE,
        Sensor.TYPE_GYROSCOPE
    ),
    LINEAR_ACCELERATION("Linear Acceleration",
        PackageManager.FEATURE_SENSOR_ACCELEROMETER,
        Sensor.TYPE_LINEAR_ACCELERATION
    ),
    GRAVITY(
        "Gravity",
        PackageManager.FEATURE_SENSOR_ACCELEROMETER,
        Sensor.TYPE_GRAVITY
    ),
    GEOMAGNETIC_ROTATION_VECTOR(
        "Geomagnetic Rotation Vector",
        PackageManager.FEATURE_SENSOR_COMPASS,
        Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR
    ),
    STEP_DETECTOR(
        "Step Detector",
        PackageManager.FEATURE_SENSOR_STEP_DETECTOR,
        Sensor.TYPE_STEP_DETECTOR
    ),
    MAGNETIC_FIELD(
        "Magnetic Field (Compass)",
        PackageManager.FEATURE_SENSOR_COMPASS,
        Sensor.TYPE_MAGNETIC_FIELD
    ),
    ROTATION_VECTOR(
        "Rotation Vector",
        null,
        Sensor.TYPE_ROTATION_VECTOR
    ),
    PROXIMITY(
        "Proximity",
        PackageManager.FEATURE_SENSOR_PROXIMITY,
        Sensor.TYPE_PROXIMITY
    ),
    LIGHT(
        "Light",
        PackageManager.FEATURE_SENSOR_LIGHT,
        Sensor.TYPE_LIGHT
    ),
    PRESSURE(
        "Barometer (Pressure)",
        PackageManager.FEATURE_SENSOR_BAROMETER,
        Sensor.TYPE_PRESSURE
    ),
    AMBIENT_TEMPERATURE(
        "Ambient Temperature",
        PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE,
        Sensor.TYPE_AMBIENT_TEMPERATURE
    ),
    RELATIVE_HUMIDITY(
        "Relative Humidity",
        PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY,
        Sensor.TYPE_RELATIVE_HUMIDITY
    ),
}