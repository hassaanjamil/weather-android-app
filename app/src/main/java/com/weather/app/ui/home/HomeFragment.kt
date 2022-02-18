package com.weather.app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.weather.app.R
import com.weather.app.data.remote.model.weather.ResponseWeather
import com.weather.app.databinding.FragmentHomeBinding
import com.weather.app.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.round
import java.util.*
import javax.inject.Inject

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

    /*private fun renderList(response: Response) {
        _binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            *//*addItemDecoration(
                DividerItemDecoration(
                    _binding.recyclerView.context,
                    (_binding.recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )*//*
            homeAdapter.updateData(response)
            this.adapter = homeAdapter
        }
    }*/

    private fun setupObserver() {
        /*homeViewModel.getMostPopularArticles().observe(it) {
            when (it.status) {
                Status.SUCCESS -> {
                    _binding.progressBar.visibility = View.GONE
                    renderList(it.data!!)
                    _binding.recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    _binding.progressBar.visibility = View.VISIBLE
                    _binding.recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    _binding.progressBar.visibility = View.GONE
                    Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }*/

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
                    _binding.progressBar.visibility = View.VISIBLE
                    _binding.recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    _binding.clProgressContainer.visibility = View.GONE
                    _binding.progressBar.visibility = View.GONE
                    Log.e("ERROR", it.message!!)
                }
            }
        }

        homeViewModel.getCurrentWeather().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    _binding.clProgressContainer.visibility = View.GONE
                    _binding.progressBar.visibility = View.GONE
                    renderUI(it.data)
                    homeViewModel.getCurrentWeather().removeObservers(this)
                    _binding.recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    _binding.clProgressContainer.visibility = View.VISIBLE
                    _binding.progressBar.visibility = View.VISIBLE
                    _binding.recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    _binding.clProgressContainer.visibility = View.GONE
                    _binding.progressBar.visibility = View.GONE
                    Log.e("ERROR", it.message!!)
                }
            }
        }
    }

    private fun renderUI(response: ResponseWeather?) {
        response?.let { it1 ->
            binding.tvLocationName.text = "${it1.name}, ${it1.sys?.country}"
            binding.tvDate.text = Date().toDateFormat("MMMM dd, yyyy")
            binding.tvTime.text = Date().toTimeFormat("hh:mm aa")
            binding.tvWindDirect.text = "Wind: ${it1.wind?.deg?.toDouble()?.formatBearing()}"
            binding.tvWindSpeed.text = "Speed: ${it1.wind?.speed} m/s"
            binding.tvTemp.text = "${it1.main?.temp?.let { round(it) }}º C"
            binding.tvDesc.text = "${it1.weather?.get(0)?.description}"
            binding.tvMaxTemp.text = "${it1.main?.temp_max}º C"
            binding.tvMinTemp.text = "${it1.main?.temp_min}º C"
            activity?.let {
                Glide.with(it)
                    .load("https://openweathermap.org/img/wn/${it1.weather?.get(0)?.icon}@4x.png")
                    .into(binding.ivIcon)
            }
        }
    }

    lateinit var locationHelper: LocationHelper

    private fun fetchLocation() {
        locationHelper = activity?.let {
            LocationHelper(it,
                {
                    homeViewModel.location.postValue(Resource.success(it))
                })
        }!!
        locationHelper.start()
    }


}