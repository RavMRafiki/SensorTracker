package com.example.sensortracker.data.sensor

import android.content.Context

class DefaultAndroidSensor(
    context: Context,
    val type: SensorType,
    samplingDelay: SensorSamplingDelay
) : AndroidSensor(
    context = context,
    sensorName = type.sensorName,
    sensorType = type.sensorType,
    samplingDelay = samplingDelay
)

interface SensorFactory {
    fun createSensor(config: SensorConfig): MeasurableSensor?
    fun createActiveSensors(configs: List<SensorConfig>): Map<SensorType, MeasurableSensor>
}

class AndroidSensorFactory(
    private val context: Context
) : SensorFactory {

    override fun createSensor(config: SensorConfig): MeasurableSensor? {
        if (!config.isEnabled) return null
        return DefaultAndroidSensor(
            context = context,
            type = config.type,
            samplingDelay = config.delay
        )
    }

    override fun createActiveSensors(configs: List<SensorConfig>): Map<SensorType, MeasurableSensor> {
        val result = mutableMapOf<SensorType, MeasurableSensor>()
        configs.filter { it.isEnabled }.forEach { config ->
            result[config.type] = DefaultAndroidSensor(
                context = context,
                type = config.type,
                samplingDelay = config.delay
            )
        }
        return result
    }
}
