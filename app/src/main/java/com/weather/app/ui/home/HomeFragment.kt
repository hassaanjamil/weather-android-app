package com.weather.app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weather.app.R
import com.weather.app.data.remote.model.other.ResponseArticles
import com.weather.app.data.remote.model.weather.ResponseWeather
import com.weather.app.databinding.FragmentHomeBinding
import com.weather.app.utils.LocationHelper
import com.weather.app.utils.Resource
import com.weather.app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
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

    private fun renderList(response: ResponseArticles) {
        _binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
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
                    _binding.progressBar.visibility = View.GONE
                    Log.d("LOCATION", "Received")
                    it.data?.let { it1 ->
                        homeViewModel.fetchCurrentWeather(it1.latitude,
                            it1.longitude)
                    }
                    _binding.recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    _binding.progressBar.visibility = View.VISIBLE
                    _binding.recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    _binding.progressBar.visibility = View.GONE
                    Log.e("ERROR", it.message!!)
                }
            }
        }

        homeViewModel.getCurrentWeather().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    _binding.progressBar.visibility = View.GONE
                    renderUI(it.data)
                    homeViewModel.getCurrentWeather().removeObservers(this)
                    _binding.recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    _binding.progressBar.visibility = View.VISIBLE
                    _binding.recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    _binding.progressBar.visibility = View.GONE
                    Log.e("ERROR", it.message!!)
                }
            }
        }
    }

    private fun renderUI(response: ResponseWeather?) {
        response?.main?.temp.let { it1 ->
            Toast.makeText(activity,
                it1.toString(),
                Toast.LENGTH_SHORT).show()
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