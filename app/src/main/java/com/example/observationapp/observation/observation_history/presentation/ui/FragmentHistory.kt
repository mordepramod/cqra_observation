package com.example.observationapp.observation.observation_history.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.observationapp.R
import com.example.observationapp.dashboard.datalayer.ICardViewClickListener
import com.example.observationapp.dashboard.domainlayer.HistoryViewModel
import com.example.observationapp.databinding.FragmentHistoryBinding
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.observation.observation_history.presentation.adapters.ObservationHistoryAdapter
import com.example.observationapp.util.gone
import com.example.observationapp.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentHistory : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private lateinit var navController: NavController
    private var observationHistoryList: List<ObservationHistory> = arrayListOf()

    companion object {
        private const val TAG = "HistoryFragment"
    }

    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var adapter: ObservationHistoryAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        showProgress()
        iniView()
        observeLiveData()
        getObservationHistoryAPIOrDB()
    }

    private fun showProgress() {
        binding.llProgress.llProgressBar.visible()
    }

    private fun hideProgress() {
        binding.llProgress.llProgressBar.gone()
    }

    private fun iniView() {
        binding.rvObservationHistory.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ObservationHistoryAdapter()
        binding.rvObservationHistory.adapter = adapter
        adapter.setListener(object : ICardViewClickListener {
            override fun onItemClick(position: Int) {
                val model = observationHistoryList[position]
                Log.d(TAG, "onItemClick: $model")
            }

        })

        binding.fabAddObservation.setOnClickListener {
            navController.navigate(R.id.action_fragmentHistory_to_observationFragment)
        }

    }

    private fun getObservationHistoryAPIOrDB() {
        val isApiCalled = viewModel.getObservationHistoryApiCalled()
        Log.d(
            TAG,
            "getObservationHistoryAPIOrDB: isApiCalled: $isApiCalled, viewModel.apiSuccess: ${viewModel.apiSuccess}"
        )
        if (!viewModel.apiSuccess && !isApiCalled) {
            viewModel.getObservationHistoryAPI()
        } else {
            viewModel.observationHistoryList()
        }

    }

    private fun observeLiveData() {
        viewModel.observationHistory.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.putObservationHistoryApiCalled(it)
            }
        }
        viewModel.observationHistoryList.observe(viewLifecycleOwner) {
            hideProgress()
            it?.let {
                if (it.isEmpty()) {
                    noListAvailable()
                } else {
                    historyAvailable()
                    observationHistoryList = it
                    adapter.setData(it)
                }
            }

        }

    }

    private fun historyAvailable() {
        binding.tvNoDataAvailable.gone()
        binding.rvObservationHistory.visible()
    }

    private fun noListAvailable() {
        binding.tvNoDataAvailable.visible()
        binding.rvObservationHistory.gone()
    }


}