package com.example.backgroundremover_changebg.presentation.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel:ViewModel() {
    private val _originalBitmap = MutableStateFlow<Bitmap?>(null)
    val originalBitmap: StateFlow<Bitmap?> = _originalBitmap

    private val _bgRemovedBitmap = MutableStateFlow<Bitmap?>(null)
    val bgRemovedBitmap: StateFlow<Bitmap?> = _bgRemovedBitmap


    private val _croppedBitmap = MutableStateFlow<Bitmap?>(null)
    val croppedBitmap: StateFlow<Bitmap?> get() = _croppedBitmap

    fun updateCroppedImage(bitmap: Bitmap) {
        _croppedBitmap.value = bitmap
    }

    fun setOriginalBitmap(bitmap: Bitmap) {
        _originalBitmap.value = bitmap
    }

    fun setBgRemovedBitmap(bitmap: Bitmap) {
        _bgRemovedBitmap.value = bitmap
    }
}