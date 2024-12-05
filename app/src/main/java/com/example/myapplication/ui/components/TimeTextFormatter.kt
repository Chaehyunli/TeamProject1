//TimeTextFormatter.kt
package com.example.myapplication.ui.components

import androidx.compose.runtime.Composable

@Composable
fun TimeTextFormatter(
    time: Int // 초 단위의 시간을 입력
): String {
    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = time % 60
    val formattedSeconds = seconds.toString().padStart(2, '0') // 출력할 때만 문자열 변환, eg) 02초

    return when {
        hours > 0 && minutes == 0 && seconds == 0 -> "${hours}시간" // 정시만 표시
        hours > 0 && minutes > 0 && seconds == 0 -> "${hours}시간 ${minutes}분" // 시간과 분만 표시 (초 0)
        hours > 0 && minutes == 0 && seconds > 0 -> "${hours}시간 ${formattedSeconds}초" // 시와 초만 표시
        hours == 0 && minutes > 0 && seconds == 0 -> "${minutes}분" // 분만 표시 (시간과 초 0)
        hours == 0 && minutes == 0 && seconds > 0 -> "${formattedSeconds}초" // 초만 표시 (시간과 분 0)
        hours > 0 && minutes > 0 && seconds > 0 -> "${hours}시간 ${minutes}분 ${formattedSeconds}초" // 전부 표시
        hours == 0 && minutes > 0 && seconds > 0 -> "${minutes}분 ${formattedSeconds}초" // 분과 초만 표시
        else -> "${formattedSeconds}초" // 기본값으로 초만 표시
    }
}
