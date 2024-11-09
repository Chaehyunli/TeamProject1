package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class) // 실험적 API 사용을 명시
@Composable
fun SearchInputField() {
    Column {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("출발지 입력") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF252F42),
                unfocusedBorderColor = Color.LightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("도착지 입력") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF252F42),
                unfocusedBorderColor = Color.LightGray
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
