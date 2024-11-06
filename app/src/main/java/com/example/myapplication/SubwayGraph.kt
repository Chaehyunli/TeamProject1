package com.example.myapplication

import kotlin.String

//import com.example.myapplication.RouteFinder
//import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
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
    private val stations = mutableMapOf<Int, Node>() // 전체 역을 저장할 맵

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

    fun printTotalStations() {
        println("전체 역의 개수: ${stations.size}")
    }

    fun loadDataDirectly(graph: SubwayGraph) {
        val data = listOf(
            listOf(101, 102, 200, 500, 200, 1),
            listOf(102, 103, 300, 400, 300, 1),
            listOf(103, 104, 1000, 600, 500, 1),
            listOf(104, 105, 500, 200, 340, 1),
            listOf(105, 106, 150, 600, 450, 1),
            listOf(106, 107, 320, 200, 120, 1),
            listOf(107, 108, 400, 700, 650, 1),
            listOf(108, 109, 800, 350, 200, 1),
            listOf(109, 110, 900, 250, 430, 1),
            listOf(110, 111, 500, 650, 120, 1),
            listOf(111, 112, 1000, 400, 890, 1),
            listOf(112, 113, 2000, 500, 800, 1),
            listOf(113, 114, 500, 500, 700, 1),
            listOf(114, 115, 220, 400, 540, 1),
            listOf(115, 116, 230, 600, 330, 1),
            listOf(116, 117, 300, 200, 280, 1),
            listOf(117, 118, 500, 600, 800, 1),
            listOf(118, 119, 480, 200, 1000, 1),
            listOf(119, 120, 500, 700, 2000, 1),
            listOf(120, 121, 400, 350, 700, 1),
            listOf(121, 122, 900, 250, 650, 1),
            listOf(122, 123, 300, 650, 440, 1),
            listOf(123, 101, 480, 400, 200, 1),
            listOf(101, 201, 1000, 500, 300, 2),
            listOf(201, 202, 250, 500, 500, 2),
            listOf(202, 203, 480, 400, 340, 2),
            listOf(203, 204, 400, 600, 450, 2),
            listOf(204, 205, 250, 200, 120, 2),
            listOf(205, 206, 500, 600, 650, 2),
            listOf(206, 207, 320, 200, 200, 2),
            listOf(207, 208, 250, 700, 430, 2),
            listOf(208, 209, 300, 350, 120, 2),
            listOf(209, 210, 150, 250, 890, 2),
            listOf(210, 211, 900, 650, 800, 2),
            listOf(211, 212, 320, 400, 700, 2),
            listOf(212, 213, 150, 500, 540, 2),
            listOf(213, 214, 500, 500, 330, 2),
            listOf(214, 215, 210, 400, 280, 2),
            listOf(215, 216, 150, 600, 800, 2),
            listOf(216, 217, 500, 200, 1000, 2),
            listOf(207, 301, 300, 600, 2000, 3),
            listOf(301, 302, 300, 200, 700, 3),
            listOf(302, 303, 480, 700, 650, 3),
            listOf(303, 304, 400, 350, 440, 3),
            listOf(304, 123, 250, 250, 200, 3),
            listOf(123, 305, 300, 650, 300, 3),
            listOf(305, 306, 250, 400, 500, 3),
            listOf(306, 307, 900, 500, 340, 3),
            listOf(307, 308, 480, 500, 450, 3),
            listOf(308, 107, 400, 400, 120, 3),
            listOf(104, 401, 1000, 600, 650, 4),
            listOf(401, 307, 150, 200, 200, 4),
            listOf(307, 402, 300, 600, 430, 4),
            listOf(402, 403, 210, 200, 120, 4),
            listOf(403, 404, 320, 700, 890, 4),
            listOf(404, 405, 210, 350, 800, 4),
            listOf(405, 406, 500, 250, 700, 4),
            listOf(406, 407, 300, 650, 540, 4),
            listOf(407, 115, 320, 400, 330, 4),
            listOf(115, 408, 480, 500, 280, 4),
            listOf(408, 409, 300, 340, 800, 4),
            listOf(409, 410, 480, 500, 1000, 4),
            listOf(410, 411, 300, 400, 2000, 4),
            listOf(411, 412, 900, 600, 700, 4),
            listOf(412, 413, 400, 200, 650, 4),
            listOf(413, 414, 430, 600, 440, 4),
            listOf(414, 415, 150, 200, 200, 4),
            listOf(415, 416, 1000, 700, 300, 4),
            listOf(416, 417, 500, 350, 500, 4),
            listOf(417, 216, 900, 250, 340, 4),
            listOf(209, 501, 320, 650, 450, 5),
            listOf(501, 502, 320, 400, 120, 5),
            listOf(502, 503, 430, 500, 650, 5),
            listOf(503, 504, 210, 500, 200, 5),
            listOf(504, 122, 320, 400, 430, 5),
            listOf(122, 505, 480, 600, 120, 5),
            listOf(505, 506, 300, 200, 890, 5),
            listOf(506, 403, 320, 600, 800, 5),
            listOf(403, 507, 300, 200, 700, 5),
            listOf(507, 109, 1000, 700, 540, 5),
            listOf(601, 602, 150, 350, 330, 6),
            listOf(602, 121, 700, 250, 280, 6),
            listOf(121, 603, 500, 650, 800, 6),
            listOf(603, 604, 300, 400, 1000, 6),
            listOf(604, 605, 430, 200, 2000, 6),
            listOf(605, 606, 480, 300, 700, 6),
            listOf(606, 116, 320, 400, 650, 6),
            listOf(116, 607, 250, 200, 440, 6),
            listOf(607, 608, 500, 600, 200, 6),
            listOf(608, 609, 700, 200, 300, 6),
            listOf(609, 412, 320, 700, 500, 6),
            listOf(412, 610, 1000, 350, 340, 6),
            listOf(610, 611, 700, 250, 450, 6),
            listOf(611, 612, 700, 650, 120, 6),
            listOf(612, 613, 150, 400, 650, 6),
            listOf(613, 614, 430, 200, 200, 6),
            listOf(614, 615, 500, 300, 430, 6),
            listOf(615, 616, 700, 400, 120, 6),
            listOf(616, 417, 480, 200, 890, 6),
            listOf(417, 617, 320, 600, 800, 6),
            listOf(617, 618, 300, 200, 700, 6),
            listOf(618, 619, 250, 700, 540, 6),
            listOf(619, 620, 700, 350, 330, 6),
            listOf(620, 621, 320, 250, 280, 6),
            listOf(621, 622, 480, 650, 800, 6),
            listOf(622, 601, 150, 400, 1000, 6),
            listOf(202, 303, 1000, 200, 2000, 7),
            listOf(303, 503, 700, 300, 700, 7),
            listOf(503, 601, 500, 400, 650, 7),
            listOf(601, 701, 430, 200, 440, 7),
            listOf(701, 702, 150, 600, 200, 7),
            listOf(702, 703, 600, 200, 300, 7),
            listOf(703, 704, 700, 700, 500, 7),
            listOf(704, 705, 250, 350, 340, 7),
            listOf(705, 706, 600, 250, 450, 7),
            listOf(706, 416, 300, 650, 120, 7),
            listOf(416, 707, 430, 400, 650, 7),
            listOf(707, 614, 480, 200, 200, 7),
            listOf(113, 801, 600, 300, 430, 8),
            listOf(801, 802, 1000, 400, 120, 8),
            listOf(802, 803, 700, 200, 890, 8),
            listOf(803, 409, 600, 600, 800, 8),
            listOf(409, 608, 500, 200, 700, 8),
            listOf(608, 804, 700, 700, 540, 8),
            listOf(804, 805, 150, 350, 330, 8),
            listOf(805, 806, 210, 250, 280, 8),
            listOf(806, 705, 600, 650, 800, 8),
            listOf(705, 618, 250, 400, 1000, 8),
            listOf(618, 214, 700, 200, 2000, 8),
            listOf(112, 901, 600, 300, 700, 9),
            listOf(901, 406, 300, 400, 650, 9),
            listOf(406, 605, 210, 200, 440, 9),
            listOf(605, 902, 480, 600, 280, 9),
            listOf(902, 119, 430, 200, 800, 9),
            listOf(119, 903, 1000, 700, 1000, 9),
            listOf(903, 702, 150, 350, 2000, 9),
            listOf(702, 904, 500, 250, 700, 9),
            listOf(904, 621, 250, 650, 650, 9),
            listOf(621, 211, 300, 400, 440, 9)
        )

        for (entry in data) {
            val src = entry[0]
            val dst = entry[1]
            val time = entry[2]
            val dis = entry[3]
            val money = entry[4]
            val line = entry[5]

            graph.addConnection(src, dst, dis, money, time, line)
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
    val subwayGraph: SubwayGraph by lazy {
        SubwayGraph().apply {
            loadDataDirectly(this)
        }
    }

    private val routeFinder = RouteFinder(subwayGraph)

    // 개별 경로에 접근할 수 있는 getter 함수 추가
    fun getShortestTimeRoute(startStation: Int, endStation: Int): RouteFinder.RouteInfo? {
        return routeFinder.findShortestTimePath(startStation, endStation)?.apply { criteria.add("최소 시간") }
    }

    fun getShortestDistanceRoute(startStation: Int, endStation: Int): RouteFinder.RouteInfo? {
        return routeFinder.findShortestDistancePath(startStation, endStation)?.apply { criteria.add("최소 거리") }
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
        val shortestDistanceRoute = routeFinder.findShortestDistancePath(startStation, endStation).apply { criteria.add("최소 거리") }
        val cheapestRoute = routeFinder.findCheapestPath(startStation, endStation).apply { criteria.add("최소 비용") }
        val fewestTransfersRoute = routeFinder.findFewestTransfersPath(startStation, endStation).apply { criteria.add("최소 환승") }

        val routes = listOf(shortestTimeRoute, shortestDistanceRoute, cheapestRoute, fewestTransfersRoute)

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

        // 최소 환승 경로 기준을 동일 환승 횟수의 다른 경로에 추가
        val minTransfersRoute = uniqueRoutes.find { it.criteria.contains("최소 환승") }
        if (minTransfersRoute != null) {
            uniqueRoutes.filter { it != minTransfersRoute && it.transfers == minTransfersRoute.transfers }.forEach {
                it.criteria.add("최소 환승")
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
//---------------------------------------------------------------------------------------------------------------------
// 실행 예제
fun main() {
    val subwayGraph = SubwayGraph()

    // 데이터 직접 로드
    subwayGraph.loadDataDirectly(subwayGraph)

    // 그래프 출력
    //subwayGraph.printTotalStations()
    //subwayGraph.printGraph()

    val routeFinder = RouteFinder(subwayGraph)
    //val startStation = 101 // 출발역
    //val endStation = 216 // 도착역
    /*print("출발역 번호를 입력하세요: ")
    val startStation = readLine()?.toIntOrNull() ?: run {
        println("유효한 숫자가 아닙니다. 프로그램을 종료합니다.")
        return
    }

    // 도착역 입력
    print("도착역 번호를 입력하세요: ")
    val endStation = readLine()?.toIntOrNull() ?: run {
        println("유효한 숫자가 아닙니다. 프로그램을 종료합니다.")
        return
    }

    //println("최소 시간 경로: ${routeFinder.findShortestTimePath(startStation, endStation)}")
    //println("최소 거리 경로: ${routeFinder.findShortestDistancePath(startStation, endStation)}")
    //println("최소 비용 경로: ${routeFinder.findCheapestPath(startStation, endStation)}")
    //println("최소 환승 경로: ${routeFinder.findFewestTransfersPath(startStation, endStation)}")

    // 각 기준별 경로 탐색
    val shortestTimeRoute = routeFinder.findShortestTimePath(startStation, endStation).apply { criteria.add("최소 시간") }
    val shortestDistanceRoute = routeFinder.findShortestDistancePath(startStation, endStation).apply { criteria.add("최소 거리") }
    val cheapestRoute = routeFinder.findCheapestPath(startStation, endStation).apply { criteria.add("최소 비용") }
    val fewestTransfersRoute = routeFinder.findFewestTransfersPath(startStation, endStation).apply { criteria.add("최소 환승") }

    val routes = listOf(
        shortestTimeRoute,
        shortestDistanceRoute,
        cheapestRoute,
        fewestTransfersRoute
    )

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

    // 최소 환승 경로를 찾고, 다른 경로에 동일한 환승 횟수가 있으면 `최소 환승` 기준을 추가
    val minTransfersRoute = uniqueRoutes.find { it.criteria.contains("최소 환승") }
    if (minTransfersRoute != null) {
        uniqueRoutes.filter { it != minTransfersRoute && it.transfers == minTransfersRoute.transfers }.forEach {
            it.criteria.add("최소 환승")
        }
        //uniqueRoutes.remove(minTransfersRoute) // 기존 최소 환승 경로 삭제
    }

    // 각 기준 별 정렬
    //  println(it) -> RouteFinder routes의 toString() 메서드 호출
    println("\n=== 최소 거리 기준으로 정렬 ===")
    uniqueRoutes.sortedBy { it.distance }.forEach { println(it) }
    // routes.sortedBy { it.distance } 까지가 객체

    println("\n=== 최소 시간 기준으로 정렬 ===")
    uniqueRoutes.sortedBy { it.time }.forEach { println(it) }

    println("\n=== 최소 비용 기준으로 정렬 ===")
    uniqueRoutes.sortedBy { it.cost }.forEach { println(it) }

    println("\n=== 최소 환승 기준으로 정렬 ===")
    uniqueRoutes.sortedBy { it.transfers }.forEach { println(it) }

     */

    println(SubwayGraphInstance.calculateMeetingPlaceRoute(listOf("601", "101", "209", "102")))

}