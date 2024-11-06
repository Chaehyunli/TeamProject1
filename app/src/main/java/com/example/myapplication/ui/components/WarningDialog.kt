package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WarningDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, // 다른 요소를 눌러도 닫히지 않게 설정

        title = null,
        text = {
            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF252F42),
                modifier = Modifier.padding(vertical = 4.dp) // 텍스트 여백
            )
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(
                        text = "확인",
                        fontSize = 16.sp,
                        color = Color(0xFF252F42)
                    )
                }
            }
        },
        containerColor = Color(0xFFCBD2DF), // 팝업 배경색 설정
        shape = RoundedCornerShape(12.dp), // 모서리 둥글기 설정
        modifier = Modifier.padding(horizontal = 32.dp)
            .fillMaxWidth(1f) // 너비 조절
            .border(1.dp, Color(0xFF252F42), RoundedCornerShape(10.dp)) // 테두리
    )
}
