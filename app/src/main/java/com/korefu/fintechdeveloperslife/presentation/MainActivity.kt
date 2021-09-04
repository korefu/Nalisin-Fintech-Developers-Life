package com.korefu.fintechdeveloperslife.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.korefu.fintechdeveloperslife.R
import com.korefu.fintechdeveloperslife.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val LATEST_TAB_INDEX = 0
        const val TOP_TAB_INDEX = 1
        const val HOT_TAB_INDEX = 2
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewPager.adapter = MainActivityPagerAdapter(this)
        setContentView(binding.root)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                LATEST_TAB_INDEX -> getString(R.string.tab_latest)
                TOP_TAB_INDEX -> getString(R.string.tab_top)
                HOT_TAB_INDEX -> getString(R.string.tab_hot)
                else -> ""
            }
        }.attach()
    }
}
