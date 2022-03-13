package com.world.edvora_assignment.Util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class Pager_Adapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager)
{

    var fragmentList  = ArrayList<Fragment>()
    var titleList = ArrayList<String>()
    override fun getItem(position: Int): Fragment {
        return fragmentList.get(position)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList.get(position)
    }


    fun addFragment(fragment : Fragment, title : String)
    {
        fragmentList.add(fragment)
        titleList.add(title)
    }

}


