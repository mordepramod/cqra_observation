package com.example.observationapp.dashboard.presentationlayer.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.observationapp.R
import com.example.observationapp.dashboard.domainlayer.ObservationViewModel

class ObservationFragment : Fragment() {

    companion object {
        fun newInstance() = ObservationFragment()
    }

    private lateinit var viewModel: ObservationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ObservationViewModel::class.java)
        return inflater.inflate(R.layout.fragment_observation, container, false)
    }

}