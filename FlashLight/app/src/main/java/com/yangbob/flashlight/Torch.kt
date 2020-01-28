package com.yangbob.flashlight

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log

class Torch(context: Context)
{
    private var cameraId: String? = null
    private var cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    init
    {
        cameraId = getCameraId()
    }

    fun flashOn()
    {
        Log.i("TEST", "Torch.flashOn()")
        cameraId?.let {
            Log.i("TEST", "Torch.flashOn().setTorch!")
            cameraManager.setTorchMode(it, true)
        }
    }

    fun flashOff()
    {
        Log.i("TEST", "Torch.flashOff()")
        cameraId?.let {
            Log.i("TEST", "Torch.flashOff().setTorch!")
            cameraManager.setTorchMode(it, false)
        }
    }

    private fun getCameraId(): String?
    {
        val cameraId = cameraManager.cameraIdList
        for(id in cameraId)
        {
            val info = cameraManager.getCameraCharacteristics(id)
            val flashAvailable = info.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            val lensFacing = info.get(CameraCharacteristics.LENS_FACING)
            if(flashAvailable != null && flashAvailable && lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_BACK) return id
        }
        return null
    }
}