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

// Constants
private const val P0 = 1013.25f // Sea level standard atmospheric pressure in hPa

// ViewModel to hold and manage UI-related data
class AltimeterViewModel(context: Context) : ViewModel(), SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val pressureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

    private val _altitude = MutableStateFlow(0f)
    val altitude: StateFlow<Float> = _altitude

    private val _pressure = MutableStateFlow(P0)
    val pressure: StateFlow<Float> = _pressure

    init {
        pressureSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_PRESSURE) {
                _pressure.value = it.values[0]
                _altitude.value = calculateAltitude(it.values[0])
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used in this implementation
    }

    private fun calculateAltitude(pressure: Float): Float {
        return 44330f * (1 - (pressure / P0).pow(1 / 5.255f))
    }

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}

// ViewModel Factory
class AltimeterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AltimeterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AltimeterViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
