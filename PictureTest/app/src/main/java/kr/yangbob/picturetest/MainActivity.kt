package kr.yangbob.picturetest

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_GALLERY = 1
    private val REQUEST_CODE_CAMERA = 2
    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

//        MediaStore 에서 get하는 방법 찾기 ! get 할 때 필요한 ID도 !. 그래야 DB에 저장하고 불러올 수도 있지 ~
        // MediaStore에서 썸네일 얻는 방법도 있음. 공식 문서 참조

        image.setImageURI(Uri.parse("content://media/external/images/media/58")) // 이걸로도 되네
        if(image.drawable == null) Log.i("TTTTTTTaaaaaaaaTTTTTT", MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()) // 이걸로 uri로 설정되는지 체크
        Log.i("TTTTTTTTTTTTTTTTTTTT", MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()) // 27 앞까지가 이 내용 끝에 / 없이

        btnGallery.setOnClickListener {
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            startActivityForResult(Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = MediaStore.Images.Media.CONTENT_TYPE
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }, REQUEST_CODE_GALLERY)
        }

        btnCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).let { takePictureIntent ->
                takePictureIntent.resolveActivity(packageManager)?.let { _ ->
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }

                    photoFile?.let {
                        val uri = FileProvider.getUriForFile(
                            this,
                            "$packageName.fileprovider",
                            it
                        )
                        Log.i("TTTTTTTTTTTTTTTTTTTT", "clickCameraBtn() : FileProvider Uri = $uri")
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                        startActivityForResult(takePictureIntent, REQUEST_CODE_CAMERA)
                    }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> {
                    val uri = data?.data
                    Log.i(
                        "TTTTTTTTTTTTTTTTTTTT",
                        "onActivityResult() : From Gallery : intent.Uri = $uri"
                    )
                    Glide.with(this).load(uri).into(image)
                }
                REQUEST_CODE_CAMERA -> {
                    currentPhotoPath?.let {
                        insert(it)
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> {
                    Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show()
                }
                REQUEST_CODE_CAMERA -> {
                    Toast.makeText(this, "사진 찍기 취소", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "IMG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
            Log.i("TTTTTTTTTTTTTTTTTTTT", "CreateImageFile(): currentPhotoPath = $currentPhotoPath")
        }
    }

    private fun insert(filePath: String) {
        val file = File(filePath)
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }
        val item: Uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        Log.i("TTTTTTTTTTTTTTTTTTTT", "insert(): uri = $item")
        contentResolver.openFileDescriptor(item, "w", null)?.use {pfd->
            FileOutputStream(pfd.fileDescriptor).use { fileOutput ->
                BufferedOutputStream(fileOutput).use { bufferedOutput ->
                    FileInputStream(file).use { fileInput->
                        BufferedInputStream(fileInput).use { bufferedInput ->
                            val byteArray = ByteArray(1024)
                            while (bufferedInput.read(byteArray) != -1) {
                                bufferedOutput.write(byteArray)
                            }
                        }
                    }
                }
            }
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(item, values, null, null)
        }
    }

    private fun checkPermission() {
        TedPermission.with(this)
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    Toast.makeText(this@MainActivity, "권한 허용", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    Toast.makeText(this@MainActivity, "권한 거부", Toast.LENGTH_SHORT).show()
                }
            })
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .check()
    }
}

