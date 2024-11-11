// SearchButton.kt
// 길찾기 검색 버튼
package com.example.myapplication.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SearchButton() {
    Button(
        onClick = { /* 검색 로직 */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF252F42)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, Color(0xFFcbd2df)),
        modifier = Modifier
            .padding(start = 8.dp)
            .height(40.dp)
    ) {
        Text("검색", fontSize = 14.sp, color = Color(0xFFcbd2df), fontWeight = FontWeight.Bold)
    }
}
