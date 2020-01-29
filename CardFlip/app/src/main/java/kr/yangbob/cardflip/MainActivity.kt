package kr.yangbob.cardflip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import kr.yangbob.cardflip.databinding.ItemPagerBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataList = listOf(
            "문제1" to "정답1",
            "문제2" to "정답2",
            "문제3" to "정답3",
            "문제4" to "정답4",
            "문제5" to "정답5",
            "문제6" to "정답6",
            "문제7" to "정답7"
        )

        viewPager.adapter = ViewPagerAdapter(dataList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
}

class ViewPagerHolder(private val binding: ItemPagerBinding) : RecyclerView.ViewHolder(binding.root) {
    private val textView = binding.textView
    private val card = binding.card
    private val btnAnswer = binding.btnShowAnswer

    private var isFront = true
    private lateinit var data: Pair<String, String>
    private val halfTime: Long = 400
    private val fullTime: Long = 800

    fun bind(data: Pair<String, String>) {
        this.data = data
        binding.string = data.first
        isFront = true

        textView.rotationY = 0f
        card.rotationY = 0f

        btnAnswer.setOnClickListener {
            card.cameraDistance = (10 * binding.card.width).toFloat()
            if(isFront) {
                textView.animate().setDuration(halfTime).alpha(1.0f).withEndAction{
                    binding.string = data.second
                    textView.rotationY = -180f
                }
                card.animate().setDuration(fullTime).rotationY(-180f)
            }
            else {
                textView.animate().setDuration(halfTime).alpha(1.0f).withEndAction{
                    binding.string = data.first
                    textView.rotationY = 0f
                }
                card.animate().setDuration(fullTime).rotationY(0f)
            }
            isFront = !isFront
        }
    }
}

class ViewPagerAdapter(private val dataList: List<Pair<String, String>>) :
    RecyclerView.Adapter<ViewPagerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerHolder {
        val binding: ItemPagerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_pager, parent, false)
        return ViewPagerHolder(binding)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.bind(dataList[position])
    }
}