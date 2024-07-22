package com.jhy.giftmanagement;

import android.graphics.Bitmap
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.tasks.await

class BarcodeScanner {
    private val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                    Barcode.FORMAT_ALL_FORMATS
            )
            .build()

    private val scanner: BarcodeScanner = BarcodeScanning.getClient(options)

    suspend fun scanBarcodes(bitmap: Bitmap): List<String> {
        val image = InputImage.fromBitmap(bitmap, 0)
        return try {
            val result = scanner.process(image).await()
            result.mapNotNull { it.rawValue }
        } catch (e: Exception) {
            emptyList()
        }
    }
}