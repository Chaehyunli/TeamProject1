// StationDetailDialog.kt
package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StationDetailDialog(
    stationName: String,
    transferInfo: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter // Box 안에서 하단 중앙에 카드 배치
    ) {
        Card(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = stationName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF97373)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = transferInfo,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))

                // 출발/도착 버튼
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { /* 출발 버튼 로직 */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0D3DC))
                    ) {
                        Text(text = "출발", color = Color.Black)
                    }
                    Button(
                        onClick = { /* 도착 버튼 로직 */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0D3DC))
                    ) {
                        Text(text = "도착", color = Color.Black)
                    }
                }
            }
        }
    }
}
