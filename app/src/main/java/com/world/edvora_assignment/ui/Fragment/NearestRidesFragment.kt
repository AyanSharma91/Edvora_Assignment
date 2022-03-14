package com.world.edvora_assignment.ui.Fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.world.edvora_assignment.Models.RidesRecyclerItem
import com.world.edvora_assignment.databinding.FragmentNearestRidesBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Integer.min
import kotlin.math.abs
import com.squareup.picasso.Picasso
import com.world.edvora_assignment.R
import com.world.edvora_assignment.Util.RidesAdapter
import com.world.edvora_assignment.databinding.ActivityMainBinding
import com.world.edvora_assignment.ui.ViewModel.UserDataViewModel
import com.world.edvora_assignment.ui.Activities.MainActivity
import com.world.edvora_assignment.ui.ViewModel.RidesViewModel


@AndroidEntryPoint
class NearestRidesFragment : Fragment() {


    private var _binding: FragmentNearestRidesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RidesViewModel by viewModels()
    private val viewModel2: UserDataViewModel by viewModels()

    lateinit var city_adapter : ArrayAdapter<String>
    lateinit  var layoutManager : LinearLayoutManager
    lateinit var  adapter : RidesAdapter


    lateinit  var main_list : ArrayList<RidesRecyclerItem>
    lateinit var filtered_list : ArrayList<RidesRecyclerItem>
    lateinit var state_list : ArrayList<String>
    lateinit var city_list : ArrayList<String>
    lateinit var main_city_list : ArrayList<String>
    var selected_state= ""
    var selected_city=""
    var customer_station = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val mainview = (requireActivity() as MainActivity).bind

        //Handling CLick Listeners
        click_listeners(mainview)

        //Setting Data to the Views
        setUserData(mainview)

        _binding = FragmentNearestRidesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Initializing The ArrayLists For Data Handling of Filtered Lists of Rides
        main_list = ArrayList()
        filtered_list= ArrayList()
        state_list = ArrayList()
        city_list=ArrayList()
        main_city_list = ArrayList()

        state_list.add("")
        main_city_list.add("")
        city_list.add("")


    }


    private fun click_listeners(mainview: ActivityMainBinding?) {



        mainview?.filterLayout?.setOnClickListener {
            if (mainview.filterBoxLayout.isVisible)
                mainview.filterBoxLayout.visibility = View.GONE
            else
                mainview.filterBoxLayout.visibility = View.VISIBLE
        }

        mainview?.filterBoxLayout?.setOnClickListener {
            mainview.filterBoxLayout.visibility = View.GONE
        }

        //Handling Clicks on States Spinner
        mainview?.stateSpinner?.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                (parent?.getChildAt(0) as TextView).setTextColor(Color.TRANSPARENT)

                if (position == 0) {
                    selected_state = ""
                } else {
                    selected_state = state_list[position]
                    mainview.stateSpinnerText.setText(state_list[position])
                    selected_city = ""
                    mainview.citySpinnerText.setText("City")
                    mainview.citySpinner.setSelection(0)
                    filter_city_list(selected_state)
                    filterlist(selected_state, selected_city)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        //Handling Clicks on City Spinner
        mainview?.citySpinner?.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                (parent?.getChildAt(0) as TextView).setTextColor(Color.TRANSPARENT)

                if (position == 0) {
                    selected_city = ""
                } else {
                    selected_city = city_list[position]
                    filterlist(selected_state, selected_city)
                    mainview.citySpinnerText.setText(city_list[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        })


    }


    private fun  setUserData(binding : ActivityMainBinding?) {

        viewModel2.get_user_details()
        lifecycleScope.launchWhenStarted {
            viewModel2.dataState.collect { event ->
                when (event) {

                    is UserDataViewModel.MainStateEvent.Success -> {

                        binding?.name?.text = event.result.name
                        Picasso.get().load(event.result.url).error(R.drawable.person).into(binding?.profile)
                        customer_station = event.result.stationCode
                        setNearestRides()
                    }
                    is UserDataViewModel.MainStateEvent.Failure -> {
                        Toast.makeText(context , "An Error Occured", Toast.LENGTH_SHORT).show()
                        _binding?.progressBar?.visibility = View.GONE

                    }
                    is UserDataViewModel.MainStateEvent.Loading -> {}
                    else -> Unit
                }
            }
        }

    }

    private fun setNearestRides() {

        viewModel.get_rides_details()
        lifecycleScope.launchWhenStarted {
            viewModel.dataState.collect { event ->

                when (event) {

                    is RidesViewModel.MainStateEvent.Success -> {
                        val list = event.result

                        for(it in list){
                            var distance = abs(it.stationPath[0] - customer_station)
                            for (i in it.stationPath){
                                distance =
                                    min(distance, abs(customer_station - i))
                            }
                            main_list.add(
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
                            if(!state_list.contains(it.state)) state_list.add(it.state)
                            if (!main_city_list.contains(it.city)){
                                main_city_list.add(it.city)
                                city_list.add(it.city)
                            }

                        }
                        val sortedlist  = main_list.sortedWith(compareBy { it.distance })

                        for(i in sortedlist)  filtered_list.add(i)

                        layoutManager = LinearLayoutManager(activity as Context)
                        adapter = RidesAdapter(activity as Context, filtered_list)
                        binding.nearestRecycler.adapter = adapter
                        binding.nearestRecycler.layoutManager = layoutManager


                        val mainview = (requireActivity() as MainActivity).bind

                        //Setting Up  State Spinner
                        set_up_state_spinner(mainview)

                        //Setting Up City Spinner
                        set_up_city_spinner(mainview)


                        binding.progressBar.visibility = View.GONE
                    }
                    is RidesViewModel.MainStateEvent.Failure -> {
                        binding.progressBar.visibility = View.GONE }

                    is RidesViewModel.MainStateEvent.Loading -> {}
                    else -> Unit
                }
            }
        }

    }

    private fun set_up_city_spinner(mainview: ActivityMainBinding?) {
        city_adapter = ArrayAdapter(
            activity!!,
            R.layout.spinner_item,
            city_list
        )

        city_adapter.setDropDownViewResource(R.layout.spinner_item)
        mainview?.citySpinner?.setAdapter(city_adapter)
    }

    private fun set_up_state_spinner(mainview: ActivityMainBinding?) {

        val state_adapter = ArrayAdapter(
            activity!!,
            R.layout.spinner_item,
            state_list
        )

        state_adapter.setDropDownViewResource(R.layout.spinner_item)
        mainview?.stateSpinner?.setAdapter(state_adapter)
    }


    private fun filter_city_list(selectedState: String) {
          city_list.clear()
          city_list.add("")
          for(i in main_list){
              if(i.state==selectedState && !(city_list.contains(i.city)))
                  city_list.add(i.city)
          }
        city_adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun filterlist(selectedState: String, selectedCity: String) {
        filtered_list.clear()
        if (selectedState != "" && selectedCity != "") {
            for (i in main_list) {
                if (i.state == selectedState && i.city == selectedCity) filtered_list.add(i)
            }
        }
        else if(selectedCity!=""){
            for (i in main_list) {
                if (i.city == selectedCity) filtered_list.add(i)
            }
        }
        else if(selectedState!=""){
            for (i in main_list) {
                if (i.state == selectedState) filtered_list.add(i)
            }
        }

        adapter.notifyDataSetChanged()
    }


}