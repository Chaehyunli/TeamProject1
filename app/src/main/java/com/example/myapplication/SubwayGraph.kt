package com.example.myapplication
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

// Node 클래스 정의: 역 번호와 해당 역의 연결 정보 저장
class Node(val stationNumber: Int) {
    val lineNumbers = mutableSetOf<Int>()  // 다중 노선 번호를 저장할 집합
    val neighbors = mutableListOf<Edge>()  // 인접 역과의 연결 정보 리스트

    // 노선 번호 추가
    fun addLineNumber(lineNumber: Int) {
        lineNumbers.add(lineNumber)
    }

    // 인접 역 추가
    fun addNeighbor(destination: Int, distance: Int, cost: Int, time: Int) {
        val edge = Edge(destination, distance, cost, time)
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
    val time: Int           // 시간
) {
    override fun toString(): String {
        return "(목적지: $destination, 거리: ${distance}m, 비용: ${cost}원, 시간: ${time}초)"
    }
}

// SubwayGraph 클래스 정의: 역을 관리하고 그래프 구축
class SubwayGraph {
    private val stations = mutableMapOf<Int, Node>() // 전체 역을 저장할 맵

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

        stations[station1]?.addNeighbor(station2, distance, cost, time)
        stations[station2]?.addNeighbor(station1, distance, cost, time) // 양방향 연결
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
            // entry 리스트의 요소를 인덱스를 통해 직접 접근
            val src = entry[0]
            val dst = entry[1]
            val time = entry[2]
            val dis = entry[3]
            val money = entry[4]
            val line = entry[5]

            // 그래프에 연결 추가
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

// 실행 예제
fun main() {
    val subwayGraph = SubwayGraph()

    // 데이터 직접 로드
    subwayGraph.loadDataDirectly(subwayGraph)

    subwayGraph.printTotalStations()
    // 그래프 출력
    subwayGraph.printGraph()
}
