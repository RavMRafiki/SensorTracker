package com.example.sensortracker.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

abstract class AndroidSensor(
    private val context: Context,
    private val sensorName: String,
    sensorType: Int,
): MeasurableSensor(sensorType), SensorEventListener {

    override val exists: Boolean
        get() = context.packageManager.hasSystemFeature(sensorName)

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    override fun startListening() {
        if (!exists) {
            return
        }
        if (!::sensorManager.isInitialized && (sensor == null)) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            sensor = sensorManager.getDefaultSensor(sensorType)
        }
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun stopListening() {
        if(!exists || !::sensorManager.isInitialized) {
            return
        }
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(!exists) {
            return
        }
        if(event?.sensor?.type == sensorType) {
            android.util.Log.d("SensorTracker", "Sensor $sensorType reporting: ${event.values[0]}, ${event.values[1]}, ${event.values[2]}")
            onSensorValuesChanged?.invoke(event.values.toList(), event.timestamp)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}
