package com.world.edvora_assignment.ui.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.world.edvora_assignment.Models.RidesRecyclerItem
import com.world.edvora_assignment.Util.RidesAdapter
import com.world.edvora_assignment.databinding.FragmentPastRidesBinding
import com.world.edvora_assignment.ui.ViewModel.UserDataViewModel
import com.world.edvora_assignment.ui.Activities.MainActivity
import com.world.edvora_assignment.ui.ViewModel.RidesViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs


@AndroidEntryPoint
class PastRidesFragment : Fragment() {

    private var _binding: FragmentPastRidesBinding? = null
    private val binding get() = _binding!!
    lateinit var pastRidesList: ArrayList<RidesRecyclerItem>

    private val viewModel: RidesViewModel by viewModels()
    private val viewModel2: UserDataViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Disabling Filter Property on PastRidesFragment
        val mainview = (requireActivity() as MainActivity).bind
        mainview?.filterLayout?.isClickable = false

        _binding = FragmentPastRidesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        pastRidesList = ArrayList()


        //Fetching the list of Past Rides
        setUserData()
    }


    private fun setUserData() {

        viewModel2.get_user_details()

        lifecycleScope.launchWhenStarted {
            viewModel2.dataState.collect { event ->
                when (event) {

                    is UserDataViewModel.MainStateEvent.Success -> {

                        setNearestRides(event.result.stationCode)
                    }
                    is UserDataViewModel.MainStateEvent.Failure -> {

                    }

                    is UserDataViewModel.MainStateEvent.Loading -> {

                    }
                    else -> Unit
                }
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun setNearestRides(customer_station: Int) {

        viewModel.get_rides_details()
        lifecycleScope.launchWhenStarted {
            viewModel.dataState.collect { event ->

                when (event) {

                    is RidesViewModel.MainStateEvent.Success -> {
                        val list = event.result

                        for (it in list) {

                            //Calculating the min distance from the user station code

                            var distance = abs(it.stationPath[0] - customer_station)
                            for (i in it.stationPath) {
                                distance =
                                    Integer.min(distance, abs(customer_station - i))
                            }

                            //Mapping the date to millis and calculatin the difference
                            val df2 = SimpleDateFormat("MM/dd/yyyy hh:mm a")
                            df2.timeZone = TimeZone.getTimeZone("UTC")
                            val current_time = df2.format(Calendar.getInstance().getTime())

                            if (df2.parse(it.date)!!.time - df2.parse(current_time)!!.time < 0) {
                                pastRidesList.add(
                                    RidesRecyclerItem(
                                        it.city,
                                        it.date,
                                        it.destinationStationCode,
                                        it.id,
                                        it.mapUrl,
                                        it.originStationCode,
                                        it.state,
                                        it.stationPath,
                                        distance
                                    )
                                )

                            }
                        }

                        val mainview = (requireActivity() as MainActivity).bind
                        mainview?.standingTabLayout?.getTabAt(2)
                            ?.setText("Past \n (${pastRidesList.size})")


                        //Setting up the recycler view
                        val adapter = RidesAdapter(activity as Context, pastRidesList)
                        val linearLayoutManager = LinearLayoutManager(activity as Context)
                        binding.pastRecycler.adapter = adapter
                        binding.pastRecycler.layoutManager = linearLayoutManager

                    }
                    is RidesViewModel.MainStateEvent.Failure -> {}

                    is RidesViewModel.MainStateEvent.Loading -> {}
                    else -> Unit
                }
            }
        }

    }

}