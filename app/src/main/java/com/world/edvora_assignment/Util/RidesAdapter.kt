package com.world.edvora_assignment.Util


import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.world.edvora_assignment.Models.RidesRecyclerItem
import com.world.edvora_assignment.databinding.SingleRowRidesBinding



class RidesAdapter(val context: Context, val arr: List<RidesRecyclerItem>) :
    RecyclerView.Adapter<RidesAdapter.DashboardViewHolder>() {

    private val spannable = Spannable_String()

    inner class DashboardViewHolder(val binding: SingleRowRidesBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {

        val binding =
            SingleRowRidesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arr.size
    }


    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val item = arr[position]
        holder.binding.cityName.text = item.city
        holder.binding.stateName.text = item.state

        holder.binding.date.text = Html.fromHtml(
            spannable.getColoredSpanned(
                "Date :",
                "#ADADAF"
            ) + " " + item.date
        )

        holder.binding.rideNo.text = Html.fromHtml(
            spannable.getColoredSpanned(
                "Ride Id :",
                "#ADADAF"
            ) + " " + item.id
        )

        holder.binding.originStation.text = Html.fromHtml(
            spannable.getColoredSpanned(
                "Origin Station :",
                "#ADADAF"
            ) + " " + item.originStationCode
        )

        holder.binding.stationPath.text = Html.fromHtml(
            spannable.getColoredSpanned(
                "station_path :",
                "#ADADAF"
            ) + " " + item.stationPath
        )

        holder.binding.distance.text = Html.fromHtml(
            spannable.getColoredSpanned(
                "Distance :",
                "#ADADAF"
            ) + " " + item.distance
        )


        Picasso.get()
            .load(item.mapUrl)
            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
            .into(holder.binding.mapPicture)

    }

}


