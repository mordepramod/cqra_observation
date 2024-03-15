package com.example.observationapp.dashboard.presentationlayer.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.observationapp.R
import com.example.observationapp.dashboard.domainlayer.ObservationViewModel
import com.example.observationapp.databinding.FragmentObservationBinding
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.AllocatedToModel
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StatusModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import com.example.observationapp.photo_edit.EditImageActivity
import com.example.observationapp.util.CommonConstant
import com.example.observationapp.util.Utility.getSelectedDateInString
import com.example.observationapp.util.Utility.getTimeStampInLong
import com.example.observationapp.util.gone
import com.example.observationapp.util.showShortToast
import com.example.observationapp.util.visible
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import java.util.TimeZone


@AndroidEntryPoint
class ObservationFragment : Fragment() {
    private lateinit var binding: FragmentObservationBinding

    companion object {
        private const val TAG = "ObservationFragment"
    }

    private var observationHistoryModel: ObservationHistory? = null
    private val viewModel: ObservationViewModel by viewModels()
    private var projectList = listOf<ProjectModelItem>()
    private var structureList = listOf<StructureModel>()
    private var stageOrFloorList = listOf<StageModel>()
    private var tradeGroupModelList = listOf<TradeGroupModel>()
    private var tradeModelList = listOf<TradeModel>()
    private var observationTypeList = listOf<ObservationType>()
    private var observationCategoryList = listOf<ObservationCategory>()
    private var observationSeverityList = listOf<ObservationSeverity>()
    private var accountableList = listOf<Accountable>()
    private var allocatedToList = listOf<AllocatedToModel>()
    private var statusList = listOf<StatusModel>()
    private var savedPathList = arrayListOf<String>()
    private var savedFileNameList = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObservationBinding.inflate(inflater, container, false)
        val model =
            arguments?.getParcelable<ObservationHistory>(CommonConstant.KEY_PARCELABLE_HISTORY_MODEL)
        Log.d(TAG, "onCreateView: $model")

        arguments?.let { bundle ->
            bundle.getParcelable<ObservationHistory>(CommonConstant.KEY_PARCELABLE_HISTORY_MODEL)
                ?.let {
                    Log.d(TAG, "onCreateView: model: $it")
                    observationHistoryModel = it
                }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                /* override back pressing */
                override fun handleOnBackPressed() {
                    if (isProgressVisible()) {
                        requireActivity().showShortToast("Please wait until form is saving.")
                    } else {
                        checkFilesToDelete()
                    }

                }
            })

        //viewModel.addDummyEntry()


        return binding.root
    }

    private fun checkFilesToDelete() {
        if (savedPathList.isEmpty()) {
            findNavController().navigateUp()
        } else {
            deleteFileIfAny()
        }
    }

    private fun showProgress() {
        makeBackButtonNonClickable()
        makeButtonNonClickable()
        binding.llProgress.llProgressBar.visible()
    }

    private fun isProgressVisible(): Boolean {
        return binding.llProgress.root.isVisible
    }

    private fun makeBackButtonNonClickable() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    private fun makeBackButtonClickable() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun makeButtonNonClickable() {
        binding.btnSavedForm.apply {
            isClickable = false
            isEnabled = false
        }
        binding.btnCaptureImages.apply {
            isClickable = false
            isEnabled = false
        }
    }

    private fun makeButtonClickable() {
        binding.btnSavedForm.apply {
            isClickable = true
            isEnabled = true
        }
        binding.btnCaptureImages.apply {
            isClickable = true
            isEnabled = true
        }
    }

    private fun hideProgress() {
        makeBackButtonClickable()
        makeButtonClickable()
        binding.llProgress.llProgressBar.gone()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveDataObservers()
        setProjectAdapterData()
        clickListeners()

    }

    private fun clickListeners() {
        binding.btnCaptureImages.setOnClickListener {
            //requireContext().launchActivity<EditImageActivity>()
            if (savedPathList.size >= 2) {
                requireContext().showShortToast("You already selected 2 images.")
            } else {
                val intent = Intent(requireContext(), EditImageActivity::class.java)
                resultLauncher.launch(intent)
            }
        }
        binding.btnSavedImages.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArrayList(CommonConstant.IMAGE_PATH, savedPathList)

            findNavController().navigate(
                R.id.action_observationFragment_to_viewImageFragment,
                bundle
            )

        }
        binding.btnSavedForm.setOnClickListener {
            val description = binding.autoDescriptionName.text.toString()
            val location = binding.autoLocationName.text.toString()
            val remark = binding.autoRemarkName.text.toString()
            val reference = binding.autoReferenceName.text.toString()
            val targetDate = binding.etDatePicker.text.toString()


            if (viewModel.isValueEmpty(viewModel.projectId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.project_name)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.structureId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.structure_name)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.stageOrFloorId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.stage_floor)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.tradeGroupId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.trade_group)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.tradeId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.activity_trade)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.observationTypeId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.observation_type)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.observationCategoryId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.observation_category)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(description)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.description)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(remark)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.remark)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(reference)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.reference)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(location)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.location)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.observationSeverityId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.observation_severity)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.accountableId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.accountable)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.statusId)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.status)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(viewModel.closeById)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.close_by)
                    )
                )
                return@setOnClickListener
            }
            if (viewModel.isValueEmpty(targetDate)) {
                requireContext().showShortToast(
                    getString(
                        R.string.s_is_empty,
                        getString(R.string.target_date)
                    )
                )
                return@setOnClickListener
            }
            if (savedPathList.size == 0) {
                requireContext().showShortToast(getString(R.string.no_images_are_selected))
                return@setOnClickListener
            }
            viewModel.saveForm(
                location,
                description,
                remark,
                reference,
                targetDate,
                savedPathList
            )


        }

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                val resultString: String = data?.getStringExtra(CommonConstant.FILE_PATH) ?: ""
                val resultFileNameString: String =
                    data?.getStringExtra(CommonConstant.FILE_NAMES) ?: ""
                if (!viewModel.isValueEmpty(resultString)) {
                    savedPathList.add(resultString)
                    savedFileNameList.add(resultFileNameString)
                }
                Log.e(TAG, "result: $resultString")
            }
        }


    private fun liveDataObservers() {
        viewModel.obsHistoryModel.observe(viewLifecycleOwner) { model ->

            Log.d(TAG, "onCreateView: $model")
        }


        viewModel.projectList.observe(viewLifecycleOwner) { projectListModel ->
            projectListModel?.let { projectModelItems ->
                projectList = projectModelItems
                val adapterProject = ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    projectList
                )
                binding.autoCompleteProjectName.setAdapter(adapterProject)
                setDataToProjectListAdapter(adapterProject)
                viewModel.getUserId()
            }
        }


        viewModel.structureList.observe(viewLifecycleOwner) {
            it?.let {
                structureList = it
                val structureModelArrayAdapter = ArrayAdapter(
                    requireContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    structureList
                )
                binding.autoStructureName.setAdapter(structureModelArrayAdapter)
                setDataToStructureListAdapter(structureModelArrayAdapter)
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
                    setDataToStageOrFloorNameAdapter(stageModelArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.tradeGroupModelList.observe(viewLifecycleOwner) {
                it?.let {
                    tradeGroupModelList = it
                    val tradeGroupArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        tradeGroupModelList
                    )
                    binding.autoTradeGroupName.setAdapter(tradeGroupArrayAdapter)
                    setTradeGroup(tradeGroupArrayAdapter)
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
                    setTradeOrActivity(tradeModelAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.observationHistoryModel.observe(viewLifecycleOwner) {
                it?.let {
                    findNavController().popBackStack()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.observationFormModel.observe(viewLifecycleOwner) {
                it?.let {
                    if (it) {
                        showProgress()
                    } else {
                        hideProgress()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.observationTypeModelList.observe(viewLifecycleOwner) {
                it?.let {
                    observationTypeList = it
                    val observationTypeArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        observationTypeList
                    )
                    binding.autoObservationTypeName.setAdapter(observationTypeArrayAdapter)
                    setObservationTypeData(observationTypeArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.observationCategoryModelList.observe(viewLifecycleOwner) {
                it?.let {
                    observationCategoryList = it
                    val observationCategoryArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        observationCategoryList
                    )
                    binding.autoObservationCategoryName.setAdapter(observationCategoryArrayAdapter)
                    setObservationCategoryData(observationCategoryArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.observationSeverList.observe(viewLifecycleOwner) {
                it?.let {
                    observationSeverityList = it
                    val observationSeverityArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        observationSeverityList
                    )
                    binding.autoObsSeverityName.setAdapter(observationSeverityArrayAdapter)
                    setObservationSeverityData(observationSeverityArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.accountableModelList.observe(viewLifecycleOwner) {
                it?.let {
                    accountableList = it
                    val accountableArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        accountableList
                    )
                    binding.autoAccountableName.setAdapter(accountableArrayAdapter)
                    setObservationAccountableData(accountableArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.allocatedlList.observe(viewLifecycleOwner) {
                it?.let {
                    allocatedToList = it
                    val allocatedArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        it
                    )
                    binding.autoCloseByName.setAdapter(allocatedArrayAdapter)
                    setCloseByData(allocatedArrayAdapter)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.statusModelList.observe(viewLifecycleOwner) {
                it?.let {
                    statusList = it
                    val statusArrayAdapter = ArrayAdapter(
                        requireContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                        it
                    )
                    binding.autoStatusName.setAdapter(statusArrayAdapter)
                    setStatusNameData(statusArrayAdapter)
                }
            }
        }


    }

    private fun setDataToProjectListAdapter(adapterProject: ArrayAdapter<ProjectModelItem>) {
        observationHistoryModel?.let { obModel ->
            projectList.forEach {
                if (obModel.project_id == it.project_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoCompleteProjectName.setText(
                        binding.autoCompleteProjectName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.projectId = projectList[pos].project_id
                    viewModel.getStructureList(viewModel.projectId)
                    return@forEach
                }
            }
        }
    }

    private fun setDataToStructureListAdapter(adapterProject: ArrayAdapter<StructureModel>) {
        observationHistoryModel?.let { model ->
            structureList.forEach {
                if (model.structure_id == it.structure_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoStructureName.setText(
                        binding.autoStructureName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.structureId = structureList[pos].structure_id
                    viewModel.getStageOrFloorList(viewModel.structureId)
                    return@forEach
                }
            }
        }
    }

    private fun setDataToStageOrFloorNameAdapter(adapterProject: ArrayAdapter<StageModel>) {
        observationHistoryModel?.let { model ->
            stageOrFloorList.forEach {
                if (model.floors == it.stage_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoStageOrFloorName.setText(
                        binding.autoStageOrFloorName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.stageOrFloorId = stageOrFloorList[pos].stage_id
                    return@forEach
                }
            }
        }
    }

    private fun setTradeGroup(adapterProject: ArrayAdapter<TradeGroupModel>) {
        observationHistoryModel?.let { model ->
            tradeGroupModelList.forEach {
                if (model.tradegroup_id == it.tradegroup_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoTradeGroupName.setText(
                        binding.autoTradeGroupName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.tradeGroupId = tradeGroupModelList[pos].tradegroup_id
                    viewModel.getTradeModelList(viewModel.tradeGroupId)
                    return@forEach
                }
            }
        }
    }

    private fun setTradeOrActivity(adapterProject: ArrayAdapter<TradeModel>) {
        observationHistoryModel?.let { model ->
            tradeModelList.forEach {
                if (model.activityOrTradeId == it.trade_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoActivityName.setText(
                        binding.autoActivityName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.tradeId = tradeModelList[pos].trade_id
                    return@forEach
                }
            }
        }
    }

    private fun setObservationTypeData(adapterProject: ArrayAdapter<ObservationType>) {
        observationHistoryModel?.let { model ->
            observationTypeList.forEach {
                if (model.observation_type == it.type_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoObservationTypeName.setText(
                        binding.autoObservationTypeName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.observationTypeId = observationTypeList[pos].type_id
                    return@forEach
                }
            }
        }
    }

    private fun setObservationCategoryData(adapterProject: ArrayAdapter<ObservationCategory>) {
        observationHistoryModel?.let { model ->
            observationCategoryList.forEach {
                if (model.observation_category == it.category_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoObservationCategoryName.setText(
                        binding.autoObservationCategoryName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.observationCategoryId = observationCategoryList[pos].category_id
                    return@forEach
                }
            }
        }
    }

    private fun setObservationSeverityData(adapterProject: ArrayAdapter<ObservationSeverity>) {
        observationHistoryModel?.let { model ->
            observationSeverityList.forEach {
                if (model.observation_severity == it.severity_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoObsSeverityName.setText(
                        binding.autoObsSeverityName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.observationCategoryId = observationSeverityList[pos].severity_id
                    return@forEach
                }
            }
        }
    }

    private fun setObservationAccountableData(adapterProject: ArrayAdapter<Accountable>) {
        observationHistoryModel?.let { model ->
            accountableList.forEach {
                if (model.site_representative == it.user_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoAccountableName.setText(
                        binding.autoAccountableName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.accountableId = accountableList[pos].user_id
                    return@forEach
                }
            }
        }
    }

    private fun setStatusNameData(adapterProject: ArrayAdapter<StatusModel>) {
        observationHistoryModel?.let { model ->
            statusList.forEach {
                if (model.site_representative == it.status_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoStatusName.setText(
                        binding.autoStatusName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.statusId = statusList[pos].status_id
                    return@forEach
                }
            }
        }
    }

    private fun setCloseByData(adapterProject: ArrayAdapter<AllocatedToModel>) {
        observationHistoryModel?.let { model ->
            allocatedToList.forEach {
                if (model.closed_by == it.role_id) {
                    val pos = adapterProject.getPosition(it)
                    binding.autoCloseByName.setText(
                        binding.autoCloseByName.adapter.getItem(pos).toString(), false
                    )
                    viewModel.closeById = allocatedToList[pos].role_id
                    return@forEach
                }
            }
        }
    }

    private fun setProjectAdapterData() {
        viewModel.getProjectList()
        viewModel.getTradeGroupList()
        viewModel.getAllocatedToList()
        viewModel.getAllStatusList()
        viewModel.getObservationSeverityList()
        viewModel.getObservationCategoryList()
        viewModel.getObservationTypeList()
        viewModel.getAccountableList()

        binding.autoCompleteProjectName.setOnItemClickListener { _, _, position, _ ->
            viewModel.projectId = projectList[position].project_id
            viewModel.getStructureList(viewModel.projectId)
            binding.autoStructureName.setText("")
            viewModel.structureId = ""
            Log.d(TAG, "setProjectAdapterData: ${viewModel.projectId}")
        }

        binding.autoStructureName.setOnItemClickListener { _, _, position, _ ->
            viewModel.structureId = structureList[position].structure_id
            binding.autoStageOrFloorName.setText("")
            viewModel.stageOrFloorId = ""
            viewModel.getStageOrFloorList(viewModel.structureId)
            Log.d(TAG, "autoStructureName: ${viewModel.structureId}")
        }

        binding.autoStageOrFloorName.setOnItemClickListener { _, _, position, _ ->
            viewModel.stageOrFloorId = stageOrFloorList[position].stage_id
            binding.autoTradeGroupName.setText("")
            viewModel.tradeGroupId = ""
            //viewModel.getTradeGroupList()
            Log.d(TAG, "autoStageOrFloorName: ${viewModel.stageOrFloorId}")
        }

        binding.autoTradeGroupName.setOnItemClickListener { _, _, position, _ ->
            viewModel.tradeGroupId = tradeGroupModelList[position].tradegroup_id
            binding.autoActivityName.setText("")
            viewModel.tradeId = ""
            viewModel.getTradeModelList(viewModel.tradeGroupId)
            Log.d(TAG, "autoTradeGroupName: ${viewModel.tradeGroupId}")
        }

        binding.autoActivityName.setOnItemClickListener { _, _, position, _ ->
            viewModel.tradeId = tradeModelList[position].trade_id
            Log.d(TAG, "autoActivityTradeName: ${viewModel.tradeId}")
        }

        binding.autoObservationTypeName.setOnItemClickListener { _, _, position, _ ->
            viewModel.observationTypeId = observationTypeList[position].type_id
            Log.d(TAG, "autoObservationTypeName: ${viewModel.observationTypeId}")
        }

        binding.autoObservationCategoryName.setOnItemClickListener { _, _, position, _ ->
            viewModel.observationCategoryId = observationCategoryList[position].category_id
            Log.d(TAG, "autoObservationCategoryName: ${viewModel.observationCategoryId}")
        }

        binding.autoObsSeverityName.setOnItemClickListener { _, _, position, _ ->
            viewModel.observationSeverityId = observationSeverityList[position].severity_id
            Log.d(TAG, "autoObsSeverityName: ${viewModel.observationSeverityId}")
        }

        binding.autoAccountableName.setOnItemClickListener { _, _, position, _ ->
            viewModel.accountableId = accountableList[position].user_id
            Log.d(TAG, "autoAccountableName: ${viewModel.accountableId}")
        }

        binding.autoCloseByName.setOnItemClickListener { _, _, position, _ ->
            viewModel.closeById = allocatedToList[position].role_id
            Log.d(TAG, "autoCloseByName: ${viewModel.closeById}")
        }

        binding.autoStatusName.setOnItemClickListener { _, _, position, _ ->
            viewModel.statusId = statusList[position].status_id
            Log.d(TAG, "autoStatusName: ${viewModel.statusId}")
        }

        binding.etDatePicker.setOnClickListener {
            val today: Long
            if (binding.etDatePicker.text.toString().isNotEmpty()) {
                today = getTimeStampInLong(binding.etDatePicker.text.toString())
                Log.d(TAG, "etDatePicker: $today")
            } else {
                today = MaterialDatePicker.todayInUtcMilliseconds()
            }
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

            calendar.timeInMillis = today
            calendar[Calendar.MONTH] = Calendar.JANUARY
            val janThisYear = calendar.timeInMillis

            calendar.timeInMillis = today
            calendar[Calendar.MONTH] = Calendar.DECEMBER
            val decThisYear = calendar.timeInMillis

// Build constraints.
            val constraintsBuilder =
                CalendarConstraints.Builder()
                    .setStart(janThisYear)
                    .setEnd(decThisYear)
                    .setValidator(DateValidatorPointForward.now())

            val picker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.select_target_date))
                    .setSelection(today)
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build()
            picker.show(childFragmentManager, "tag")


            picker.addOnPositiveButtonClickListener {
                it?.let {
                    Log.d(TAG, "addOnPositiveButtonClickListener $it: ")
                    val date = getSelectedDateInString(it)
                    binding.etDatePicker.setText(date)
                }
                // Respond to positive button click.
            }
            picker.addOnNegativeButtonClickListener {
                // Respond to negative button click.
            }
            picker.addOnCancelListener {
                // Respond to cancel button click.
            }
            picker.addOnDismissListener {
                // Respond to dismiss events.
            }
        }

    }

    private fun deleteFileIfAny() {

        Log.d(TAG, "deleteFileIfAny: point 1")
        savedPathList.forEach {
            val isFileDeleted = lifecycleScope.async {
                var isFileDeleted = false
                val file = File(it)
                if (file.exists()) {
                    isFileDeleted = file.delete()
                }
                return@async isFileDeleted
            }
            lifecycleScope.launch {
                isFileDeleted.await()
                Log.d(TAG, "deleteFileIfAny: file deleted $it")
            }
        }
        Log.e(TAG, "deleteFileIfAny: point 2")
        findNavController().navigateUp()


    }

}