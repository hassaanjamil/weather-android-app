package com.weather.app.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.weather.app.data.remote.model.cities.ResponseCities
import com.weather.app.databinding.FragmentSearchBinding
import com.weather.app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var searchAdapter: SearchAdapter

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
                requestCitiesData(newText)
                return true
            }
        })

        binding.searchView.setOnCloseListener {
            binding.searchView.onActionViewCollapsed()
            true
        }
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
        viewModel.getCities().observe(viewLifecycleOwner) {
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
    }

    private fun renderList(response: ResponseCities) {
        _binding.rvCities.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            searchAdapter.updateData(response)
            this.adapter = searchAdapter
        }
    }

}