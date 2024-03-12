package com.gmart.gmovies.utils

import android.util.Log
import androidx.lifecycle.ViewModel

fun Any.logD(message: String) = Log.d(this.javaClass.simpleName, message)

fun ViewModel.logD(message: String) = Log.d(this.javaClass.simpleName, message)

fun ViewModel.logE(message: String) = Log.e(this.javaClass.simpleName, message)