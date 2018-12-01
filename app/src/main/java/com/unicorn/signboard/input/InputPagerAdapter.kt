package com.unicorn.signboard.input

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.unicorn.signboard.input.ground.AddGroundFra
import com.unicorn.signboard.merchant.add.AddMerchantFra

class InputPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AddMerchantFra()
            1 -> AddMerchantFra()
            2 -> AddGroundFra()
            else -> null!!
        }
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int) = listOf("墙面", "建筑", "地面")[position]

}