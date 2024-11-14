// StationDataLoader.kt
package com.example.myapplication.ui.components

import android.content.Context
import androidx.compose.ui.geometry.Offset
import java.io.BufferedReader
import java.io.InputStreamReader

// 각 역의 좌표를 파일에서 읽어오는 함수
fun loadStationCoordinates(context: Context): Map<Int, Offset> {
    val coordinates = mutableMapOf<Int, Offset>()
    val inputStream = context.assets.open("map_coordinate.txt")
    val reader = BufferedReader(InputStreamReader(inputStream))

    // 파일의 각 줄을 읽어 좌표 정보 추가
    reader.forEachLine { line ->
        if (line.startsWith("src")) return@forEachLine  // 헤더 건너뜀

        val parts = line.split(",")
        if (parts.size == 3) {
            val stationId = parts[0].toIntOrNull()
            val x = parts[1].toFloatOrNull()
            val y = parts[2].toFloatOrNull()

            if (stationId != null && x != null && y != null) {
                coordinates[stationId] = Offset(x, y)
            }
        }
    }
    reader.close()
    return coordinates
}
