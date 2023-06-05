package com.jhy.giftmanagement.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jhy.giftmanagement.data.GiftCategory
import com.jhy.giftmanagement.data.GiftInfo
import java.util.Date

@Composable
fun GiftList() {
    val numbers = (0..20).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(numbers.size) {
            GiftDetail(giftInfo = GiftInfo(1,"","","", Date(),"",GiftCategory.BAKERY))
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