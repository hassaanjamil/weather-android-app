package com.weather.app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weather.app.R
import com.weather.app.data.remote.model.forecast.ResponseForecast
import com.weather.app.data.remote.model.weather.ResponseWeather
import com.weather.app.databinding.FragmentHomeBinding
import com.weather.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var homeAdapter: HomeAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var _binding: FragmentHomeBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (!::_binding.isInitialized) {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)
            setupObserver()
            fetchLocation()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            Toast.makeText(context, "Search Clicked", Toast.LENGTH_SHORT)
                .show()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*if (::_binding.isInitialized) {
            _binding = null //de-referencing
        }*/
        locationHelper.stop()
    }

    private fun renderList(response: ResponseForecast) {
        _binding.rvForecast.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            /*addItemDecoration(
                DividerItemDecoration(
                    _binding.recyclerView.context,
                    (_binding.recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )*/
            homeAdapter.updateData(response)
            this.adapter = homeAdapter
        }
    }

    private fun setupObserver() {
        homeViewModel.getCurrentLocation().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    //_binding.clProgressContainer.visibility = View.GONE
                    //_binding.progressBar.visibility = View.GONE
                    Log.d("LOCATION", "Received")
                    it.data?.let { it1 ->
                        homeViewModel.fetchCurrentWeather(it1.latitude,
                            it1.longitude)
                    }
                    //_binding.recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    _binding.clProgressContainer.visibility = View.VISIBLE
                    _binding.root.visibility = View.GONE
                }
                Status.ERROR -> {
                    _binding.clProgressContainer.visibility = View.GONE
                    Log.e("ERROR", it.message!!)
                }
            }
        }

        homeViewModel.getCurrentWeather().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    _binding.clProgressContainer.visibility = View.GONE
                    renderUI(it.data)
                    homeViewModel.getCurrentWeather().removeObservers(this)
                    _binding.root.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    _binding.clProgressContainer.visibility = View.VISIBLE
                    _binding.root.visibility = View.GONE
                }
                Status.ERROR -> {
                    _binding.clProgressContainer.visibility = View.GONE
                    Log.e("ERROR", it.message!!)
                }
            }
        }

        homeViewModel.getMonthlyForecast().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    _binding.clProgressContainer.visibility = View.GONE
                    it.data?.let { it1 -> renderList(it1) }
                    homeViewModel.getMonthlyForecast().removeObservers(this)
                    _binding.root.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    _binding.clProgressContainer.visibility = View.VISIBLE
                    _binding.root.visibility = View.GONE
                }
                Status.ERROR -> {
                    _binding.clProgressContainer.visibility = View.GONE
                    Log.e("ERROR", it.message!!)
                }
            }
        }
    }

    private fun renderUI(response: ResponseWeather?) {
        response?.let { it1 ->
            val locationName = "${it1.name}, ${it1.sys?.country}"
            binding.tvLocationName.text = locationName
            binding.tvDate.text = Date().toDateFormat("MMMM dd, yyyy")
            binding.tvTime.text = Date().toTimeFormat("hh:mm aa")
            val windDirect = "Wind: ${it1.wind?.deg?.toDouble()?.formatBearing()}"
            binding.tvWindDirect.text = windDirect
            val windSpeed = "Speed: ${it1.wind?.speed} m/s"
            binding.tvWindSpeed.text = windSpeed
            val avgTemp = "${it1.main?.temp?.roundToInt()}º C"
            binding.tvTemp.text = avgTemp
            binding.tvDesc.text = "${it1.weather?.get(0)?.description}"
            val avgTempMax = "Max: ${it1.main?.temp_max?.roundToInt()}º C"
            binding.tvMaxTemp.text = avgTempMax
            val avgTempMin = "Min: ${it1.main?.temp_min?.roundToInt()}º C"
            binding.tvMinTemp.text = avgTempMin
            val humidity = "H: ${it1.main?.humidity}%"
            binding.tvHumidity.text = humidity
            activity?.let {
                Glide.with(it)
                    .load("https://openweathermap.org/img/wn/${it1.weather?.get(0)?.icon}@4x.png")
                    .into(binding.ivIcon)
            }
        }
    }

    private lateinit var locationHelper: LocationHelper

    private fun fetchLocation() {
        locationHelper = activity?.let { activity ->
            LocationHelper(activity,
                {
                    homeViewModel.location.postValue(Resource.success(it))
                })
        }!!
        locationHelper.start()
    }


}