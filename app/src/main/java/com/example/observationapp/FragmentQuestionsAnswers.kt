package com.example.observationapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.observationapp.dashboard.domainlayer.ObservationViewModel
import com.example.observationapp.dashboard.domainlayer.QuestionsAnswersViewModel
import com.example.observationapp.databinding.FragmentObservationBinding
import com.example.observationapp.databinding.FragmentQuestionsAnswersBinding


class FragmentQuestionsAnswers : Fragment() {
    private lateinit var binding: FragmentQuestionsAnswersBinding

    companion object {
        private const val TAG = "QuestionsAnswersFragment"
    }

    private lateinit var viewModel: QuestionsAnswersViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionsAnswersBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        return binding.root
    }

}