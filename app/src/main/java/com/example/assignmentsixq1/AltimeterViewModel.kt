package com.example.assignmentsixq1

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.pow

// Sea level standard atmospheric pressure in hPa.
private const val P0 = 1013.25f

/**
 * ViewModel that manages pressure sensor data to calculate altitude.
 * @param context The application context to access system services.
 */
class AltimeterViewModel(context: Context) : ViewModel(), SensorEventListener {
    // Android's sensor service manager.
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    // The device's pressure sensor. Null if not available.
    private val pressureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

    // Internal state for altitude (in meters).
    private val _altitude = MutableStateFlow(0f)
    // Public, read-only state for the UI to observe altitude.
    val altitude: StateFlow<Float> = _altitude

    // Internal state for atmospheric pressure (in hPa).
    private val _pressure = MutableStateFlow(P0)
    // Public, read-only state for the UI to observe pressure.
    val pressure: StateFlow<Float> = _pressure

    // This block runs when the ViewModel is first created.
    init {
        // Register this class as a listener for the pressure sensor.
        pressureSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    /** Called when the sensor detects a new value. */
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            // Ensure the event is from the pressure sensor.
            if (it.sensor.type == Sensor.TYPE_PRESSURE) {
                // Update pressure and recalculate altitude.
                _pressure.value = it.values[0]
                _altitude.value = calculateAltitude(it.values[0])
            }
        }
    }

    /** Called when the sensor's accuracy changes. (Not used here). */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used in this implementation.
    }

    /** Calculates altitude from pressure using the barometric formula. */
    private fun calculateAltitude(pressure: Float): Float {
        return 44330f * (1 - (pressure / P0).pow(1 / 5.255f))
    }

    /** Called when the ViewModel is destroyed. */
    override fun onCleared() {
        super.onCleared()
        // Unregister the listener to save power and prevent memory leaks.
        sensorManager.unregisterListener(this)
    }
}

/**
 * Factory for creating AltimeterViewModel instances with a Context parameter.
 */
class AltimeterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Ensure the factory is used for the correct ViewModel.
        if (modelClass.isAssignableFrom(AltimeterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Create and return an instance of the AltimeterViewModel.
            return AltimeterViewModel(context) as T
        }
        // Throw an error if the factory is used for a different ViewModel.
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
