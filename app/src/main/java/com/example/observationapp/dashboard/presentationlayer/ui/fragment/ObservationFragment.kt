package com.example.observationapp.dashboard.presentationlayer.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.observationapp.dashboard.domainlayer.ObservationViewModel
import com.example.observationapp.databinding.FragmentObservationBinding
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.UnitModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ObservationFragment : Fragment() {
    private lateinit var binding: FragmentObservationBinding
    private var projectId = ""
    private var structureId = ""
    private var stageOrFloorId = ""
    private var unitId = ""

    companion object {
        private const val TAG = "ObservationFragment"
    }

    private lateinit var viewModel: ObservationViewModel
    private var projectList = listOf<ProjectModelItem>()
    private var structureList = listOf<StructureModel>()
    private var stageOrFloorList = listOf<StageModel>()
    private var unitList = listOf<UnitModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObservationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ObservationViewModel::class.java]

        liveDataObservers()

        setProjectAdapterData()
        return binding.root
    }

    private fun liveDataObservers() {

        viewModel.getProjectList().observe(viewLifecycleOwner) {
            it?.let { projectModelItems ->
                projectList = projectModelItems
                val adapterProject = ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    projectList
                )
                binding.autoCompleteProjectName.setAdapter(adapterProject)
            }
        }


        lifecycleScope.launch {
            viewModel.structureList.observe(viewLifecycleOwner) {
                it?.let {
                    structureList = it
                    val structureModelArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        structureList
                    )
                    binding.autoStructureName.setAdapter(structureModelArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.stageOrFloorList.observe(viewLifecycleOwner) {
                it?.let {
                    stageOrFloorList = it
                    val stageModelArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        stageOrFloorList
                    )
                    binding.autoStageOrFloorName.setAdapter(stageModelArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.unitList.observe(viewLifecycleOwner) {
                it?.let {
                    unitList = it
                    val unitModelArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        unitList
                    )
                    binding.autoTradeGroupName.setAdapter(unitModelArrayAdapter)
                }
            }
        }


    }

    private fun setProjectAdapterData() {

        binding.autoCompleteProjectName.setOnItemClickListener { _, _, position, _ ->
            projectId = projectList[position].project_id
            viewModel.getStructureList(projectId)
            binding.autoStructureName.setText("")
            Log.d(TAG, "setProjectAdapterData: $projectId")
        }

        binding.autoStructureName.setOnItemClickListener { _, _, position, _ ->
            structureId = structureList[position].structure_id
            binding.autoStageOrFloorName.setText("")
            viewModel.getStageOrFloorList(structureId)
            Log.d(TAG, "autoStructureName: $structureId")
        }

        binding.autoStageOrFloorName.setOnItemClickListener { _, _, position, _ ->
            stageOrFloorId = stageOrFloorList[position].stage_id
            binding.autoTradeGroupName.setText("")
            viewModel.getUnitList(stageOrFloorId)
            Log.d(TAG, "autoStageOrFloorName: $stageOrFloorId")
        }

        binding.autoTradeGroupName.setOnItemClickListener { _, _, position, _ ->
            unitId = unitList[position].stage_id
            Log.d(TAG, "autoTradeGroupName: $unitId")
        }

    }


}