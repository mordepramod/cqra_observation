package com.example.observationapp.dashboard.presentationlayer.ui.activity

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.observationapp.Utils.IToastUtils
import javax.inject.Inject

class BaseActivity<BaseActivityComponent> :AppCompatActivity() {
    protected var component: BaseActivityComponent? = null

    var progressDialog: AlertDialog? = null




}