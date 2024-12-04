//SubwayMapDataInstance
package com.example.myapplication

import android.content.Context
import android.util.Log
import androidx.compose.ui.geometry.Offset
import java.io.BufferedReader

object SubwayMapDataInstance {
    val stationCoordinates = mutableMapOf<Int, Offset>() // 역 번호와 좌표 저장
    val connections = mutableListOf<Triple<Int, Int, Int>>() // 역 연결 정보 (src, dst, line)

    private val lock = Any()

    // 초기화 상태를 확인하기 위한 변수
    var isInitialized = false
        private set // 외부에서 값을 변경하지 못하도록 설정

    fun initialize(context: Context) {
        // 데이터 중복 로드 방지
        if (!isInitialized) {
            synchronized(lock) { // 동기화 블록으로 스레드 충돌 방지
                if (!isInitialized) { // 다시 한 번 상태 확인
                    loadStationCoordinates(context)
                    loadConnections(context)
                    isInitialized = true
                    Log.d("SubwayMapDataInstance", "데이터 로드 완료")
                }
            }
        }
    }

    // 역 좌표 데이터 로드 함수
    private fun loadStationCoordinates(context: Context) {
        try {
            val inputStream = context.assets.open("map_coordinate.txt")
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    if (line.startsWith("src")) return@forEach // 헤더 건너뛰기
                    val parts = line.split(",")  // 쉼표로 데이터 분리
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
        Log.d("SubwayMapDataInstance", "loadConnections 완료")

        try {
            val inputStream = context.assets.open("stations.txt")
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    if (line.startsWith("src")) return@forEach // 헤더 건너뛰기
                    val parts = line.split(",") // 쉼표로 데이터 분리
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
