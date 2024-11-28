//ConfrimationDialog.kt
package com.example.myapplication.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmationDialog(
    message: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 24.dp) // 화면 양쪽에서 여백
            .widthIn(max = 300.dp) // 최대 너비 조정
    ) {
        AlertDialog(
            onDismissRequest = {}, // 외부 클릭 시 닫히지 않도록 설정
            title = null,
            text = {
                Text(
                    text = message,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF252F42),
                    maxLines = 1, // 텍스트를 한 줄로 제한
                    overflow = TextOverflow.Ellipsis, // 한 줄을 넘어갈 경우 말줄임표(...)로 표시
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm // 확인 버튼 클릭 시 실행
                ) {
                    Text(
                        text = "확인",
                        fontSize = 16.sp,
                        color = Color(0xFF252F42),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel // 취소 버튼 클릭 시 실행
                ) {
                    Text(
                        text = "취소",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = Color(0xFFCBD2DF),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .widthIn(max = 300.dp) // 다이얼로그 자체의 최대 너비 조정
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp))
        )
    }
}
