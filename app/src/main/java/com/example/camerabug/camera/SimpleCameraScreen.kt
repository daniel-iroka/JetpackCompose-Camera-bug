package com.example.camerabug.camera

import android.Manifest
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.camerabug.generateImageUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SimpleCameraScreen() {

    val context = LocalContext.current
    var imageUri by rememberSaveable { mutableStateOf(value = Uri.EMPTY) }
    var capturedUri by remember { mutableStateOf(value = Uri.EMPTY) }

    val imageCaptureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                capturedUri = imageUri
                Toast.makeText(context, "Retrieved URI is $capturedUri!", Toast.LENGTH_LONG).show()
                Log.d("SimpleCameraScreen", "Retrieved URI is $capturedUri")
            }
        }
    )

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            capturedUri = uri
        }
    )

    val cameraPermission = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionResult = { granted ->
            if (granted) {
                imageUri = context.generateImageUri()
                imageCaptureLauncher.launch(imageUri)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        AsyncImage(
            model = if(capturedUri.path?.isNotEmpty() == true) capturedUri else "",
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(percent = 50))
                .border(
                    width = 1.dp,
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(percent = 50)
                ),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = {
            cameraPermission.launchPermissionRequest()
        }) {
            Text(text = "Take Photo")
        }
        Spacer(modifier = Modifier.height(6.dp))
        Button(onClick = {
            imagePickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text(text = "Pick from gallery")
        }
        Spacer(modifier = Modifier.height(64.dp))
    }
}
