package com.weather.app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weather.app.BuildConfig
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
            setupView()
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
        inflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miSearch -> {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(R.id.action_navigation_home_to_navigation_search)
                }
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        /*if (::_binding.isInitialized) {
            _binding = null //de-referencing
        }*/
        locationHelper.stop()
    }

    private fun setupView() {
        binding.ivArrowLeft.setOnClickListener {
            if (layoutManager.findFirstVisibleItemPosition() > 0) {
                binding.rvForecast.smoothScrollToPosition(layoutManager.findFirstVisibleItemPosition() - 1)
            } else {
                binding.rvForecast.smoothScrollToPosition(0)
            }
        }

        binding.ivArrowRight.setOnClickListener {
            binding.rvForecast.smoothScrollToPosition(layoutManager.findLastVisibleItemPosition() + 1)
        }
    }

    private lateinit var layoutManager: LinearLayoutManager
    private fun renderList(response: ResponseForecast) {
        _binding.rvForecast.apply {
            this@HomeFragment.layoutManager =
                LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            layoutManager = this@HomeFragment.layoutManager
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
                            it1.longitude, "")
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
            val avgTemp = "${it1.main?.temp?.roundToInt()}?? C"
            binding.tvTemp.text = avgTemp
            binding.tvDesc.text = "${it1.weather?.get(0)?.description}"
            val avgTempMax = "Max: ${it1.main?.temp_max?.roundToInt()}?? C"
            binding.tvMaxTemp.text = avgTempMax
            val avgTempMin = "Min: ${it1.main?.temp_min?.roundToInt()}?? C"
            binding.tvMinTemp.text = avgTempMin
            val humidity = "H: ${it1.main?.humidity}%"
            binding.tvHumidity.text = humidity
            activity?.let {
                Glide.with(it)
                    .load("${BuildConfig.BASE_URL_ICONS}img/wn/${it1.weather?.get(0)?.icon}@4x.png")
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