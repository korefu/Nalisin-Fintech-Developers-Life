package com.korefu.fintechdeveloperslife.presentation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.korefu.fintechdeveloperslife.data.model.SortingType
import com.korefu.fintechdeveloperslife.presentation.memes.MemesFragment

class MainActivityPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val sortingType = when (position) {
            MainActivity.LATEST_TAB_INDEX -> SortingType.LATEST
            MainActivity.TOP_TAB_INDEX -> SortingType.TOP
            MainActivity.HOT_TAB_INDEX -> SortingType.HOT
            else -> throw IndexOutOfBoundsException("Unexpected tab position")
        }
        return MemesFragment.newInstance(sortingType)
    }
}
