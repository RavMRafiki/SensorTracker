# SensorTracker

![Android](https://img.shields.io/badge/Platform-Android-green?logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple?logo=kotlin)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-blue?logo=jetpackcompose)
![Min SDK](https://img.shields.io/badge/Min%20SDK-33-orange)
![Target SDK](https://img.shields.io/badge/Target%20SDK-36-brightgreen)

**SensorTracker** is an Android application built with **Jetpack Compose** and modern Android architecture components. It monitors, records, and exports real-time sensor data from mobile device hardware sensors—including Accelerometer, Gyroscope, and Linear Acceleration—at high sampling rates.

---

## 📱 Features

- **Real-Time Sensor Monitoring**: Displays real-time 3-axis ($X, Y, Z$) telemetry for:
  - **Accelerometer**: Measures acceleration force applied to the device across $X, Y, Z$ axes (including gravity).
  - **Gyroscope**: Measures rotation rate around the device's $X, Y, Z$ axes.
  - **Linear Accelerometer**: Measures acceleration force excluding gravity.
- **High-Frequency Sampling**: Requests maximum sampling frequency (`SENSOR_DELAY_FASTEST`) utilizing the `HIGH_SAMPLING_RATE_SENSORS` permission.
- **Recording Controls**: Start and pause recording sessions dynamically without interrupting real-time preview.
- **CSV Data Export**: Saves buffered telemetry streams into timestamped `.csv` files inside the app's external documents directory.
- **Clean Architecture & Hardware Abstractions**: Decoupled hardware sensor listening using Kotlin Channels, Coroutines, and an extensible `MeasurableSensor` abstraction.

---

## 🛠 Tech Stack & Architecture

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel) + Unidirectional Data Flow (UDF)
- **Asynchronous Execution**: Kotlin Coroutines & Channels (`Channel.UNLIMITED` with `DROP_OLDEST` buffer handling)
- **Lifecycle Management**: `AndroidX Lifecycle ViewModel`
- **Android SDK**:
  - `minSdk`: 33 (Android 13)
  - `targetSdk`: 36 (Android 16)

---

## 📂 Project Structure

```
com.example.sensortracker/
├── MainActivity.kt         # Entry activity & Compose UI rendering (SensorScreen, SensorDisplay)
├── MainViewModel.kt        # State management, recording pipeline, and CSV file generation
├── sensor/
│   ├── MeasurableSensor.kt # Abstract base class for sensor wrappers
│   ├── AndroidSensor.kt    # Generic Android SensorEventListener wrapper
│   ├── Sensors.kt          # Concrete implementations (Accelerometer, Gyroscope, LinearAccelerometer)
│   ├── SensorData.kt       # Data class representing instantaneous (x, y, z) state
│   └── SensorRecord.kt     # Data class representing recorded sensor sample with timestamp
└── ui/theme/               # Jetpack Compose Material 3 theme definitions
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** (Ladybug or newer recommended)
- **Android SDK**: API 33+ (Device or Emulator with sensor simulation support)
- **JDK**: Version 11 or higher

### Build & Run

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/SensorTracker.git
   cd SensorTracker
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle assets and ensure proper SDK dependencies are installed.
4. Select an Android device or virtual device (API 33+) and run:
   ```bash
   ./gradlew installDebug
   ```

---

## 📖 How to Use

1. **View Telemetry**: Launch the app to observe live sensor streams for Accelerometer, Gyroscope, and Linear Acceleration.
2. **Start Recording**: Tap the **Start** button to begin storing sensor events.
3. **Pause Recording**: Tap **Pause** to temporarily hold data collection.
4. **Save CSV**: Tap **Save CSV** to output all recorded samples into a `.csv` file. A toast notification will display the saved file path:
   ```
   Android/data/com.example.sensortracker/files/Documents/sensor_data_YYYYMMDD_HHmmss.csv
   ```

---

## 📄 Exported CSV Format

Exported `.csv` files follow this structure:

```csv
timestamp,sensor,x,y,z
1700000000000000000,Accelerometer,-0.12,9.81,0.45
1700000000050000000,Gyroscope,0.01,-0.02,0.00
1700000000100000000,LinearAccelerometer,0.00,0.05,-0.01
```

- **`timestamp`**: Hardware event timestamp in nanoseconds.
- **`sensor`**: Name of the reporting sensor (`Accelerometer`, `Gyroscope`, `LinearAccelerometer`).
- **`x, y, z`**: Sensor reading values along each axis.

---

## 🛡 Permissions

The app declares the following system permission in `AndroidManifest.xml`:
- `android.permission.HIGH_SAMPLING_RATE_SENSORS`: Required on Android 12+ (API level 31+) to request sensor updates at rates higher than 200 Hz.

---

## 📄 License

This project is released under the [MIT License](LICENSE).
