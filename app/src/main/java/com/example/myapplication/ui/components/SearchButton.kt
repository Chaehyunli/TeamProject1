package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SearchButton() {
    Button(
        onClick = { /* 검색 로직 */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252F42)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text("검색", fontSize = 14.sp, color = Color.White)
    }
}
