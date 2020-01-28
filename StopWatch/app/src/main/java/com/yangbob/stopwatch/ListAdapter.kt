package com.yangbob.stopwatch

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.yangbob.stopwatch.databinding.ItemLaptimeBinding

class ListAdapter(private val dataList: List<LapData>) : RecyclerView.Adapter<ListAdapter.MyViewHolder>()
{
    inner class MyViewHolder(private val binding: ItemLaptimeBinding): RecyclerView.ViewHolder(binding.root), LifecycleOwner
    {
        val lifecycleRegistry = LifecycleRegistry(this)
        init
        {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }
        override fun getLifecycle(): Lifecycle {
            Log.i("TEST", "${this.hashCode()} ViewHolder's getLifeCycle() : ${lifecycleRegistry.currentState}")
            return lifecycleRegistry
        }
        
        fun bind(lapData: LapData)
        {
            binding.lapdata = lapData
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val binding = DataBindingUtil.inflate<ItemLaptimeBinding>(LayoutInflater.from(parent.context), R.layout.item_laptime, parent,false)
        val viewholder = MyViewHolder(binding)
        viewholder.lifecycleRegistry.currentState = Lifecycle.State.CREATED
        binding.lifecycleOwner = viewholder
        Log.i("TEST", "${viewholder.hashCode()} onCreateViewHolder")
        return viewholder
    }
    
    override fun getItemCount(): Int = dataList.size
    
    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        Log.i("TEST", "${holder.hashCode()} onBindViewHolder")
        holder.bind(dataList[position])
    }
    
    override fun onViewAttachedToWindow(holder: MyViewHolder)
    {
        Log.i("TEST", "${holder.hashCode()} onViewAttachedToWindow")
        super.onViewAttachedToWindow(holder)
        holder.lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }
    
    override fun onViewDetachedFromWindow(holder: MyViewHolder)
    {
        Log.i("TEST", "${holder.hashCode()} onViewDetachedToWindow")
        super.onViewDetachedFromWindow(holder)
        holder.lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }
}