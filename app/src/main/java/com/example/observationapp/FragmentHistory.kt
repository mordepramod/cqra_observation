package com.example.observationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.observationapp.dashboard.domainlayer.HistoryViewModel
import com.example.observationapp.dashboard.domainlayer.ObservationViewModel
import com.example.observationapp.databinding.FragmentHistoryBinding
import com.example.observationapp.databinding.FragmentObservationBinding

class FragmentHistory : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    companion object {
        private const val TAG = "HistoryFragment"
    }

    private lateinit var viewModel: HistoryViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        return binding.root
    }


}