package com.example.observationapp.dashboard.presentationlayer.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.observationapp.R
import com.example.observationapp.dashboard.datalayer.ICardViewClickListener
import com.example.observationapp.dashboard.domainlayer.DashboardViewModel
import com.example.observationapp.dashboard.presentationlayer.ui.activity.SplashScrActivity
import com.example.observationapp.dashboard.presentationlayer.ui.adapters.DashboardCardRecyclerAdapter
import com.example.observationapp.dashboard.presentationlayer.ui.adapters.ImageSlideAdapter
import com.example.observationapp.databinding.FragmentDashboardBinding
import com.example.observationapp.models.Module
import com.example.observationapp.util.ItemOffsetDecoration
import com.example.observationapp.util.Utility.launchActivity
import com.example.observationapp.util.gone
import com.example.observationapp.util.showShortToast
import com.example.observationapp.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var imageList: ArrayList<Int>
    private var dots: Array<ImageView?> = arrayOfNulls(INITIAL_VALUE)
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var handler: Handler
    private lateinit var adapter: DashboardCardRecyclerAdapter

    private var moduleList: List<Module> = arrayListOf()

    companion object {
        private const val TAG = "DashboardFragment"

        //fun newInstance() = DashboardFragment()
        private const val DELAY_TIME_VALUE = 2500L
        private const val SPAN_COUNT = 2
        private const val INITIAL_VALUE = 0
        private const val MARGIN_PIX_VALUE = 40
    }

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        Log.e(TAG, "onViewCreated: ")
        binding.rvItems.layoutManager = GridLayoutManager(requireActivity(), SPAN_COUNT)
        val itemDecoration = ItemOffsetDecoration(requireContext(), R.dimen.item_offset)
        binding.rvItems.addItemDecoration(itemDecoration)
        adapter = DashboardCardRecyclerAdapter()
        binding.rvItems.adapter = adapter
        handler = Handler(Looper.getMainLooper())
        slidingImageAdapter()
        setUpTransformer()
        viewPagerCallBack()
        showDotsOnViewPager()
        adapterClickListener()
        liveDataObservers()
        loadProjectData()
        menuOptionImplementation()
    }

    private fun menuOptionImplementation() {

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.dashboard_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.refresh_project_data -> {
                        requireContext().showShortToast(getString(R.string.feature_is_coming_soon))
                        true
                    }

                    R.id.logout -> {
                        showProgress()
                        lifecycleScope.launch {
                            viewModel.deleteAllTablesData()
                            viewModel.deleteDataStore()
                            viewModel.deleteLoginRelatedData()
                        }
                        requireContext().launchActivity<SplashScrActivity> {
                            requireActivity().finish()
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun loadProjectData() {
        val value = viewModel.getProjectsApiCalled()
        Log.d(TAG, "loadProjectData: viewModel.apiSuccess: ${viewModel.apiSuccess}, value: $value")

        if (!value && !viewModel.apiSuccess) {
            lifecycleScope.launch {
                showProgress()
                viewModel.getLoggedInUser()
            }
        }

        lifecycleScope.launch {
            Log.e(TAG, "loadProjectData: moduleList")
            viewModel.moduleList()
        }
    }

    private fun liveDataObservers() {
        viewModel.projectList.observe(viewLifecycleOwner) {
            it?.let {
                Log.e(TAG, "liveDataObservers: $it")
                hideProgress()
                viewModel.putProjectsApiCalled(true)
            }

        }

        viewModel.userInfo.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.getProjectsList(it.user_id)
            }
        }

        viewModel.modelList.observe(viewLifecycleOwner) {
            it?.let {
                Log.e(TAG, "liveDataObservers: modelList: $it")
                moduleList = it
                adapter.setData(it)
            }

        }

    }

    private fun showProgress() {
        binding.llProgress.pbText.text = getString(R.string.loading_data)
        binding.llProgress.root.visible()
    }

    private fun hideProgress() {
        binding.llProgress.root.gone()
    }

    private fun adapterClickListener() {
        adapter.setListener(object : ICardViewClickListener {
            override fun onItemClick(position: Int) {
                if (moduleList[position].module_id == "6") {
                    viewModel.resetLiveData()
                    navController.navigate(R.id.dashboardFragment_to_observationFragment)
                }
            }
        })
    }

    private fun showDotsOnViewPager() {
        dots = arrayOfNulls(imageList.size)

        for (i in INITIAL_VALUE until imageList.size) {
            dots[i] = ImageView(requireContext())
            setNotActiveDotImage(i)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 8)
            binding.llDots.addView(dots[i], params)
        }

    }

    private fun viewPagerCallBack() {
        binding.viewPagerImages.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, DELAY_TIME_VALUE)
                if (dots.isNotEmpty()) {
                    setActiveDotImage(position)
                }

            }
        })
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable, DELAY_TIME_VALUE)
    }

    private val runnable = Runnable {
        binding.viewPagerImages.currentItem = binding.viewPagerImages.currentItem + 1

        if (binding.viewPagerImages.currentItem == imageList.size - 1) {
            lifecycleScope.launch {
                delay(DELAY_TIME_VALUE)
                binding.viewPagerImages.currentItem = INITIAL_VALUE
                setActiveDotImage(INITIAL_VALUE)
                for (i in 1 until imageList.size) {
                    setNotActiveDotImage(i)
                }
            }
        }
    }

    private fun setActiveDotImage(i: Int) {
        dots[i]?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.active_dot
            )
        )
    }

    private fun setNotActiveDotImage(i: Int) {
        dots[i]?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.non_active_dot
            )
        )
    }


    private fun slidingImageAdapter() {
        imageList = arrayListOf()
        imageList.add(R.drawable.ic_launcher_background)
        imageList.add(R.drawable.ic_launcher_background)
        imageList.add(R.drawable.ic_launcher_background)
        imageList.add(R.drawable.ic_launcher_background)

        val viewPagerAdapter = ImageSlideAdapter(imageList)
        binding.viewPagerImages.apply {
            adapter = viewPagerAdapter
            offscreenPageLimit = imageList.size
            clipChildren = false
            clipToPadding = false
        }

    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(MARGIN_PIX_VALUE))
        transformer.addTransformer { page, position ->
            val r = kotlin.math.abs(position)
            page.scaleY = 1f + r * 0.34f
        }

        binding.viewPagerImages.setPageTransformer(transformer)
    }

}