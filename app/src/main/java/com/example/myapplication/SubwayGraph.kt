// SubwayGraph.kt
package com.example.myapplication

import java.util.*
import android.content.Context
import kotlin.String

// 지하철 역,노드, 간선 및 메인 함수가 있는 파일

// Node 클래스 정의: 역 번호와 해당 역의 연결 정보 저장
class Node(val stationNumber: Int) {
    val lineNumbers = mutableSetOf<Int>()  // 다중 노선 번호를 저장할 집합
    val neighbors = mutableListOf<Edge>()  // 인접 역과의 연결 정보 리스트

    // 노선 번호 추가
    fun addLineNumber(lineNumber: Int) {
        lineNumbers.add(lineNumber)
    }

    // 인접 역 추가
    fun addNeighbor(destination: Int, distance: Int, cost: Int, time: Int, line: Int) {
        val edge = Edge(destination, distance, cost, time, line)
        neighbors.add(edge)
    }

    override fun toString(): String {
        val neighborsStr = neighbors.joinToString(", ")
        return "역 $stationNumber (노선: ${lineNumbers.joinToString(", ")}) -> 연결된 역: [$neighborsStr]"
    }
}

// Edge 클래스 정의: 인접 역과의 연결 및 이동 정보
data class Edge(
    val destination: Int,   // 인접 역 번호
    val distance: Int,      // 거리
    val cost: Int,          // 비용
    val time: Int,          // 시간
    val line: Int           // 노선 번호
) {
    override fun toString(): String {
        return "(목적지: $destination, 거리: ${distance}m, 비용: ${cost}원, 시간: ${time}초, 노선: $line)"
    }
}

// SubwayGraph 클래스 정의: 역을 관리하고 그래프 구축
class SubwayGraph {
    val stations = mutableMapOf<Int, Node>() // 전체 역을 저장할 맵

    fun getAllStationNumbers(): List<Int> {
        return stations.keys.toList()
    }

    // 역 추가 및 노선 번호 추가
    fun addStation(stationNumber: Int, lineNumber: Int) {
        val station = stations.getOrPut(stationNumber) { Node(stationNumber) }
        station.addLineNumber(lineNumber)
    }

    // 두 역 간의 연결 추가
    fun addConnection(station1: Int, station2: Int, distance: Int, cost: Int, time: Int, line: Int) {
        // 역과 노선 번호 추가
        addStation(station1, line)
        addStation(station2, line)

        stations[station1]?.addNeighbor(station2, distance, cost, time, line)
        stations[station2]?.addNeighbor(station1, distance, cost, time, line) // 양방향 연결
    }

    // 지정된 역 번호의 인접 역 목록을 반환하는 메소드
    fun getNeighbors(stationNumber: Int): List<Edge>? {
        return stations[stationNumber]?.neighbors
    }

    fun printTotalStations() { // 전체 역 수 출력(디버깅용)
        println("전체 역의 개수: ${stations.size}")
    } 

    // AssetManager를 이용해 파일을 로드하는 메소드
    fun loadDataFromAssets(context: Context) {
        try {
            val inputStream = context.assets.open("stations.txt")
            inputStream.bufferedReader().useLines { lines ->
                lines.forEach { line ->
                    println("읽은 줄: $line")
                    if (line.startsWith("src")) return@forEach // 첫 줄(헤더)은 건너뜀

                    val row = line.split(",")
                    val src = row.getOrNull(0)?.toIntOrNull() ?: return@forEach
                    val dst = row.getOrNull(1)?.toIntOrNull() ?: return@forEach
                    val time = row.getOrNull(2)?.toIntOrNull() ?: return@forEach
                    val distance = row.getOrNull(3)?.toIntOrNull() ?: return@forEach
                    val cost = row.getOrNull(4)?.toIntOrNull() ?: return@forEach
                    val line = row.getOrNull(5)?.toIntOrNull() ?: return@forEach

                    addConnection(src, dst, distance, cost, time, line)
                }
            }
            println("데이터가 성공적으로 로드되었습니다.")
        } catch (e: Exception) {
            println("파일을 읽는 중 오류 발생: ${e.message}")
        }
    }

    // 그래프 출력 (디버깅용)
    fun printGraph() {
        for (station in stations.values) {
            println(station)
        }
    }
}

// 나머지 SubwayGraph 클래스 내용 유지 -> SubwayGraphInstance.subwayGraph 객체로 지하철 노드 접근
object SubwayGraphInstance {
    private var _subwayGraph: SubwayGraph? = null

    val subwayGraph: SubwayGraph
        get() = _subwayGraph ?: throw IllegalStateException("SubwayGraph has not been initialized. Call initialize() first.")

    private val routeFinder: RouteFinder by lazy { RouteFinder(subwayGraph) }

    // initialize 메서드에서 subwayGraph를 초기화
    fun initialize(context: Context) {
        _subwayGraph = SubwayGraph().apply {
            loadDataFromAssets(context)
        }
        println("SubwayGraph 데이터가 성공적으로 초기화되었습니다.")
    }

    // 특정 노선의 역들을 순서대로 가져오는 함수
    fun getStationsByLineInOrder(lineNumber: Int): List<Int> {
        val visited = mutableSetOf<Int>() // 방문한 역을 저장
        val stationsInLine = LinkedList<Int>() // 노선의 역을 순서대로 저장

        // 깊이 우선 탐색(DFS) 함수 정의
        fun dfs(station: Int) {
            if (station in visited) return // 이미 방문한 역이면 종료
            visited.add(station)
            stationsInLine.add(station)

            // 현재 역의 인접 역을 탐색
            val neighbors = subwayGraph.stations[station]?.neighbors ?: return
            neighbors.filter { it.line == lineNumber && it.destination !in visited }
                .forEach { dfs(it.destination) } // 같은 노선의 인접 역 탐색
        }

        // 각 노선의 시작점을 설정 (예: 601번)
        val firstStationNumber = lineNumber * 100 + 1
        var isCircularLine = false // 순환 노선인지 여부 확인

        // 순환 구조 여부 확인
        val startingNeighbors = subwayGraph.stations[firstStationNumber]?.neighbors
        if (startingNeighbors != null) {
            val visitedForCycleCheck = mutableSetOf<Int>()
            fun checkCycle(station: Int) {
                if (station in visitedForCycleCheck) {
                    isCircularLine = true
                    return
                }
                visitedForCycleCheck.add(station)
                subwayGraph.stations[station]?.neighbors
                    ?.filter { it.line == lineNumber && it.destination !in visitedForCycleCheck }
                    ?.forEach { checkCycle(it.destination) }
            }
            checkCycle(firstStationNumber)
        }

        // 순환 구조인 경우에만 지정된 첫 번째 역에서 탐색 시작
        if (isCircularLine) {
            dfs(firstStationNumber)
        } else {
            // 순환 구조가 아닐 경우, 노선의 임의의 한 역에서 DFS 시작
            val anyStartingStation = subwayGraph.stations.values
                .firstOrNull { lineNumber in it.lineNumbers }
            anyStartingStation?.let { dfs(it.stationNumber) }
        }

        // 첫 번째 역 번호의 위치에 따라 리스트 반전
        val midpoint = stationsInLine.size / 2
        if (stationsInLine.indexOf(firstStationNumber) > midpoint) {
            stationsInLine.reverse()
        }

        return stationsInLine.distinct() // 중복 제거 후 반환
    }

    // 개별 경로에 접근할 수 있는 getter 함수 추가(디버깅용)
    fun getShortestTimeRoute(startStation: Int, endStation: Int): RouteFinder.RouteInfo? {
        return routeFinder.findShortestTimePath(startStation, endStation)?.apply { criteria.add("최소 시간") }
    }

    fun getShortestDistanceRoute(startStation: Int, endStation: Int): RouteFinder.RouteInfo? {
        return routeFinder.findShortestDistancePath(startStation, endStation)?.apply { criteria.add("최단 거리") }
    }

    fun getCheapestRoute(startStation: Int, endStation: Int): RouteFinder.RouteInfo? {
        return routeFinder.findCheapestPath(startStation, endStation)?.apply { criteria.add("최소 비용") }
    }

    fun getFewestTransfersRoute(startStation: Int, endStation: Int): RouteFinder.RouteInfo? {
        return routeFinder.findFewestTransfersPath(startStation, endStation)?.apply { criteria.add("최소 환승") }
    }

    fun findUniqueRoutes(startStation: Int, endStation: Int): List<RouteFinder.RouteInfo> {
        // 각 기준별 경로 탐색
        val shortestTimeRoute = routeFinder.findShortestTimePath(startStation, endStation).apply { criteria.add("최소 시간") }
        val shortestDistanceRoute = routeFinder.findShortestDistancePath(startStation, endStation).apply { criteria.add("최단 거리") }
        val cheapestRoute = routeFinder.findCheapestPath(startStation, endStation).apply { criteria.add("최소 비용") }
        val fewestTransfersRoute = routeFinder.findFewestTransfersPath(startStation, endStation).apply { criteria.add("최소 환승") }

        var routes = listOf(shortestTimeRoute, shortestDistanceRoute, cheapestRoute, fewestTransfersRoute)

        // 더 적은 환승 횟수 경로 있으면 업데이트
        var minTransfers = fewestTransfersRoute.transfers
        routes.filter { it != fewestTransfersRoute && it.transfers < minTransfers }.forEach { route ->
            route.criteria.add("최소 환승")
        }

        // routes에 더 적은 환승 횟수를 가진 다른 경로가 없을 경우에만 유지
        routes = routes.filter { it != fewestTransfersRoute || routes.none { it != fewestTransfersRoute && it.transfers < minTransfers } }

        // 경로 중복 제거
        val uniqueRoutes = mutableListOf<RouteFinder.RouteInfo>()
        routes.forEach { newRoute ->
            val duplicate = uniqueRoutes.find { it.path == newRoute.path }
            if (duplicate != null) {
                // 기존 경로와 새 경로가 같은 경우, 기준을 추가로 병합
                duplicate.criteria.addAll(newRoute.criteria)
            } else {
                // 중복되지 않으면 새로운 경로로 추가
                uniqueRoutes.add(newRoute)
            }
        }

        // 모든 기준(최소 시간, 최단 거리, 최소 비용, 최소 환승)에 대해 처리
        val criteriaList = listOf(
            "최소 시간" to { route: RouteFinder.RouteInfo -> route.time },
            "최단 거리" to { route: RouteFinder.RouteInfo -> route.distance },
            "최소 비용" to { route: RouteFinder.RouteInfo -> route.cost },
            "최소 환승" to { route: RouteFinder.RouteInfo -> route.transfers }
        )

        criteriaList.forEach { (criterion, criterionSelector) ->
            // 해당 기준을 가진 경로 탐색
            val referenceRoute = uniqueRoutes.minByOrNull(criterionSelector) // 기준에 따른 최소값 경로 찾기
            if (referenceRoute != null) {
                // 동일한 기준을 만족하는 경로 업데이트
                uniqueRoutes.filter { it != referenceRoute && criterionSelector(it) == criterionSelector(referenceRoute) }.forEach {
                    it.criteria.add(criterion) // 기준 추가
                }
                // 기준이 만족되는 경로에도 기준 추가
                referenceRoute.criteria.add(criterion)
            }
        }

        return uniqueRoutes
    }

    // 약소 장소 추천 기능 결과 반환 데이터
    data class MeetingPlaceResult(
        val bestStation: Int,
        val timesFromStartStations: List<Int>
    )

    // 출발역들에서 최소 시간 기준으로 중앙에 가까운 최적의 만날 지하철 역 계산
    fun calculateMeetingPlaceRoute(startStations: List<String>): MeetingPlaceResult? {
        val stationNumbers = startStations.mapNotNull { it.toIntOrNull() }
        if (stationNumbers.isEmpty()) return null

        var bestStation = -1
        var minTotalScore = Int.MAX_VALUE
        var bestTimesFromStartStations = listOf<Int>()

        // 모든 역 번호에 대해 최적의 만날 역을 찾기
        for (candidate in subwayGraph.getAllStationNumbers()) {
            // 각 출발역에서 후보 역까지의 최단 시간 리스트 계산
            val timesFromStartStations = stationNumbers.map { startStation ->
                routeFinder.findShortestTimePath(startStation, candidate)?.time ?: Int.MAX_VALUE
            }

            // 시간 합과 분산을 고려하여 중앙 지점에 가까운 최적의 역 결정
            val totalTravelTime = timesFromStartStations.sum()
            val timeVariance = calculateTimeVariance(timesFromStartStations)
            val totalScore = totalTravelTime + timeVariance  // 총 이동 시간과 분산을 함께 고려

            if (totalScore < minTotalScore) {
                minTotalScore = totalScore
                bestStation = candidate
                bestTimesFromStartStations = timesFromStartStations
            }
        }
        return MeetingPlaceResult(bestStation, bestTimesFromStartStations)
    }

    // 시간 리스트의 분산을 계산하여 시간 차이 평가
    private fun calculateTimeVariance(times: List<Int>): Int {
        val mean = times.average()
        return times.sumOf { ((it - mean) * (it - mean)).toInt() }
    }

}
