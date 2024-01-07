package com.example.observationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.observationapp.dashboard.domainlayer.MaterialManagementViewModel
import com.example.observationapp.dashboard.domainlayer.ObservationViewModel
import com.example.observationapp.databinding.FragmentManagementBinding
import com.example.observationapp.databinding.FragmentObservationBinding


class FragmentManagement : Fragment() {

    private lateinit var binding: FragmentManagementBinding

    companion object {
        private const val TAG = "ManagementFragment"
    }

    private lateinit var viewModel: MaterialManagementViewModel




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentManagementBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[MaterialManagementViewModel::class.java]
        return binding.root
    }


}