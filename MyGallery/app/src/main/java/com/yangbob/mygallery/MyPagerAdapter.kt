package com.yangbob.mygallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MyPagerAdapter(fm: FragmentManager, behavior: Int) : FragmentStatePagerAdapter(fm, behavior)
{
    // 뷰페이저가 표시할 프래그먼트 목록
    private val items = ArrayList<Fragment>()

    // position 위치의 프래그먼트 리턴
    override fun getItem(position: Int): Fragment
    {
        return items[position]
    }

    // 아이템 개수 리턴
    override fun getCount(): Int
    {
        return items.size
    }

    // 아이템 갱신 (외부에서 프래그먼트 목록 추가)
    fun updateFragments(items : List<Fragment>)
    {
        this.items.addAll(items)
    }
}