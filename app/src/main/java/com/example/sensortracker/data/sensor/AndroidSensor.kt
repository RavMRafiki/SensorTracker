package com.example.sensortracker.data.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

abstract class AndroidSensor(
    private val context: Context,
    private val sensorName: String?,
    sensorType: Int,
    private val samplingDelay: SensorSamplingDelay = SensorSamplingDelay.NORMAL
) : MeasurableSensor(sensorType), SensorEventListener {

    override val exists: Boolean
        get() = sensorName == null || context.packageManager.hasSystemFeature(sensorName)

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
            sensorManager.registerListener(this, it, samplingDelay.delayUs)
        }
    }

    override fun stopListening() {
        if (!exists || !::sensorManager.isInitialized) {
            return
        }
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!exists) {
            return
        }
        if (event?.sensor?.type == sensorType) {
            onSensorValuesChanged?.invoke(event.values.toList(), event.timestamp)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}
