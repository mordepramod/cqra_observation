package com.example.observationapp.dashboard.presentationlayer.ui.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.media.ExifInterface
import android.os.AsyncTask
import android.util.Log
import com.example.observationapp.dashboard.presentationlayer.ui.listeners.ICompressImageListener
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ImageCompression : AsyncTask<String, Void, String>() {

    companion object {
        fun calculateInSampleSize(
            options: BitmapFactory.Options,
            actualWidth: Int,
            actualHeight: Int
        ): Int {

            return TODO("Provide the return value")
        }

        private const val TAG = "ImageCompression"
        private const val maxHeight = 1280.0f
        private const val maxWidth = 1280.0f

    }

    private var context: Context? = null
    private var listener: ICompressImageListener? = null

    fun ImageCompression(context: Context?, onSaveListener: ICompressImageListener) {
        this.context = context
        listener = onSaveListener
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        val totalPixels = (width * height).toFloat()
        val totalReqPixelsCap = (reqWidth * reqHeight * 2).toFloat()
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++
        }
        return inSampleSize
    }

    protected override fun onPreExecute() {
        this.onPreExecute()
    }

    protected override fun doInBackground(vararg strings: String?): String? {
        return if (strings.size == 0 || strings[0] == null) null else context?.let {
            drawTextToBitmap(
                it,
                strings[0]
            )
        }
    }

    override fun
            onPostExecute(imagePath: String) {
        Log.e(ImageCompression.TAG, "onPostExecute: $imagePath")
        listener!!.imageProcessed(imagePath)

        // imagePath is path of new compressed image.
    }

    fun drawTextToBitmap(
        gContext: Context,
        imagePath: String?
    ): String? {
        var bitmap: Bitmap? = null
        val df11 = SimpleDateFormat("dd/MM/yyyy  HH:mm", Locale.ENGLISH)
        val timestamp = df11.format(Date())
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio: Float = ImageCompression.maxWidth / ImageCompression.maxHeight
        if (actualHeight > ImageCompression.maxHeight || actualWidth > ImageCompression.maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = ImageCompression.maxHeight / actualHeight
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = ImageCompression.maxHeight.toInt()
            } else if (imgRatio > maxRatio) {
                imgRatio = ImageCompression.maxWidth / actualWidth
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = ImageCompression.maxWidth.toInt()
            } else {
                actualHeight = ImageCompression.maxHeight.toInt()
                actualWidth = ImageCompression.maxWidth.toInt()
            }
        }
        options.inSampleSize =
            ImageCompression.calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        options.inDither = false
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)
        try {
            bitmap = BitmapFactory.decodeFile(imagePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }
        val resources = gContext.resources
        val scale = resources.displayMetrics.density
        var bitmapConfig = bitmap!!.config
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }
        bitmap = bitmap.copy(bitmapConfig, true)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        /*Path mPath = new Path();
        RectF mRectF = new RectF(20, 20, 240, 240);
        mPath.addRect(mRectF, Path.Direction.CCW);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, paint);*/paint.color = Color.WHITE
        canvas.drawRect(5f, 5f, 350f, 80f, paint)
        paint.color = Color.RED
        // paint.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/DS-DIGI.TTF"));
        paint.textSize = (14 * scale).toInt().toFloat()
        paint.textAlign = Paint.Align.LEFT
        paint.style = Paint.Style.FILL
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE)
        val bounds = Rect()
        paint.getTextBounds(timestamp, 0, timestamp.length, bounds)
        /* int x = (bitmap.getWidth() - bounds.width()) / 2;
        int y = (bitmap.getHeight() + bounds.height()) / 2;*/
        val horizontalSpacing = 24
        val verticalSpacing = 36
        val x = horizontalSpacing //(bitmap.getWidth() - bounds.width()) / 2;
        val y = bitmap.height - verticalSpacing //(bitmap.getHeight() + bounds.height()) / 2;
        //canvas.drawText(timestamp, x, y, paint);
        canvas.drawText(timestamp, 10f, 50f, paint)
        // canvas.drawTextOnPath(timestamp, mPath, 0, 5, paint);
        val exif: ExifInterface
        try {
            exif = ExifInterface(imagePath!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            if (orientation == 6) {
                matrix.postRotate(90f)
            } else if (orientation == 3) {
                matrix.postRotate(180f)
            } else if (orientation == 8) {
                matrix.postRotate(270f)
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var out: FileOutputStream? = null
        val filepath: String = getFilename(imagePath)
        try {
            out = FileOutputStream(filepath)

            //write the compressed bitmap at the destination specified by filename.
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 80, out)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return filepath
    }

    fun getFilename(imagePath: String?): String {
        /* File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                                                + "/Android/data/"
                                                + context.getApplicationContext().getPackageName()
                                                + "/Files/Compressed");*/
        /* File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                                                + File.separator + "CQRA");*/
        val mediaStorageDir = File(imagePath)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs()
        }

        //String mImageName="IMG_"+ String.valueOf(System.currentTimeMillis()) +".jpg";
        // String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ mImageName);;
        return mediaStorageDir.absolutePath
    }


}

private fun Any.onPreExecute() {
    TODO("Not yet implemented")
}
