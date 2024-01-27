package com.example.observationapp.photo_edit

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.observationapp.databinding.FragmentViewImageBinding
import com.example.observationapp.util.CommonConstant
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ViewImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var imagePath: ArrayList<String>? = null
    private lateinit var binding: FragmentViewImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = it.getStringArrayList(CommonConstant.IMAGE_PATH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentViewImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e(TAG, "onViewCreated: $imagePath")

        imagePath?.forEachIndexed { index, str ->
            if (index == 0) {
                val imgFile = File(str)
                if (imgFile.exists()) {

                    // on below line we are creating an image bitmap variable
                    // and adding a bitmap to it from image file.
                    val imgBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

                    // on below line we are setting bitmap to our image view.
                    binding.imageView.setImageBitmap(imgBitmap)
                }
            } else {
                val imgFile = File(str)
                if (imgFile.exists()) {

                    // on below line we are creating an image bitmap variable
                    // and adding a bitmap to it from image file.
                    val imgBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

                    // on below line we are setting bitmap to our image view.
                    binding.imageView2.setImageBitmap(imgBitmap)
                }
            }

        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewImageFragment.
         */
        private const val TAG = "ViewImageFragment"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            ViewImageFragment().apply {
                arguments = Bundle().apply {
                    putString(CommonConstant.IMAGE_PATH, param1)
                }
            }
    }
}