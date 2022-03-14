package com.world.edvora_assignment.ui.Activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.Toast
import com.google.android.material.tabs.TabLayout
import com.world.edvora_assignment.Util.Pager_Adapter
import com.world.edvora_assignment.databinding.ActivityMainBinding
import com.world.edvora_assignment.ui.Fragment.NearestRidesFragment
import com.world.edvora_assignment.ui.Fragment.PastRidesFragment
import com.world.edvora_assignment.ui.Fragment.UpcomingRidesFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var adapter2: Pager_Adapter
    var binding: ActivityMainBinding? = null
    val bind get() = binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        setUpTabLayout()

    }


    private fun setUpTabLayout() {

        adapter2 = Pager_Adapter(supportFragmentManager)
        adapter2.addFragment(NearestRidesFragment(), "Nearest")
        adapter2.addFragment(UpcomingRidesFragment(), "Upcoming")
        adapter2.addFragment(PastRidesFragment(), "Past")

        binding?.viewPager?.adapter = adapter2
        binding?.standingTabLayout?.setupWithViewPager(binding?.viewPager)


        binding?.standingTabLayout?.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding?.filterLayout?.isClickable = (tab?.position == 0)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

}