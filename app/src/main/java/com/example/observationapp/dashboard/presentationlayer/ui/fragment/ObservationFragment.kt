package com.example.observationapp.dashboard.presentationlayer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.observationapp.dashboard.domainlayer.ObservationViewModel
import com.example.observationapp.databinding.FragmentObservationBinding
import com.example.observationapp.models.ProjectModelItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObservationFragment : Fragment() {
    private lateinit var binding: FragmentObservationBinding

    companion object {
        private const val TAG = "ObservationFragment"
    }

    private lateinit var viewModel: ObservationViewModel
    private var projectList = listOf<ProjectModelItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObservationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ObservationViewModel::class.java]

        viewModel.getProjectList().observe(viewLifecycleOwner) {
            it?.let { projectModelItems ->
                projectList = projectModelItems
                val adapterQuestionHeading = ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    projectList
                )
                binding.autoCompleteProjectName.setAdapter(adapterQuestionHeading)
            }
        }

        setProjectAdapterData()
        return binding.root
    }

    private fun setProjectAdapterData() {

        binding.autoCompleteProjectName.setOnItemClickListener { _, _, position, _ ->
            val projectId = projectList[position].project_id
            Log.d(TAG, "setProjectAdapterData: $projectId")

        }

    }


}