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
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ObservationFragment : Fragment() {
    private lateinit var binding: FragmentObservationBinding
    private var projectId = ""
    private var structureId = ""
    private var stageOrFloorId = ""
    private var tradeGroupId = ""
    private var tradeId = ""
    private var observationTypeId = ""
    private var observationSeverityId = ""
    private var accountableId = ""

    companion object {
        private const val TAG = "ObservationFragment"
    }

    private lateinit var viewModel: ObservationViewModel
    private var projectList = listOf<ProjectModelItem>()
    private var structureList = listOf<StructureModel>()
    private var stageOrFloorList = listOf<StageModel>()
    private var tradeGroupModelList = listOf<TradeGroupModel>()
    private var tradeModelList = listOf<TradeModel>()
    private var observationTypeList = listOf<ObservationType>()
    private var observationSeverityList = listOf<ObservationSeverity>()
    private var accountableList = listOf<Accountable>()

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
            viewModel.getTradeGroupList().observe(viewLifecycleOwner) {
                it?.let {
                    tradeGroupModelList = it
                    val tradeGroupArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        tradeGroupModelList
                    )
                    binding.autoTradeGroupName.setAdapter(tradeGroupArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.tradeModelList.observe(viewLifecycleOwner) {
                it?.let {
                    tradeModelList = it
                    val tradeModelAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        tradeModelList
                    )
                    binding.autoActivityName.setAdapter(tradeModelAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getObservationTypeList().observe(viewLifecycleOwner) {
                it?.let {
                    observationTypeList = it
                    val tradeGroupArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        observationTypeList
                    )
                    binding.autoObservationTypeName.setAdapter(tradeGroupArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getObservationSeverityList().observe(viewLifecycleOwner) {
                it?.let {
                    observationSeverityList = it
                    val tradeGroupArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        observationSeverityList
                    )
                    binding.autoObsSeverityName.setAdapter(tradeGroupArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getAccountableList().observe(viewLifecycleOwner) {
                it?.let {
                    accountableList = it
                    val tradeGroupArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        accountableList
                    )
                    binding.autoAccountableName.setAdapter(tradeGroupArrayAdapter)
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
            Log.d(TAG, "autoStageOrFloorName: $stageOrFloorId")
        }

        binding.autoTradeGroupName.setOnItemClickListener { _, _, position, _ ->
            tradeGroupId = tradeGroupModelList[position].tradegroup_id
            binding.autoActivityName.setText("")
            viewModel.getTradeModelList(tradeGroupId)
            Log.d(TAG, "autoTradeGroupName: $tradeGroupId")
        }

        binding.autoActivityName.setOnItemClickListener { _, _, position, _ ->
            tradeId = tradeModelList[position].trade_id
            Log.d(TAG, "autoActivityTradeName: $tradeId")
        }

        binding.autoObservationTypeName.setOnItemClickListener { _, _, position, _ ->
            observationTypeId = observationTypeList[position].type_id
            Log.d(TAG, "autoObservationTypeName: $observationTypeId")
        }

        binding.autoObsSeverityName.setOnItemClickListener { _, _, position, _ ->
            observationSeverityId = observationSeverityList[position].severity_id
            Log.d(TAG, "autoObsSeverityName: $observationSeverityId")
        }

        binding.autoAccountableName.setOnItemClickListener { _, _, position, _ ->
            accountableId = accountableList[position].user_id
            Log.d(TAG, "autoAccountableName: $accountableId")
        }

    }


}