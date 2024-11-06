package com.example.myapplication
//import com.example.myapplication.SubwayGraph
// 지하철 역에서 최소 시간, 최소 환승, 최소 비용, 최소 거리로 최적 경로 찾는 클래스 파일

import java.util.PriorityQueue

// 경로 탐색을 위한 클래스 정의
// SubwayGraph 객체를 받아 최소 시간, 거리, 비용, 환승을 기준으로 경로를 탐색하는 메소드들을 포함함
class RouteFinder(private val graph: SubwayGraph) {

    // 최소 시간을 기준으로 경로를 찾는 메소드
    fun findShortestTimePath(start: Int, end: Int): RouteInfo {
        return findPath(start, end, Comparator { e1, e2 -> e1.time - e2.time })
    }

    // 최소 거리를 기준으로 경로를 찾는 메소드
    fun findShortestDistancePath(start: Int, end: Int): RouteInfo {
        return findPath(start, end, Comparator { e1, e2 -> e1.distance - e2.distance })
    }

    // 최소 비용을 기준으로 경로를 찾는 메소드
    fun findCheapestPath(start: Int, end: Int): RouteInfo {
        return findPath(start, end, Comparator { e1, e2 -> e1.cost - e2.cost })
    }

    // 최소 환승 횟수를 기준으로 경로를 찾는 메소드
    fun findFewestTransfersPath(start: Int, end: Int): RouteInfo {
        return findPath(start, end, Comparator { e1, e2 -> e1.transfers - e2.transfers })
    }

    // 경로 탐색 메소드: 다익스트라 알고리즘을 사용하여 경로를 찾음
    // start: 시작 역 번호
    // end: 도착 역 번호
    // comparator: 경로 비교 기준 (시간, 거리, 비용, 환승 중 하나)
    private fun findPath(start: Int, end: Int, comparator: Comparator<RouteInfo>): RouteInfo {
        val queue = PriorityQueue(comparator) // 우선순위 큐 생성
        val distances = mutableMapOf<Int, RouteInfo>() // 각 역까지의 최적 경로 정보를 저장할 맵

        // 초기 상태: 출발점에 대한 초기 RouteInfo 추가
        queue.add(RouteInfo(0, 0, 0, 0, listOf(start), -1))  // -1은 초기 상태로, 아직 노선이 없음을 나타냄

        while (queue.isNotEmpty()) {
            val current = queue.poll() ?: continue  // current가 null이면 다음 반복으로 넘어감
            val currentStation = current.path.last() // 현재 경로의 마지막 역 번호

            // 도착 역에 도달한 경우 해당 경로 정보를 반환
            if (currentStation == end) {
                // 한 노선에서만 이동했다면 환승 횟수를 0으로 설정
                return if (current.transfers > 0 && current.currentLine == current.path.first()) {
                    current.copy(transfers = 0)
                } else {
                    current
                }
            }

            // 현재 역의 인접 역을 순회하며 경로를 갱신
            graph.getNeighbors(currentStation)?.forEach { edge ->
                // 처음 노선을 설정하거나 이전 노선 번호와 현재 edge의 노선 번호가 다르면 환승으로 간주
                val newTransfers = if (current.currentLine == -1 || edge.line == current.currentLine) {
                    current.transfers  // 노선이 동일하면 환승 없이 그대로 유지
                } else {
                    current.transfers + 1  // 노선이 다르면 환승 추가
                }

                val newPath = current.path + edge.destination // 새로운 경로 생성
                val newInfo = RouteInfo(
                    time = current.time + edge.time,               // 시간 누적
                    distance = current.distance + edge.distance,   // 거리 누적
                    cost = current.cost + edge.cost,               // 비용 누적
                    transfers = newTransfers,                      // 환승 횟수 업데이트
                    path = newPath,                                // 경로 업데이트
                    currentLine = edge.line                        // 현재 노선 번호 업데이트
                )

                val existingInfo = distances[edge.destination]
                if (existingInfo == null || comparator.compare(newInfo, existingInfo) < 0) {
                    distances[edge.destination] = newInfo
                    queue.add(newInfo)
                }
            }
        }
        throw IllegalArgumentException("경로를 찾을 수 없습니다.")
    }

    // 경로 정보를 저장하는 데이터 클래스
    // time: 총 소요 시간
    // distance: 총 거리
    // cost: 총 비용
    // transfers: 총 환승 횟수
    // path: 경로를 이루는 역들의 목록
    // currentLine: 경로 상에서 현재의 노선 번호
    data class RouteInfo(
        val time: Int,
        val distance: Int,
        val cost: Int,
        val transfers: Int,
        val path: List<Int>,
        val currentLine: Int  // 현재 노선 번호를 저장하여 환승 여부 판단
    ) {
        val criteria: MutableSet<String> = mutableSetOf() // 해당 경로가 최소 시간, 최소 비용, 최소 거리, 최소 환승 어떠한 것들에 해당되는지에 대한 정보

        override fun toString(): String {
            val criteriaStr = if (criteria.isNotEmpty()) "${criteria.joinToString(" / ")}  - " else ""
            return "${criteriaStr}시간: ${time}초, 거리: ${distance}m, 비용: ${cost}원, 환승 횟수: $transfers, 경로: ${path.joinToString(" -> ")}"
        }
    }
}