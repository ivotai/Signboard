package com.unicorn.signboard.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.unicorn.signboard.input.building.AddBuildingFra
import com.unicorn.signboard.input.ground.AddGroundFra
import com.unicorn.signboard.list.merchant.MerchantListFra

class ListPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MerchantListFra()
            1 -> AddBuildingFra()
            2 -> AddGroundFra()
            else -> null!!
        }
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int) = listOf("墙面", "建筑", "地面")[position]

}