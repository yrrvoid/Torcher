package com.example.torchlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private lateinit var cameraManager: CameraManager
    private var cameraId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraManager =
            getSystemService(Context.CAMERA_SERVICE) as CameraManager

        cameraId = findFlashCamera()

        setContent {
            TorchApp(
                onTorchChanged = { enabled ->
                    setTorch(enabled)
                }
            )
        }
    }

    private fun findFlashCamera(): String? {
        return try {
            cameraManager.cameraIdList.firstOrNull { id ->
                val characteristics =
                    cameraManager.getCameraCharacteristics(id)

                characteristics.get(
                    CameraCharacteristics.FLASH_INFO_AVAILABLE
                ) == true
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun setTorch(enabled: Boolean) {
        try {
            cameraId?.let { id ->
                cameraManager.setTorchMode(id, enabled)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        setTorch(false)
        super.onDestroy()
    }
}

@Composable
fun TorchApp(
    onTorchChanged: (Boolean) -> Unit
) {
    var torchOn by remember {
        mutableStateOf(false)
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "🔦 Torch Light",
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(
                    modifier = Modifier.height(25.dp)
                )

                Text(
                    text =
