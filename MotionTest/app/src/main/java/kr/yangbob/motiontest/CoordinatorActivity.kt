package kr.yangbob.motiontest

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_coordinator.*

class CoordinatorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)

        if( resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            coordinatorLayout.loadLayoutDescription(R.xml.scene_no)
        }

        imageLayout.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageLayout.adapter = ImageAdapter()


        linear.setOnClickListener {
            edit2.requestFocus()
            Toast.makeText(this, "click layout", Toast.LENGTH_LONG).show()
        }
    }



}

class ImageAdapter: RecyclerView.Adapter<ImageViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = 10
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
    }
}

class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view){
    init {
        view.findViewById<ImageButton>(R.id.fab).setOnClickListener {
            Log.i("TEST","CLICK FAB")
        }
    }
}
