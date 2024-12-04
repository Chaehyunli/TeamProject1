// SortButton.kt
//경로 나열 순서 메뉴
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R

// SortButton: 경로 나열 순서를 설정하는 버튼과 드롭다운 메뉴
@Composable
fun SortButton(
    currentSortCriteria: String, // 현재 선택된 정렬 기준
    onSortCriteriaSelected: (String) -> Unit, // 정렬 기준이 변경될 때 호출되는 콜백
    resetMenuItems: Boolean // 초기화 상태 (true일 경우 메뉴 항목을 초기화)
) {
    // 상태 변수: 드롭다운 메뉴의 펼침/닫힘 상태
    var expanded by remember { mutableStateOf(false) }

    // 메뉴 항목 초기화 상태
    var menuItems by remember { mutableStateOf(listOf("최단 거리 순", "최소 시간 순", "최소 비용 순", "최소 환승 순")) }

    // 초기화 상태가 true일 경우 메뉴 항목을 초기 상태로 재설정
    LaunchedEffect(resetMenuItems) {
        if (resetMenuItems) {
            menuItems = listOf("최단 거리 순", "최소 시간 순", "최소 비용 순", "최소 환승 순")
        }
    }

    // Sort 버튼
    Box(
        modifier = Modifier
            .padding(0.dp)
            .height(36.dp)
    ) {
        // 정렬 기준을 선택하는 버튼
        Button(
            onClick = { expanded = !expanded }, // 버튼 클릭 시 메뉴 펼침/닫힘
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(0.7.dp, Color(0xFF252f42)),
            contentPadding = PaddingValues(horizontal = 12.dp),
            modifier = Modifier
                .width(160.dp)
                .height(40.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 정렬 아이콘 (PNG 이미지)
                Image(
                    painter = painterResource(id = R.drawable.ic_dot),
                    contentDescription = "정렬 아이콘",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(16.dp)
                        .align(Alignment.CenterVertically)
                )

                // 현재 선택된 정렬 기준 표시
                Text(
                    text = currentSortCriteria,
                    fontSize = 12.sp,
                    color = Color(0xFF252F42),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        // 드롭다운 메뉴
        DropdownMenu(
            expanded = expanded, // 메뉴의 펼침 상태
            onDismissRequest = { expanded = false }, // 메뉴 외부 클릭 시 닫기
            modifier = Modifier
                .width(160.dp)
                .wrapContentHeight()
                .border(BorderStroke(0.7.dp, Color(0xFF252f42)), shape = RoundedCornerShape(16.dp))
                .padding(0.dp),
            offset = DpOffset(x = 0.dp, y = (-40).dp) // 메뉴가 버튼 위로 떠오르게 설정
        ) {
            Column {
                // 메뉴 항목 반복
                menuItems.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false // 메뉴 닫기
                            onSortCriteriaSelected(text) // 선택된 정렬 기준을 콜백으로 전달

                            // 선택된 항목을 목록의 맨 앞에 배치
                            menuItems = listOf(text) + menuItems.filter { it != text }
                        },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    painter = painterResource(id = if (index == 0) R.drawable.ic_arrow_down else R.drawable.ic_dot),
                                    contentDescription = null, // 아이콘 설명
                                    tint = Color(0xFF252F42),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text, fontSize = 12.sp, color = Color(0xFF252F42)) // 메뉴 항목 텍스트
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                            .background(Color.White),
                        contentPadding = PaddingValues(0.dp)
                    )

                    // 항목 구분선
                    if (index < menuItems.size - 1) {
                        Divider(color = Color(0xFF252f42), thickness = 0.7.dp)
                    }
                }
            }
        }
    }
}
