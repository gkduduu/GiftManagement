package com.jhy.giftmanagement.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.coroutineScope
import com.jhy.giftmanagement.db.GiftInfoDatabase
import com.jhy.giftmanagement.ui.theme.GiftManagementTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var giftInfoDatabase: GiftInfoDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GiftManagementTheme {
                Main(viewModel,this)

            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.getAllGift().collect() {
            }
        }
    }

    //사진 선택 후 bitmap으로 변환
    val photoChooseActivityLauncher: ActivityResultLauncher<Intent> =
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
                        viewModel.getBarcodeToImage(bMap)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
//            else if (it.resultCode == RESULT_CANCELED) {
//            }
        }
}