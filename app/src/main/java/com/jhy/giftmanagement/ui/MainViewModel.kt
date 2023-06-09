package com.jhy.giftmanagement.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
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
import com.jhy.giftmanagement.data.GiftCategory
import com.jhy.giftmanagement.db.GiftInfo
import com.jhy.giftmanagement.db.GiftInfoDatabase
import com.jhy.giftmanagement.repo.GiftInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val giftInfoRepository: GiftInfoRepository
) : ViewModel() {

    fun getAllGift(): Flow<List<GiftInfo>> = giftInfoRepository.getAllGift()

    //bitmap이미지에서 바코드 가져오기 throw zxing.NotFoundException
    fun getBarcodeToImage(originalBMap: Bitmap) {
        viewModelScope.launch {
            getAllGift().collect() {
                Log.i("jhy","사이즈! ${it.size}")
            }
        }

        val bMap = if(Build.VERSION.SDK_INT > 25)
            originalBMap.copy(Bitmap.Config.RGBA_F16, true)
        else originalBMap

        var contents: String? = null
        val intArray = IntArray(bMap.width * bMap.height)
        bMap.getPixels(intArray, 0, bMap.width, 0, 0, bMap.width, bMap.height)
        val source: LuminanceSource = RGBLuminanceSource(bMap.width, bMap.height, intArray)
        val bitmap = BinaryBitmap(HybridBinarizer(source))
        val reader: Reader = MultiFormatReader()

        val result = reader.decode(bitmap)
        contents = result.text
        Log.i("jhy!!", contents)
    }

    //코드를 바코드 비트맵으로 변환
    fun generateBarcodeToString(code : String) : ImageBitmap {
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
