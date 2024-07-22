package com.jhy.giftmanagement.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.oned.Code128Writer
import com.jhy.giftmanagement.BarcodeScanner
import com.jhy.giftmanagement.data.GiftCategory
import com.jhy.giftmanagement.db.GiftInfo
import com.jhy.giftmanagement.db.GiftInfoDatabase
import com.jhy.giftmanagement.repo.GiftInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val giftInfoRepository: GiftInfoRepository
) : ViewModel() {

    private val barcodeScanner = BarcodeScanner()
    private val _scannedBarcodes = MutableStateFlow<List<String>>(emptyList())
    val scannedBarcodes: StateFlow<List<String>> = _scannedBarcodes.asStateFlow()

    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri.asStateFlow()

    private val _barcodeString = MutableStateFlow<String?>(null)
    val barcodeString: StateFlow<String?> = _barcodeString.asStateFlow()

    private val _imageRatio = MutableStateFlow(1f)
    val imageRatio: StateFlow<Float> = _imageRatio

    private val _items = MutableStateFlow<List<GiftInfo>>(emptyList())
    val giftItems: StateFlow<List<GiftInfo>> = _items.asStateFlow()

    fun setImage(uri: Uri?, context: Context) {
        _imageUri.value = uri
        viewModelScope.launch {
            uri?.let {
                val ratio = withContext(Dispatchers.IO) {
                    context.contentResolver.openInputStream(it)?.use { stream ->
                        val options = BitmapFactory.Options().apply {
                            inJustDecodeBounds = true
                        }
                        BitmapFactory.decodeStream(stream, null, options)
                        options.outWidth.toFloat() / options.outHeight.toFloat()
                    } ?: 1f
                }
                _imageRatio.value = ratio
            }
        }
    }

    fun getAllGift() {
        viewModelScope.launch {
            giftInfoRepository.getAllGift().collect { newItems ->
                _items.value = newItems
            }
        }
    }
    fun addGift(info:GiftInfo) = viewModelScope.launch {
        giftInfoRepository.insertGift(info)
    }

    //bitmap이미지에서 바코드 가져오기 throw zxing.NotFoundException
    fun getBarcodeToImage(originalBMap: Bitmap) {
        viewModelScope.launch {
            val barcodes = barcodeScanner.scanBarcodes(originalBMap)
            _scannedBarcodes.value = barcodes

            if (_scannedBarcodes.value.isNotEmpty()) {
                _scannedBarcodes.value.forEach { barcode ->
                    Log.i("jhy!!", barcode)
                    _barcodeString.value = barcode
                }
            } else {
                Log.i("jhy!!", "no barcode!!!!!")
            }
        }

//        val bMap = if(Build.VERSION.SDK_INT > 25)
//            originalBMap.copy(Bitmap.Config.RGBA_F16, true)
//        else originalBMap
//
//        var contents: String? = null
//        val intArray = IntArray(bMap.width * bMap.height)
//        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)
//        val source: LuminanceSource = RGBLuminanceSource(bMap.width, bMap.height, intArray)
//        val bitmap = BinaryBitmap(HybridBinarizer(source))
//        val reader: Reader = MultiFormatReader()
//
//        val result = reader.decode(bitmap)
//        contents = result.text
//        Log.i("jhy!!", contents)
    }

    //코드를 바코드 비트맵으로 변환
    fun generateBarcodeToString(code : String?) : ImageBitmap {
        val bitMatrix = Code128Writer().encode(
            code,
            BarcodeFormat.CODE_128,
            300.toPx,
            65.toPx
        )

        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )

        return bitmap.asImageBitmap()
    }


    //dp를 px Int로 변환
    val Number.toPx get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics).toInt()
}
