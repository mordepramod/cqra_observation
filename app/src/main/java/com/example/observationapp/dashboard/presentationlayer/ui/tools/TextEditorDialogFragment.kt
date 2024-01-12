package com.example.observationapp.dashboard.presentationlayer.ui.tools

import android.app.Dialog
import android.app.PendingIntent.getActivity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.R

class TextEditorDialogFragment : DialogFragment() {

    fun setOnTextEditorListener(textEditor: TextEditor) {
        mTextEditor = textEditor
    }

    companion object {
        val TAG = TextEditorDialogFragment::class.java.simpleName

        const val EXTRA_INPUT_TEXT = "extra_input_text"
        const val EXTRA_COLOR_CODE = "extra_color_code"
        private var mAddTextEditText: EditText? = null
        private var mAddTextDoneTextView: TextView? = null
        private var mInputMethodManager: InputMethodManager? = null
        private var mColorCode = 0
        private lateinit var mTextEditor: TextEditor

        fun show(
            appCompatActivity: AppCompatActivity,
            inputText: String,
            @ColorInt colorCode: Int
        ): TextEditorDialogFragment? {
            val args = Bundle()
            args.putString(EXTRA_INPUT_TEXT, inputText)
            args.putInt(EXTRA_COLOR_CODE, colorCode)
            val fragment = TextEditorDialogFragment()
            fragment.arguments = args
            fragment.show(appCompatActivity.supportFragmentManager, TAG)
            return fragment
        }

        fun show(appCompatActivity: AppCompatActivity): TextEditorDialogFragment? {
            return show(
                appCompatActivity,
                "", ContextCompat.getColor(appCompatActivity, R.color.md_theme_onTertiary)
            )
        }

        fun onStart() {
            this.onStart()
            val dialog: Dialog = getDialog()
            //Make dialog full screen with transparent background
            if (dialog != null) {
                val width = ViewGroup.LayoutParams.MATCH_PARENT
                val height = ViewGroup.LayoutParams.MATCH_PARENT
                dialog.window!!.setLayout(width, height)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }

        fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.add_text_dialog, container, false)
        }

        fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            this.onViewCreated(view, savedInstanceState)
            mAddTextEditText = view.findViewById(R.id.add_text_edit_text)
            mInputMethodManager = getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)
            mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv)

            //Setup the color picker for text color
            val addTextColorPickerRecyclerView =
                view.findViewById<RecyclerView>(R.id.add_text_color_picker_recycler_view)
            val layoutManager =
                LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false)
            addTextColorPickerRecyclerView.layoutManager = layoutManager
            addTextColorPickerRecyclerView.setHasFixedSize(true)
            val colorPickerAdapter = ColorPickerAdapter(getActivity())
            //This listener will change the text color when clicked on any color from picker
            colorPickerAdapter.setOnColorPickerClickListener(object : OnColorPickerClickListener {
                fun onColorPickerClickListener(colorCode: Int) {
                    mColorCode = colorCode
                    mAddTextEditText?.setTextColor(colorCode)
                }

                override fun onColorPickerClick(colorCode: Int) {
                    TODO("Not yet implemented")
                }
            })
            addTextColorPickerRecyclerView.adapter = colorPickerAdapter
            mAddTextEditText?.setText(getArguments().getString(EXTRA_INPUT_TEXT))
            mColorCode = getArguments().getInt(EXTRA_COLOR_CODE)
            mAddTextEditText?.setTextColor(mColorCode)
            mInputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

            //Make a callback on activity when user is done with text editing
            mAddTextDoneTextView?.setOnClickListener(View.OnClickListener { view ->
                mInputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
                dismiss()
                val inputText = mAddTextEditText?.getText().toString()
                if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                    mTextEditor.onDone(inputText, mColorCode)
                }
            })
        }

        interface TextEditor {
            fun onDone(inputText: String, colorCode: Int)
        }


    }

}