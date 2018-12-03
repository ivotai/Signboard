package com.unicorn.signboard.list

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.unicorn.signboard.list.building.BuildingListFra
import com.unicorn.signboard.list.ground.GroundListFra
import com.unicorn.signboard.list.merchant.MerchantListFra

class ListPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MerchantListFra()
            1 -> BuildingListFra()
            2 -> GroundListFra()
            else -> null!!
        }
    }

    override fun getCount() = 3

    override fun getPageTitle(position: Int) = listOf("墙面", "建筑", "地面")[position]

}