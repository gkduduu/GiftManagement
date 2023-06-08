package com.jhy.giftmanagement.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jhy.giftmanagement.data.GiftCategory
import com.jhy.giftmanagement.db.GiftInfo
import java.util.Date

@Composable
fun Main(viewModel : MainViewModel, activity: MainActivity) {
    var AddDialogState by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Image(bitmap = viewModel.generateBarcodeToString("50913376868621"), contentDescription = "")
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
                    loadGallery(activity)
                }

            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
            }
        }
    }
}

//갤러리 실행
fun loadGallery(activity: MainActivity) {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.setType("image/*")
    activity.photoChooseActivityLauncher.launch(intent)
}

@Composable
fun GiftList() {
    val numbers = (0..20).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(numbers.size) {
            GiftDetail(giftInfo = GiftInfo(1,"","","", "","",GiftCategory.BAKERY))
        }
    }
}
@Composable
fun GiftDetail(giftInfo: GiftInfo) {
    Column {
        Box(modifier = Modifier.height(300.dp))
        Text(text = "품목명")
        Text(text = "2023/05/30 까지")
    }
}

@Composable
fun GiftAddDialog(onChangeState: () -> Unit) {
    AlertDialog(
        onDismissRequest = {  },
        title = { Text(text = "추가", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        text = {
               //TODO : 추가화면 만들기
        },
        dismissButton = {
            TextButton(onClick = { onChangeState() }) {
                Text(text = "취소", color = Color.Gray)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onChangeState()
                }) {
                Text(text = "확인", color = Color.Black)
            }
        }
    )

}