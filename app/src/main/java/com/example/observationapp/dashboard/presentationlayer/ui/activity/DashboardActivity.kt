package com.example.observationapp.dashboard.presentationlayer.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.observationapp.R
import com.example.observationapp.dashboard.presentationlayer.ui.adapters.DashboardCardRecyclerAdapter
import com.example.observationapp.databinding.ActivityDashboardBinding
import com.example.observationapp.util.ItemOffsetDecoration

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)

        binding.rvItems.layoutManager = GridLayoutManager(this, 2)
        val itemDecoration = ItemOffsetDecoration(this, R.dimen.item_offset);
        binding.rvItems.addItemDecoration(itemDecoration)
        val adapter = DashboardCardRecyclerAdapter()
        binding.rvItems.adapter = adapter

        setContentView(binding.root)
    }
}