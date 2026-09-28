package com.example.sensortracker.data.repository

import android.content.Context
import android.os.Environment
import com.example.sensortracker.data.model.SensorRecord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface SensorStorageRepository {
    suspend fun saveRecordsToCsv(records: List<SensorRecord>): Result<String>
}

class CsvSensorStorageRepository(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SensorStorageRepository {

    override suspend fun saveRecordsToCsv(records: List<SensorRecord>): Result<String> =
        withContext(ioDispatcher) {
            if (records.isEmpty()) {
                return@withContext Result.failure(IllegalArgumentException("No data to save"))
            }

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "sensor_data_$timestamp.csv"

            try {
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
                FileOutputStream(file).use { fos ->
                    fos.write("timestamp,sensor,x,y,z\n".toByteArray())
                    records.forEach { record ->
                        val line = "${record.timestamp},${record.sensorType},${record.x},${record.y},${record.z}\n"
                        fos.write(line.toByteArray())
                    }
                }
                Result.success(file.absolutePath)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
