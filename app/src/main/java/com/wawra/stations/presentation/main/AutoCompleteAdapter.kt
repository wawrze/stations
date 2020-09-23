package com.wawra.stations.presentation.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.wawra.stations.R
import com.wawra.stations.database.entities.Station
import java.util.*

class AutoCompleteAdapter(context: Context, stations: List<Station>) :
    ArrayAdapter<Station>(context, R.layout.item_station, stations), Filterable {

    private val stations: List<Station>
    private var suggestions: MutableList<Station> = ArrayList()

    private val mFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filterResults = FilterResults()
            suggestions.clear()
            val fixedConstraint = constraint.toString().toUpperCase(Locale.getDefault())
            for (station in stations) {
                val fixedStationName = station.toString().toUpperCase(Locale.getDefault())
                if (fixedStationName.contains(fixedConstraint)) suggestions.add(station)
            }
            filterResults.values = suggestions
            filterResults.count = suggestions.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            @Suppress("UNCHECKED_CAST")
            val filteredList = results.values as? List<Station> ?: listOf()
            if (results.count > 0) {
                clear()
                for (filteredObject in filteredList) add(filteredObject)
                notifyDataSetChanged()
            }
        }

    }

    override fun getFilter() = mFilter

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_station, parent, false)

        val station: Station? = getItem(position)

        val stationName: TextView? = view.findViewById(R.id.item_station_name)
        stationName?.text = station?.toString().orEmpty()

        return view
    }

    init {
        this.stations = ArrayList(stations)
    }

}