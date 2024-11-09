// StationDetailCard.kt
package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Divider
import androidx.compose.ui.text.style.TextAlign

@Composable
fun StationDetailDialog(
    stationName: String,
    transferInfo: String,
    transferDetailInfo: String
) {
    Card(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(16.dp, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .padding(bottom = 0.dp), // 화면 하단에 딱 맞게 배치
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 26.dp, end = 24.dp, bottom = 10.dp ),
//            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stationName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF97373),
//                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFF808590), thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = transferInfo,
                fontSize = 14.sp,
                color = Color(0xFF252f42),
                lineHeight = 20.sp,
//                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = transferDetailInfo,
                fontSize = 14.sp,
                color = Color(0xFF252f42),
                lineHeight = 20.sp,
//                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFF808590), thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // 출발/도착 버튼을 오른쪽 정렬하고 아이콘 스타일로 변경
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f)) // 오른쪽 정렬을 위한 Spacer
                Button(
                    onClick = { /* 출발 버튼 로직 */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0D3DC))
                ) {
                    Text(text = "출발", color = Color(0xFF252f42),fontWeight = FontWeight.Bold,)
                }
                Spacer(modifier = Modifier.width(7.dp))
                Button(
                    onClick = { /* 도착 버튼 로직 */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD0D3DC))
                ) {
                    Text(text = "도착", color = Color(0xFF252f42),fontWeight = FontWeight.Bold,)
                }
            }
        }
    }
}
