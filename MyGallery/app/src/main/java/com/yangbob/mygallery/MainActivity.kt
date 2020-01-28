package com.yangbob.mygallery

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity()
{

    private val REQUEST_READ_EXTERNAL_STORATE = 1000

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this,
                                              Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        )
        {
            getAllPhotos(this)
        } else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                                                    Manifest.permission.READ_EXTERNAL_STORAGE)
            )
            {
                AlertDialog.Builder(this)
                        .setMessage("사진 정보를 얻으려면 외부 저장소 권한이 필수로 필요합니다.\n~~ 권한이 필요한 이유 ~~")
                        .setNegativeButton("거부") { _, _ -> }
                        .setPositiveButton("허용") { _, _ ->
                            ActivityCompat.requestPermissions(this,
                                                              arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                                              REQUEST_READ_EXTERNAL_STORATE)
                        }.create().show()
            } else
            {
                ActivityCompat.requestPermissions(this,
                                                  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                                  REQUEST_READ_EXTERNAL_STORATE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
        {
            REQUEST_READ_EXTERNAL_STORATE ->
            {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getAllPhotos(this)
                } else
                {
                    Toast.makeText(this, "권한 요청 거부됨", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getAllPhotos(activity: Activity): MutableList<Uri>
    {
        val cursor: Cursor?
        val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder: String = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC"
        val columnIndexID: Int
        val listOfAllImages: MutableList<Uri> = mutableListOf()
        var imageId: Long
        var isDragging = false

        cursor = activity.contentResolver.query(uriExternal, projection, null, null, sortOrder)

        val fragments = ArrayList<Fragment>()
        if (cursor != null)
        {
            columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext())
            {
                imageId = cursor.getLong(columnIndexID)
                val uriImage = Uri.withAppendedPath(uriExternal, "$imageId")
                listOfAllImages.add(uriImage)
//                Log.i("TEST", "uri : ${uriImage}")
                fragments.add(PhotoFragment.newInstance("$uriImage"))
            }
            cursor.close()
        }
        val adapter = MyPagerAdapter(supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        adapter.updateFragments(fragments)
        viewPager.adapter = adapter

        var handler = Handler()
        val millisTime = 3000L
        val handlerTask = object : Runnable {
            override fun run()
            {
                Log.i("TEST", "handlerTask run()")
                runOnUiThread {
                    if(viewPager.currentItem < adapter.count - 1) viewPager.currentItem++
                    else viewPager.currentItem = 0
                }
                handler.postDelayed(this, millisTime)
            }
        }
        handler.postDelayed(handlerTask, millisTime)

        viewPager.addOnPageChangeListener( object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int)
            {
                Log.i("TEST", "state = $state")
                when(state)
                {
                    ViewPager.SCROLL_STATE_IDLE -> {
                        if(isDragging)
                        {
                            handler.postDelayed(handlerTask, millisTime)
                            isDragging = false
                        }
                    }
                    ViewPager.SCROLL_STATE_DRAGGING -> {
                        handler.removeCallbacks(handlerTask)
                        isDragging = true
                    }
                    ViewPager.SCROLL_STATE_SETTLING ->{
                        if(isDragging)
                        {
                            handler.postDelayed(handlerTask, millisTime)
                            isDragging = false
                        }
                    }
                }
            }
            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int)
            {
            }
            override fun onPageSelected(position: Int)
            {
            }
        })

        return listOfAllImages
    }

}
