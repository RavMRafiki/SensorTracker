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
import android.provider.MediaStore
import android.content.ContentValues

interface SensorStorageRepository {
    suspend fun saveRecordsToCsv(records: List<SensorRecord>): Result<String>
}

class CsvSensorStorageRepository(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SensorStorageRepository {

    override suspend fun saveRecordsToCsv(records: List<SensorRecord>): Result<String> =
        runCatching {
            val fileName = "records_${System.currentTimeMillis()}.csv"

            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            // 1. Insert into public Downloads
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
            ) ?: error("Failed to create file in Downloads")

            // 2. Stream the CSV lines
            context.contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { writer ->
                writer.appendLine("timestamp,sensor,x,y,z") // Header
                records.forEach { record ->
                    writer.appendLine("${record.timestamp},${record.sensorType},${record.x},${record.y},${record.z}")
                }
            } ?: error("Failed to open output stream")

            "Downloads/$fileName"
        }
}
