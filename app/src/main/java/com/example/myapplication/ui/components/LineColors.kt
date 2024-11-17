package com.example.myapplication.ui.components

import androidx.compose.ui.graphics.Color

val LineColors = mapOf(
    1 to Color(0xFF00B050), // 1호선
    2 to Color(0xFF002060), // 2호선
    3 to Color(0xFF953735), // 3호선
    4 to Color(0xFFFF0000), // 4호선
    5 to Color(0xFF4A7EBB), // 5호선
    6 to Color(0xFFFFC514), // 6호선
    7 to Color(0xFF92D050), // 7호선
    8 to Color(0xFF00B0F0), // 8호선
    9 to Color(0xFF7030A0)  // 9호선
)

// 각 노선 번호에 따른 색상 반환 함수
fun getLineColor(lineNumber: Int): Color {
    return LineColors[lineNumber] ?: Color.Gray
}
