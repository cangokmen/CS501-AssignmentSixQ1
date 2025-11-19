package com.example.assignmentsixq1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignmentsixq1.ui.theme.AssignmentSixQ1Theme

// Sea level pressure constant used for altitude calculation.
private const val P0 = 1013.25f

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable drawing content edge-to-edge.
        enableEdgeToEdge()
        setContent {
            AssignmentSixQ1Theme {
                // Instantiate the ViewModel using a factory to pass the application context.
                val viewModel: AltimeterViewModel = viewModel(
                    factory = AltimeterViewModelFactory(LocalContext.current.applicationContext)
                )
                // Set up the main screen Composable.
                AltimeterScreen(viewModel)
            }
        }
    }
}

/**
 * The main screen Composable for the Altimeter application.
 * @param viewModel The ViewModel that provides altitude and pressure data.
 */
@Composable
fun AltimeterScreen(viewModel: AltimeterViewModel) {
    // Collect the altitude and pressure values as state from the ViewModel.
    // The UI will automatically recompose whenever these values change.
    val altitude by viewModel.altitude.collectAsState()
    val pressure by viewModel.pressure.collectAsState()

    // Define a maximum altitude (Mt. Everest) for UI color scaling.
    val maxAltitude = 8848f
    // Calculate a fraction representing the current altitude relative to the max.
    val colorFraction = (altitude / maxAltitude).coerceIn(0f, 1f)
    // Linearly interpolate between the surface color and black based on altitude.
    // The background gets darker as the altitude increases.
    val backgroundColor = lerp(MaterialTheme.colorScheme.surface, Color.Black, colorFraction)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor) // Apply the dynamic background color.
                .padding(innerPadding) // Apply padding for system bars.
                .padding(16.dp), // Apply content padding.
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Dynamically change text color for better readability against the changing background.
            val textColor = if (colorFraction > 0.5) Color.White else Color.Black

            Text(
                text = "Altimeter",
                fontSize = 32.sp,
                color = textColor
            )
            Spacer(modifier = Modifier.height(32.dp))
            // Display the current altitude, formatted to two decimal places.
            Text(
                text = "Altitude: %.2f meters".format(altitude),
                fontSize = 25.sp,
                color = textColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Display the current atmospheric pressure, formatted to two decimal places.
            Text(
                text = "Pressure: %.2f hPa".format(pressure),
                fontSize = 25.sp,
                color = textColor
            )
        }
    }
}
