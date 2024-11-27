// StationDetailDialog.kt
package com.example.myapplication.ui.components

import androidx.compose.foundation.border
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
    stationName: String,           // 역 이름
    transferInfo: String,          // 환승 정보
    transferDetailInfo: String,    // 인접 역 정보
    onStartClick: () -> Unit,      // 출발 버튼 클릭 이벤트
    onEndClick: () -> Unit         // 도착 버튼 클릭 이벤트
) {
    Card(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 0.dp)
            .border(0.5.dp, Color(0xFF252f42), shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 26.dp, end = 24.dp, bottom = 10.dp),
        ) {
            Text(
                text = stationName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF97373)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color(0xFF808590), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = transferInfo,
                fontSize = 14.sp,
                color = Color(0xFF252f42),
                lineHeight = 20.sp,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = transferDetailInfo,
                fontSize = 14.sp,
                color = Color(0xFF252f42),
                lineHeight = 20.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color(0xFF808590), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // 출발/도착 버튼
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onStartClick, // 출발 버튼 클릭 시 onStartClick 실행
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0D3DC))
                ) {
                    Text(text = "출발", color = Color(0xFF252f42), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Button(
                    onClick = onEndClick, // 도착 버튼 클릭 시 onEndClick 실행
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0D3DC))
                ) {
                    Text(text = "도착", color = Color(0xFF252f42), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

