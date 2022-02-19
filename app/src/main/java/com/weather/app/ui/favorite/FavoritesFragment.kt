package com.weather.app.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.weather.app.R
import com.weather.app.data.local.entity.Data
import com.weather.app.databinding.FragmentDashboardBinding
import com.weather.app.ui.search.CitiesAdapter
import com.weather.app.ui.search.ItemClickListener
import com.weather.app.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    @Inject
    lateinit var citiesAdapter: CitiesAdapter

    private lateinit var _binding: FragmentDashboardBinding

    private val binding get() = _binding
    private lateinit var viewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[FavoritesViewModel::class.java]
        if (!::_binding.isInitialized) {
            _binding = FragmentDashboardBinding.inflate(inflater, container, false)
            setViews()
            setupObserver()
            fetchCities()
        }
        return binding.root
    }

    private fun setViews() {
        citiesAdapter.setFavoriteClickListener(object : ItemClickListener {
            override fun onItemClick(view: View, data: Data) {
                val result = viewModel.delete(data)
                Log.d("DELETE", result.toString())
            }
        })

        citiesAdapter.setItemClickListener(object : ItemClickListener {
            override fun onItemClick(view: View, data: Data) {
                val bundle = bundleOf("data" to Gson().toJson(data))
                Navigation.findNavController(view)
                    .navigate(R.id.action_navigation_favorites_to_navigation_detail, bundle)
            }
        })
    }

    private fun fetchCities() {
        viewModel.fetchCities()
    }

    private fun setupObserver() {
        viewModel.getCities().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    renderList(it.data ?: listOf())
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        }

        viewModel.getCityDb().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show()
                    viewModel.fetchCities()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        }
    }

    private fun renderList(list: List<Data>) {
        _binding.rvCities.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            citiesAdapter.updateData(list, true)
            this.adapter = citiesAdapter
        }
    }
}