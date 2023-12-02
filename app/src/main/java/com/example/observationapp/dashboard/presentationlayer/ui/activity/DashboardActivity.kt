package com.example.observationapp.dashboard.presentationlayer.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.observationapp.R
import com.example.observationapp.dashboard.presentationlayer.ui.adapters.DashboardCardRecyclerAdapter
import com.example.observationapp.dashboard.presentationlayer.ui.adapters.ImageSlideAdapter
import com.example.observationapp.databinding.ActivityDashboardBinding
import com.example.observationapp.util.ItemOffsetDecoration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DashboardActivity : AppCompatActivity() {
    private val imageList = ArrayList<Int>()
    private var dots: Array<ImageView?> = arrayOfNulls(INITIAL_VALUE)

    companion object {
        private const val DELAY_TIME_VALUE = 2500L
        private const val SPAN_COUNT = 2
        private const val INITIAL_VALUE = 0
        private const val MARGIN_PIX_VALUE = 40
    }

    private lateinit var handler: Handler
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        binding.rvItems.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        val itemDecoration = ItemOffsetDecoration(this, R.dimen.item_offset)
        binding.rvItems.addItemDecoration(itemDecoration)
        val adapter = DashboardCardRecyclerAdapter()
        binding.rvItems.adapter = adapter
        handler = Handler(Looper.getMainLooper())
        slidingImageAdapter()
        setUpTransformer()
        viewPagerCallBack()
        showDotsOnViewPager()
        setContentView(binding.root)
    }

    private fun showDotsOnViewPager() {
        dots = arrayOfNulls(imageList.size)

        for (i in INITIAL_VALUE until imageList.size) {
            dots[i] = ImageView(this)
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
                this@DashboardActivity,
                R.drawable.active_dot
            )
        )
    }

    private fun setNotActiveDotImage(i: Int) {
        dots[i]?.setImageDrawable(
            ContextCompat.getDrawable(
                this@DashboardActivity,
                R.drawable.non_active_dot
            )
        )
    }


    private fun slidingImageAdapter() {
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