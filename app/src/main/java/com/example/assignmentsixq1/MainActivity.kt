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

// Sea level pressure constant used for the simulation button
private const val P0 = 1013.25f

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AssignmentSixQ1Theme {
                // Correctly instantiating the ViewModel using the Factory for the main app
                val viewModel: AltimeterViewModel = viewModel(
                    factory = AltimeterViewModelFactory(LocalContext.current.applicationContext)
                )
                AltimeterScreen(viewModel)
            }
        }
    }
}

@Composable
fun AltimeterScreen(viewModel: AltimeterViewModel) {
    val altitude by viewModel.altitude.collectAsState()
    val pressure by viewModel.pressure.collectAsState()

    // Assuming a max altitude of 8848m (Everest) for color interpolation
    val maxAltitude = 8848f
    val colorFraction = (altitude / maxAltitude).coerceIn(0f, 1f)
    val backgroundColor = lerp(MaterialTheme.colorScheme.surface, Color.Black, colorFraction)

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val textColor = if (colorFraction > 0.5) Color.White else Color.Black

            Text(
                text = "Altimeter",
                fontSize = 32.sp,
                color = textColor
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Altitude: %.2f meters".format(altitude),
                fontSize = 25.sp,
                color = textColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Pressure: %.2f hPa".format(pressure),
                fontSize = 25.sp,
                color = textColor
            )
        }
    }
}

