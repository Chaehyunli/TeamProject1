package com.example.myapplication

import android.content.Context
import androidx.compose.ui.geometry.Offset
import java.io.BufferedReader

object SubwayMapDataInstance {

    val stationCoordinates = mutableMapOf<Int, Offset>() // 역 번호와 좌표 저장
    val connections = mutableListOf<Triple<Int, Int, Int>>() // 역 연결 정보 (src, dst, line)

    // 초기화 함수
    fun initialize(context: Context) {
        loadStationCoordinates(context)
        loadConnections(context)
    }

    private fun loadStationCoordinates(context: Context) {
        try {
            val inputStream = context.assets.open("map_coordinate.txt")
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    if (line.startsWith("src")) return@forEach // 헤더 건너뛰기
                    val parts = line.split(",")
                    val stationId = parts[0].toIntOrNull() ?: return@forEach
                    val x = parts[1].toFloatOrNull() ?: return@forEach
                    val y = parts[2].toFloatOrNull() ?: return@forEach
                    stationCoordinates[stationId] = Offset(x, y)
                }
            }
        } catch (e: Exception) {
            println("역 좌표 데이터를 로드하는 중 오류 발생: ${e.message}")
        }
    }

    private fun loadConnections(context: Context) {
        try {
            val inputStream = context.assets.open("stations.txt")
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    if (line.startsWith("src")) return@forEach // 헤더 건너뛰기
                    val parts = line.split(",")
                    val src = parts[0].toIntOrNull() ?: return@forEach
                    val dst = parts[1].toIntOrNull() ?: return@forEach
                    val lineNumber = parts[5].toIntOrNull() ?: return@forEach
                    connections.add(Triple(src, dst, lineNumber))
                }
            }
        } catch (e: Exception) {
            println("역 연결 데이터를 로드하는 중 오류 발생: ${e.message}")
        }
    }
}
