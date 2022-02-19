package com.weather.app.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weather.app.data.remote.model.cities.Data
import com.weather.app.data.remote.model.cities.ResponseCities
import com.weather.app.databinding.FragmentSearchBinding
import com.weather.app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var citiesAdapter: CitiesAdapter

    private lateinit var viewModel: SearchViewModel

    private lateinit var _binding: FragmentSearchBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        if (!::_binding.isInitialized) {
            _binding = FragmentSearchBinding.inflate(inflater, container, false)
            setHasOptionsMenu(true)
            setViews()
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

    private val mHandler = Handler(Looper.myLooper()!!)
    private var mQuery = ""
    private fun setViews() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("onQueryTextSubmit", query)
                requestCitiesData(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("onQueryTextChange", newText)
                if (newText.isEmpty()) {
                    citiesAdapter.updateData(listOf(), false)
                } else {
                    requestCitiesData(newText)
                }
                return true
            }
        })

        citiesAdapter.setFavoriteClickListener(object : ItemClickListener {
            override fun onItemClick(view: View, data: Data) {
                val result = viewModel.insert(data)
                Log.d("INSERT", result.toString())
            }
        })

        citiesAdapter.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(view: View, data: Data) {
                Toast.makeText(activity,
                    "${data.name} clicked",
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun requestCitiesData(query: String) {
        mQuery = query
        mHandler.removeCallbacksAndMessages(null)

        mHandler.postDelayed({
            if (mQuery.length > 2) {
                viewModel.fetchCities(mQuery)
            }
        }, 300)
    }

    private fun setupObserver() {
        viewModel.getCitiesResponse().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { it1 -> renderList(it1) }
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Log.e("ERROR", it.message!!)
                }
            }
        }

        viewModel.getCityDb().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(activity, "Inserted", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Log.e("ERROR", it.message!!)
                }
            }
        }
    }

    private fun renderList(response: ResponseCities) {
        _binding.rvCities.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            citiesAdapter.updateData(response.data ?: listOf(), false)
            this.adapter = citiesAdapter
        }
    }

}