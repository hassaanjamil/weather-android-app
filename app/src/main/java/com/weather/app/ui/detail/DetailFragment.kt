package com.weather.app.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.weather.app.BuildConfig
import com.weather.app.data.local.entity.Data
import com.weather.app.data.remote.model.weather.ResponseWeather
import com.weather.app.databinding.FragmentDetailBinding
import com.weather.app.ui.search.CitiesAdapter
import com.weather.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class DetailFragment : Fragment() {

    @Inject
    lateinit var citiesAdapter: CitiesAdapter

    private lateinit var viewModel: DetailViewModel

    private lateinit var _binding: FragmentDetailBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        if (!::_binding.isInitialized) {
            _binding = FragmentDetailBinding.inflate(inflater, container, false)
            setHasOptionsMenu(true)
            fetchLocation()
            setupObserver()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            view?.let { Navigation.findNavController(it).popBackStack() }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObserver() {
        viewModel.getCurrentWeather().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> renderUI(it1) }
                    _binding.clProgressContainer.visibility = View.GONE
                }
                Status.LOADING -> {
                    _binding.clProgressContainer.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Log.e("ERROR", it.message!!)
                    _binding.clProgressContainer.visibility = View.GONE
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
                    .load("${BuildConfig.BASE_URL_ICONS}img/wn/${it1.weather?.get(0)?.icon}@4x.png")
                    .into(binding.ivIcon)
            }
        }
    }

    private lateinit var locationHelper: LocationHelper

    private fun fetchLocation() {
        val data = Gson().fromJson(arguments?.getString("data"), Data::class.java)
        viewModel.fetchCurrentWeather(data.latitude, data.longitude, "")
    }

}