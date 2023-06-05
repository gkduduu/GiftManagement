package com.jhy.giftmanagement.ui

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.LuminanceSource
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.oned.Code128Writer
import com.jhy.giftmanagement.ui.theme.GiftManagementTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiftManagementTheme {
                var AddDialogState by remember {
                    mutableStateOf(false)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Image(bitmap = generateBarcodeToString("50913376868621"), contentDescription = "")
                    GiftList()

                    // 다이얼로그 표시
                    if (AddDialogState) {
                        GiftAddDialog() // 다이얼로그 컴포즈
                        { AddDialogState = false } // 다이얼로그를 숨기는 unit 함수를 인자로 줌
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        FloatingActionButton(
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .align(alignment = Alignment.BottomEnd),
                            onClick = {
                                AddDialogState = true
                                loadGallery()
                            }

                        ) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        }
                    }
                }
            }
        }
    }

    //갤러리 실행
    fun loadGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        photoChooseActivityLauncher.launch(intent)
    }

    //사진 선택 후 bitmap으로 변환
    private val photoChooseActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {
                var currentImageUri = it.data?.data
                var bMap: Bitmap
                try {
                    currentImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            bMap = MediaStore.Images.Media.getBitmap(this.contentResolver, currentImageUri)
                        } else {
                            val source =
                                ImageDecoder.createSource(this.contentResolver, currentImageUri)
                            bMap = ImageDecoder.decodeBitmap(source)
                        }
                        getBarcodeToImage(bMap)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
//            else if (it.resultCode == RESULT_CANCELED) {
//            }
        }

    //bitmap이미지에서 바코드 가져오기 throw zxing.NotFoundException
    fun getBarcodeToImage(originalBMap: Bitmap) {
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
