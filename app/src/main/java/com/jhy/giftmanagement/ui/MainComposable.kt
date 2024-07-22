package com.jhy.giftmanagement.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.jhy.giftmanagement.R
import com.jhy.giftmanagement.data.GiftCategory
import com.jhy.giftmanagement.db.GiftInfo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Main(viewModel : MainViewModel, activity: MainActivity) {
    var AddDialogState by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
//        Image(bitmap = viewModel.generateBarcodeToString("50913376868621"), contentDescription = "")
        GiftList(viewModel)

        // 다이얼로그 표시
        if (AddDialogState) {
            GiftAddDialog(viewModel,activity) // 다이얼로그 컴포즈
            { AddDialogState = false } // 다이얼로그를 숨기는 unit 함수를 인자로 줌
        }

        Box(modifier = Modifier.fillMaxSize()) {
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .align(alignment = Alignment.BottomEnd),
                onClick = {
                    AddDialogState = true
//                    loadGallery(activity)
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
fun GiftList(viewModel: MainViewModel) {

    val giftItems by viewModel.giftItems.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(giftItems.size) {
            GiftDetail(giftInfo = giftItems[it],viewModel)
        }
    }
}
@Composable
fun GiftDetail(giftInfo: GiftInfo,viewModel: MainViewModel) {
    Column {
        Box(modifier = Modifier.height(300.dp)) {

            Image(
                bitmap = viewModel.generateBarcodeToString(giftInfo.giftBarcode),
                contentDescription = "Gift Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.wrapContentSize()
            )
        }
        Text(text = giftInfo.giftTitle)
        Text(text = giftInfo.giftExpiredDate)
    }
}


@Composable
fun GiftAddDialog(viewModel: MainViewModel,activity: MainActivity,onChangeState: () -> Unit) {
//    val giftContent : String,
//    val giftURL : String,
    var addTitle by remember { mutableStateOf("") }
    var addExpDate by remember { mutableStateOf("") }
    var addCategory by remember { mutableStateOf(GiftCategory.OTHER) }
    val addBarcode by viewModel.barcodeString.collectAsState()

    AlertDialog(
        onDismissRequest = {  },
        title = { Text(text = "추가", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
        text = {
            LazyColumn {
                item {
                    ImagePickerScreen(viewModel, activity)
                    TextAndInput("설명", addTitle, onTextChange = { addTitle = it })
                    GiftCategorySelector { category ->
                        addCategory = category
                        // 여기서 선택된 카테고리로 추가 작업을 수행할 수 있습니다.
                    }
                    DatePickerButton(onDateSelected = { dateString ->
                        addExpDate = dateString
                    })
                }
            }
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
                    viewModel.addGift(
                        GiftInfo(addTitle,"","",addExpDate,addBarcode,addCategory)
                    )
                }) {
                Text(text = "확인", color = Color.Black)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextAndInput(hint : String, text: String,
                 onTextChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(15.dp)) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text(hint) },
            modifier = Modifier.fillMaxWidth()
        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Text("입력된 텍스트: $text")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftCategorySelector(onCategorySelected: (GiftCategory) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<GiftCategory?>(null) }

    Column(modifier = Modifier.padding(15.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                readOnly = true,
                value = selectedCategory?.categoryName ?: "카테고리 선택",
                onValueChange = { },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                GiftCategory.values().forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.categoryName) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                            onCategorySelected(category)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(
    onDateSelected: (String) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val formatter = remember { DateTimeFormatter.ofPattern("yyyy-MM-dd") }

    Column( modifier = Modifier.padding(15.dp),horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { showDatePicker = true }) {
            Text("만료일 선택")
        }

        Spacer(modifier = Modifier.height(8.dp))

        selectedDate?.let { date ->
            Text("만료일: ${date.format(formatter)}")
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                        selectedDate?.let { date ->
                            onDateSelected(date.format(formatter))
                        }
                    }
                    showDatePicker = false
                }) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("취소")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun ImagePickerScreen(
    viewModel: MainViewModel,
    activity: MainActivity
) {
    val context = LocalContext.current
    val imageUri by viewModel.imageUri.collectAsState()
    val imageRatio by viewModel.imageRatio.collectAsState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        val imageWidth = maxWidth
        val imageHeight = if (imageRatio > 1f) {
            imageWidth / imageRatio
        } else {
            maxWidth.coerceAtMost(600.dp)
        }

        Box(
            modifier = Modifier.size(imageWidth, imageHeight)
        ) {
            Image(
                painter = if (imageUri != null) {
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(imageUri)
                            .size(Size.ORIGINAL)
                            .build()
                    )
                } else {
                    painterResource(id = R.drawable.default_image)
                },
                contentDescription = "Selected image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
            Button(onClick = { loadGallery(activity) }) {
                Text("갤러리에서 이미지 선택")
            }
        }
    }

//    val launcher = activity.photoChooseActivityLauncher

//    Column(
//        modifier = Modifier
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            modifier = Modifier
//                .size(600.dp)
//                .padding(8.dp)
//        ) {
//            Image(
//                painter = if (imageUri != null) {
//                    rememberAsyncImagePainter(
//                        ImageRequest.Builder(context).data(data = imageUri).build()
//                    )
//                } else {
//                    painterResource(id = R.drawable.default_image)
//                },
//                contentDescription = "Selected image",
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//
//    }
}