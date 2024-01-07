package com.example.observationapp.dashboard.presentationlayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.observationapp.dashboard.domainlayer.ObservationViewModel
import com.example.observationapp.databinding.FragmentObservationBinding

class ObservationFragment : Fragment() {
    private lateinit var binding: FragmentObservationBinding

    companion object {
        private const val TAG = "ObservationFragment"
    }

    private lateinit var viewModel: ObservationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObservationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ObservationViewModel::class.java]
        return binding.root
    }


}