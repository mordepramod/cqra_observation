package com.example.observationapp.photo_edit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.observationapp.R
import com.example.observationapp.databinding.ActivityEditImageBinding
import com.example.observationapp.util.CommonConstant
import com.example.observationapp.util.PermissionEnum
import com.example.observationapp.util.Utility
import com.example.observationapp.util.Utility.openAppSettings
import com.example.observationapp.util.gone
import com.example.observationapp.util.showLongToast
import com.example.observationapp.util.visible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.SaveFileResult
import ja.burhanrashid52.photoeditor.SaveSettings
import ja.burhanrashid52.photoeditor.TextStyleBuilder
import ja.burhanrashid52.photoeditor.ViewType
import ja.burhanrashid52.photoeditor.shape.ShapeBuilder
import ja.burhanrashid52.photoeditor.shape.ShapeType
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


@AndroidEntryPoint
class EditImageActivity : AppCompatActivity(), OnPhotoEditorListener, View.OnClickListener,
    ShapeBSFragment.Properties,
    EditingToolsAdapter.OnItemSelected {
    private var savedFilePath: String = ""
    private var galleryClicked: Boolean = false
    private var image_uri: Uri? = null
    private var mAngleRotate = 0f
    lateinit var mPhotoEditor: PhotoEditor
    private lateinit var mPhotoEditorView: PhotoEditorView
    private lateinit var mShapeBSFragment: ShapeBSFragment
    private lateinit var mShapeBuilder: ShapeBuilder
    private lateinit var mTxtCurrentTool: TextView
    private lateinit var mRvTools: RecyclerView
    private val mEditingToolsAdapter = EditingToolsAdapter(this)
    private lateinit var mRootView: ConstraintLayout
    private lateinit var binding: ActivityEditImageBinding

    private lateinit var mSaveFileHelper: FileSaveHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()

        handleIntentImage(mPhotoEditorView.source)
        mShapeBSFragment = ShapeBSFragment()
        mShapeBSFragment.setPropertiesChangeListener(this)

        mEditingToolsAdapter.setList(this)
        val llmTools = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRvTools.layoutManager = llmTools
        mRvTools.adapter = mEditingToolsAdapter

        // NOTE(lucianocheng): Used to set integration testing parameters to PhotoEditor
        val pinchTextScalable = intent.getBooleanExtra(PINCH_TEXT_SCALABLE_INTENT_KEY, true)

        mPhotoEditor = PhotoEditor.Builder(this, mPhotoEditorView)
            .setPinchTextScalable(pinchTextScalable) // set flag to make text scalable when pinch
            .build() // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this)
        mPhotoEditorView.source.scaleType = ImageView.ScaleType.FIT_XY

        mSaveFileHelper = FileSaveHelper(this)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            /* override back pressing */
            override fun handleOnBackPressed() {
                //Your code here
                if (mPhotoEditorView.source.drawable == null) {
                    showLongToast("Please select image from Gallery or capture an Image.")
                    finish()
                } else {
                    showSaveDialog()
                }

            }
        })
    }

    private fun makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    private fun handleIntentImage(source: ImageView) {
        if (intent == null) {
            return
        }

        when (intent.action) {
            Intent.ACTION_EDIT, ACTION_NEXTGEN_EDIT -> {
                try {
                    val uri = intent.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    source.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            else -> {
                val intentType = intent.type
                if (intentType != null && intentType.startsWith("image/")) {
                    val imageUri = intent.data
                    if (imageUri != null) {
                        source.setImageURI(imageUri)
                    }
                }
            }
        }
    }

    private fun initViews() {
        mPhotoEditorView = binding.photoEditorView
        mTxtCurrentTool = binding.txtCurrentTool
        mRvTools = binding.rvConstraintTools
        mRootView = binding.rootView

        binding.imgUndo.setOnClickListener(this)

        binding.imgRedo.setOnClickListener(this)

        binding.imgCamera.setOnClickListener(this)

        binding.imgGallery.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgUndo -> mPhotoEditor.undo()
            R.id.imgRedo -> mPhotoEditor.redo()
            R.id.imgCamera -> {
                val permissionList = arrayListOf<String>()
                if (!hasPermission(PermissionEnum.CAMERA.permission)) {
                    permissionList.add(PermissionEnum.CAMERA.permission)
                }
                if (!hasPermission(PermissionEnum.WRITE_EXTERNAL_STORAGE.permission)) {
                    permissionList.add(PermissionEnum.WRITE_EXTERNAL_STORAGE.permission)
                }
                requestCameraPermissionLauncherMulti.launch(permissionList.toTypedArray())
            }

            R.id.imgGallery -> {
                galleryClicked = true
                if (hasPermission(PermissionEnum.WRITE_EXTERNAL_STORAGE.permission)) {
                    openGallery()
                } else {
                    permissionLauncherForStorage.launch(PermissionEnum.WRITE_EXTERNAL_STORAGE.permission)
                }

            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        registerActivityForStorage.launch(Intent.createChooser(intent, "Select Picture"))
    }

    private fun externalPermissionAlert() {
        showPermissionAlert(
            getString(R.string.storage_permission),
            getString(R.string.this_permission_is_required_to_pick_a_image_from_storage),
            getString(R.string.ok), getString(R.string.cancel)
        ) {
            openAppSettings(this)
        }
    }

    private fun showPermissionAlert(
        title: String,
        message: String,
        ok: String,
        cancel: String,
        function: () -> Unit
    ) {
        val mDialog = MaterialAlertDialogBuilder(this)
        mDialog.setTitle(title)
        mDialog.setMessage(message)
        mDialog.setPositiveButton(
            ok
        ) { _, _ ->
            function.invoke()
        }
        mDialog.setNegativeButton(cancel) { _, _ ->
            Log.d(TAG, "showPermissionAlert: ")
        }
        mDialog.show()
    }

    private fun saveImage() {
        val timeStampValue = System.currentTimeMillis().toString()
        val fileName = "$timeStampValue${CommonConstant.FILE_EXTENSIONS}"
        val hasStoragePermission = hasPermission(PermissionEnum.WRITE_EXTERNAL_STORAGE.permission)
        if (hasStoragePermission || FileSaveHelper.isSdkHigherThan28()) {
            showLoading()
            mSaveFileHelper.createFile(fileName, object : FileSaveHelper.OnFileCreateResult {

                @SuppressLint("MissingPermission")
                override fun onFileCreateResult(
                    created: Boolean,
                    filePath: String?,
                    error: String?,
                    uri: Uri?
                ) {
                    lifecycleScope.launch {
                        if (created && filePath != null) {
                            val saveSettings = SaveSettings.Builder()
                                .setClearViewsEnabled(true)
                                .setTransparencyEnabled(true)
                                .build()

                            val result = mPhotoEditor.saveAsFile(filePath, saveSettings)

                            if (result is SaveFileResult.Success) {
                                mSaveFileHelper.notifyThatFileIsNowPubliclyAvailable(contentResolver)
                                hideLoading()
                                showSnackbar("Image Saved Successfully")

                                Log.d(
                                    TAG,
                                    "onFileCreateResult: ${result.toString()}, filePath: $filePath, fileName: $fileName"
                                )
                                val string = Utility.getFilePathWithoutName(filePath)
                                val path = "$string/$fileName"
                                Log.d(
                                    TAG,
                                    "onFileCreateResult: truncated str: $string, actualPath: $path"
                                )

                                savedFilePath = path
                                val returnIntent = Intent()
                                returnIntent.putExtra(CommonConstant.FILE_PATH, path)
                                returnIntent.putExtra(CommonConstant.FILE_NAMES, timeStampValue)
                                setResult(Activity.RESULT_OK, returnIntent)
                                if (image_uri != null) {
                                    deleteFileIfAny()
                                }
                                finish()

                            } else {
                                hideLoading()
                                showSnackbar("Failed to save Image")
                            }
                        } else {
                            hideLoading()
                            error?.let { showSnackbar(error) }
                        }
                    }
                }
            })
        } else {
            requestPermission()
        }
    }

    override fun onColorChanged(colorCode: Int) {
        mPhotoEditor.setShape(mShapeBuilder.withShapeColor(colorCode))
        mTxtCurrentTool.setText(R.string.label_brush)
    }

    override fun onOpacityChanged(opacity: Int) {
        mPhotoEditor.setShape(mShapeBuilder.withShapeOpacity(opacity))
        mTxtCurrentTool.setText(R.string.label_brush)
    }

    override fun onShapeSizeChanged(shapeSize: Int) {
        mPhotoEditor.setShape(mShapeBuilder.withShapeSize(shapeSize.toFloat()))
        mTxtCurrentTool.setText(R.string.label_brush)
    }

    override fun onShapePicked(shapeType: ShapeType) {
        mPhotoEditor.setShape(mShapeBuilder.withShapeType(shapeType))
    }


    private fun showSaveDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setMessage(getString(R.string.save_image))
        builder.setPositiveButton(getString(R.string.save)) { _: DialogInterface?, _: Int -> saveImage() }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
        builder.setNeutralButton(getString(R.string.discard)) { _: DialogInterface?, _: Int -> deleteFileIfAny() }//finish() }
        builder.show()
    }

    private fun deleteFileIfAny() {

        try {
            val isFileDeleted = lifecycleScope.async {
                var isFileDeleted = false
                if (image_uri == null) {
                    isFileDeleted = true
                } else {
                    savedFilePath = getRealPathFromURI(this@EditImageActivity, image_uri!!) ?: ""
                    if (savedFilePath.isNotEmpty()) {
                        val file = File(savedFilePath)
                        Log.d(
                            TAG,
                            "fileExist = ${file.exists()}, deleteFileIfAny: path: $savedFilePath"
                        )
                        if (file.exists()) {
                            isFileDeleted = file.delete()
                        }
                    } else {
                        isFileDeleted = true
                    }
                }

                return@async isFileDeleted
            }
            lifecycleScope.launch {
                if (isFileDeleted.await()) {
                    finish()
                } else {
                    finish()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            finish()
        }


    }

    override fun onToolSelected(toolType: ToolType) {
        when (toolType) {
            ToolType.SHAPE -> {
                mPhotoEditor.setBrushDrawingMode(true)
                mShapeBuilder = ShapeBuilder()
                mPhotoEditor.setShape(mShapeBuilder)
                mTxtCurrentTool.setText(getString(R.string.shape))
                showBottomSheetDialogFragment(mShapeBSFragment)
            }

            ToolType.TEXT -> {
                val textEditorDialogFragment = TextEditorDialogFragment.show(this)
                textEditorDialogFragment.setOnTextEditorListener(object :
                    TextEditorDialogFragment.TextEditorListener {
                    override fun onDone(inputText: String, colorCode: Int) {
                        val styleBuilder = TextStyleBuilder()
                        styleBuilder.withTextColor(colorCode)
                        mPhotoEditor.addText(inputText, styleBuilder)
                        mTxtCurrentTool.setText("Text")
                    }
                })
            }

            ToolType.ERASER -> {
                mPhotoEditor.brushEraser()
                mTxtCurrentTool.setText("Eraser")
            }

            ToolType.ROTATE -> {
                when (mAngleRotate) {
                    0f -> {
                        mAngleRotate = 90f
                    }

                    90f -> {
                        mAngleRotate = 180f
                    }

                    180f -> {
                        mAngleRotate = 270f
                    }

                    270f -> {
                        mAngleRotate = 0f
                    }
                }
                mPhotoEditorView.source.rotation = mAngleRotate
            }
        }
    }

    private fun showBottomSheetDialogFragment(fragment: BottomSheetDialogFragment?) {
        if (fragment == null || fragment.isAdded) {
            return
        }
        fragment.show(supportFragmentManager, fragment.tag)
    }

    companion object {

        private const val TAG = "EditImageActivity"

        const val FILE_PROVIDER_AUTHORITY = "com.burhanrashid52.photoediting.fileprovider"
        private const val CAMERA_REQUEST = 52
        private const val PICK_REQUEST = 53
        const val ACTION_NEXTGEN_EDIT = "action_nextgen_edit"
        const val PINCH_TEXT_SCALABLE_INTENT_KEY = "PINCH_TEXT_SCALABLE"
    }

    override fun onAddViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(TAG, "onAddViewListener: ")
    }

    override fun onEditTextChangeListener(rootView: View?, text: String?, colorCode: Int) {
        val textEditorDialogFragment =
            TextEditorDialogFragment.show(this, text.toString(), colorCode)
        textEditorDialogFragment.setOnTextEditorListener(object :
            TextEditorDialogFragment.TextEditorListener {
            override fun onDone(inputText: String, colorCode: Int) {
                val styleBuilder = TextStyleBuilder()
                styleBuilder.withTextColor(colorCode)
                if (rootView != null) {
                    mPhotoEditor.editText(rootView, inputText, styleBuilder)
                }
                mTxtCurrentTool.setText(R.string.label_text)
            }
        })
    }

    override fun onRemoveViewListener(viewType: ViewType?, numberOfAddedViews: Int) {
        Log.d(TAG, "onRemoveViewListener: ")
    }

    override fun onStartViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStartViewChangeListener: ")
    }

    override fun onStopViewChangeListener(viewType: ViewType?) {
        Log.d(TAG, "onStopViewChangeListener: ")
    }

    override fun onTouchSourceImage(event: MotionEvent?) {
        Log.d(TAG, "onTouchSourceImage: ")
    }

    protected fun showLoading() {
        binding.llProgress.llProgressBar.visible()
        binding.llProgress.pbText.text = getString(R.string.saving)
    }

    protected fun hideLoading() {
        binding.llProgress.llProgressBar.gone()
    }

    protected fun showSnackbar(message: String) {
        val view = findViewById<View>(android.R.id.content)
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    /*private fun requestCameraPermission() {

        when {
            ContextCompat.checkSelfPermission(
                this,
                PermissionEnum.CAMERA.permission
            ) == PackageManager.PERMISSION_GRANTED -> {

                Log.d(TAG, "requestCameraPermission - Camera Permission Granted")
                openCamera()
            }
            shouldShowRequestPermissionRationale(PermissionEnum.CAMERA.permission) -> {
                Log.d(TAG, "requestCameraPermission - Camera Permission NOT Granted")
                showPermissionAlert(
                    "Camera Permission", "This permission is required to camera image.",
                    "OK","Cancel"
                ) { requestCameraPermissionLauncher.launch(PermissionEnum.CAMERA.permission) }
            }
            else -> {

                showPermissionAlert(
                    "Camera Permission", "This permission is required to camera image.",
                    "OK","Cancel"
                ) {
                    openAppSettings(this)
                }

            }
        }
    }*/


    private val permissionLauncherForStorage =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            it?.let {
                if (it) {
                    if (galleryClicked) {
                        openGallery()
                        galleryClicked = false
                    } else {
                        saveImage()
                    }
                } else {
                    externalPermissionAlert()
                }
            }

        }

    private fun requestPermission() {
        if (!hasPermission(PermissionEnum.WRITE_EXTERNAL_STORAGE.permission)) {
            permissionLauncherForStorage.launch(PermissionEnum.WRITE_EXTERNAL_STORAGE.permission)
        }
    }

    private var requestCameraPermissionLauncherMulti =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            val deniedPermissionList = arrayListOf<String>()
            granted.entries.forEach {
                Log.d(TAG, "key: ${it.key} and value: ${it.value} ")
                if (shouldShowRequestPermissionRationale(it.key)) {
                    Log.d(TAG, "shouldShowRequestPermissionRationale: ")
                } else {
                    if (!it.value) {
                        when (it.key) {
                            PermissionEnum.CAMERA.permission -> {
                                deniedPermissionList.add(getString(R.string.camera))
                            }

                            PermissionEnum.WRITE_EXTERNAL_STORAGE.permission -> {
                                deniedPermissionList.add(getString(R.string.storage))
                            }

                        }
                    }
                }
            }
            if (deniedPermissionList.isNotEmpty()) {
                val permissionString =
                    java.lang.String.join(CommonConstant.COMMA_STRING, deniedPermissionList)
                showPermissionAlert(
                    getString(R.string.request_permission),
                    String.format(
                        getString(R.string.permission_is_required_to_function_properly),
                        permissionString
                    ),
                    getString(R.string.ok), getString(R.string.cancel)
                ) {
                    openAppSettings(this)
                }
            } else {
                openCamera()
            }
        }


    private var registerActivityForCamera: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                mPhotoEditor.clearAllViews()
                Log.d(
                    TAG, "cameraInfo ----: ${it.data?.data?.path}," +
                            "data: "
                )
                var inputImage = uriToBitmap(image_uri!!)
                inputImage = rotateBitmap(inputImage!!)
                mPhotoEditorView.source.setImageBitmap(inputImage)


            }
        }

    private var registerActivityForStorage: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == RESULT_OK) {
                try {
                    mPhotoEditor.clearAllViews()
                    val uri = it?.data?.data
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        contentResolver, uri
                    )
                    mPhotoEditorView.source.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    private fun rotateBitmap(input: Bitmap): Bitmap {
        val orientationColumn =
            arrayOf(MediaStore.Images.Media.ORIENTATION)
        val cur =
            contentResolver.query(image_uri!!, orientationColumn, null, null, null)
        var orientation = -1
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndexOrThrow(orientationColumn[0]))
        }
        Log.d("tryOrientation", orientation.toString() + "")
        val rotationMatrix = Matrix()
        rotationMatrix.setRotate(orientation.toFloat())
        return Bitmap.createBitmap(input, 0, 0, input.width, input.height, rotationMatrix, true)
    }

    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        registerActivityForCamera.launch(cameraIntent)
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            Log.e(TAG, "getRealPathFromURI Exception : $e")
            ""
        } finally {
            cursor?.close()
        }
    }

}