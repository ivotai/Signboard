package com.unicorn.signboard.input

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.unicorn.signboard.merchant.add.AddMerchantFra

class InputPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return AddMerchantFra()
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int) = listOf("墙面", "建筑", "地面")[position]

}