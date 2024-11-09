package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SortButton() {
    Button(
        onClick = { /* 정렬 로직 */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDFE3EB)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text("최단 거리 순", fontSize = 14.sp, color = Color(0xFF252F42))
    }
}
