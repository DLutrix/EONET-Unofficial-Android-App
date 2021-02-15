package com.dlutrix.eonetunofficial.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dlutrix.eonetunofficial.R
import com.dlutrix.eonetunofficial.data.model.CategoryX
import com.dlutrix.eonetunofficial.data.model.Event
import com.dlutrix.eonetunofficial.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.tingyik90.snackprogressbar.SnackProgressBar
import com.tingyik90.snackprogressbar.SnackProgressBarManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private val snackProgressBarManager by lazy { SnackProgressBarManager(requireView(), this) }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val markers: MutableList<Marker> = arrayListOf()
    private val banjarmasin = LatLng(-3.316694, 114.590111)

    private val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

    private var userSelect = false

    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        snackProgressBarManager.setProgressBarColor(R.color.white)
            .setBackgroundColor(R.color.black)
            .useRoundedCornerBackground(true)
        with(binding) {
            categoriesCardView.isVisible = false
            categoriesSpinner.isVisible = false
            categoriesCardDetail.isVisible = false
            detailCategoriesSpinner.isVisible = false
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync {
                map = it
                it.animateCamera(
                    CameraUpdateFactory.newLatLng(banjarmasin)
                )
                it.setOnMarkerClickListener { marker ->
                    detailCategoriesSpinner.setSelection(marker.tag.toString().toInt())
                    false
                }
            }
        }

        categoriesEventObserver()
        detailCategoriesEventObserver()
    }

    private fun horizontalSnackBar() {
        val horizontalType = SnackProgressBar(SnackProgressBar.TYPE_HORIZONTAL, "Loading")
            .setIsIndeterminate(false)
            .setProgressMax(100)
        snackProgressBarManager.show(horizontalType, SnackProgressBarManager.LENGTH_INDEFINITE)
    }

    private fun categoriesEventObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.categories.collect { event ->
                when (event) {
                    is HomeViewModel.CategoriesEvent.Loading -> {
                        horizontalSnackBar()
                    }
                    is HomeViewModel.CategoriesEvent.Success -> {
                        val categories: MutableList<CategoryX> =
                            event.result.categories.toMutableList()
                        categories.add(
                            0, CategoryX(
                                "",
                                "",
                                "",
                                "",
                                "Select event..."
                            )
                        )

                        val adapter = object : ArrayAdapter<CategoryX>(
                            requireContext(),
                            R.layout.support_simple_spinner_dropdown_item,
                            categories
                        ) {
                            override fun isEnabled(position: Int): Boolean {
                                return position != 0
                            }

                            override fun getDropDownView(
                                position: Int,
                                convertView: View?,
                                parent: ViewGroup
                            ): View {
                                val tvView: TextView =
                                    super.getDropDownView(position, convertView, parent) as TextView
                                if (position == 0) {
                                    tvView.setTextColor(Color.LTGRAY)
                                }
                                return tvView
                            }
                        }
                        with(binding) {
                            categoriesSpinner.adapter = adapter
                            categoriesCardView.isVisible = true
                            categoriesSpinner.isVisible = true
                        }
                        snackProgressBarManager.dismissAll()
                    }
                    is HomeViewModel.CategoriesEvent.Failure -> {
                        Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_LONG).show()
                        snackProgressBarManager.dismissAll()
                    }
                    else -> {
                        snackProgressBarManager.dismissAll()
                    }
                }
            }
        }

        binding.categoriesSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val categories = parent?.selectedItem as CategoryX
                    viewModel.getDetailCategories(categories.id)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun formatDate(date: String): String {
        return if (date.isNotEmpty() || date.isNotBlank()) {
           "Date: ${format.parse(date)}"
        } else {
            "Date: -"
        }
    }

    private fun detailCategoriesEventObserver() {
        lifecycleScope.launchWhenStarted {
            viewModel.detailCategories.collect { events ->
                when (events) {
                    is HomeViewModel.DetailCategoriesEvent.Loading -> {
                        binding.categoriesCardDetail.isVisible = false
                        binding.detailCategoriesSpinner.isVisible = false
                        horizontalSnackBar()
                    }
                    is HomeViewModel.DetailCategoriesEvent.Success -> {
                        markers.clear()
                        map?.clear()
                        if (events.result.events.isNotEmpty()) {
                            events.result.events.forEachIndexed { i, event ->
                                if (event.geometry[0].type == "Point") {
                                    val marker = map?.addMarker(
                                        MarkerOptions().position(
                                            LatLng(
                                                event.geometry[0].coordinates[1].toString()
                                                    .toDouble(),
                                                event.geometry[0].coordinates[0].toString()
                                                    .toDouble()
                                            )
                                        ).title(event.title)
                                            .snippet(formatDate(event.geometry[0].date))
                                    )
                                    marker?.let {
                                        it.tag = i
                                        markers.add(it)
                                    }
                                }
                            }
                            markers.size
                            map?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        events.result.events[0].geometry[0].coordinates[1].toString()
                                            .toDouble(),
                                        events.result.events[0].geometry[0].coordinates[0].toString()
                                            .toDouble()
                                    ),
                                    map!!.minZoomLevel
                                )
                            )
                            val detailCategories: List<Event> = events.result.events
                            val adapter = ArrayAdapter(
                                requireContext(),
                                R.layout.support_simple_spinner_dropdown_item,
                                detailCategories
                            )
                            with(binding) {
                                categoriesCardDetail.isVisible = true
                                detailCategoriesSpinner.isVisible = true
                                detailCategoriesSpinner.adapter = adapter
                                detailCategoriesSpinner.setOnTouchListener { v, _ ->
                                    userSelect = true
                                    v.performClick()
                                }
                                detailCategoriesSpinner.onItemSelectedListener =
                                    object : OnItemSelectedListener {
                                        override fun onItemSelected(
                                            parent: AdapterView<*>?,
                                            view: View?,
                                            position: Int,
                                            id: Long
                                        ) {
                                            if (userSelect) {
                                                val event = parent?.selectedItem as Event
                                                map?.animateCamera(
                                                    CameraUpdateFactory.newLatLngZoom(
                                                        LatLng(
                                                            event.geometry[0].coordinates[1].toString()
                                                                .toDouble(),
                                                            event.geometry[0].coordinates[0].toString()
                                                                .toDouble()
                                                        ),
                                                        10f
                                                    )
                                                )
                                                userSelect = false
                                            }
                                            markers[position].showInfoWindow()
                                        }

                                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                                    }
                            }

                        } else {
                            map?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    banjarmasin, map!!.minZoomLevel
                                )
                            )
                        }
                        snackProgressBarManager.dismissAll()
                    }
                    is HomeViewModel.DetailCategoriesEvent.Failure -> {
                        binding.categoriesCardDetail.isVisible = false
                        binding.detailCategoriesSpinner.isVisible = false
                        Toast.makeText(requireContext(), events.errorText, Toast.LENGTH_LONG).show()
                        snackProgressBarManager.dismissAll()
                    }
                    else -> {
                        binding.categoriesCardDetail.isVisible = false
                        binding.detailCategoriesSpinner.isVisible = false
                        snackProgressBarManager.dismissAll()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        snackProgressBarManager.disable()
    }
}