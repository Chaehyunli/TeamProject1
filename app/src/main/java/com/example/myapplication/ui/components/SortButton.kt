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

@Composable
fun SortButton(
    currentSortCriteria: String, // 현재 선택된 정렬 기준을 외부에서 받음
    onSortCriteriaSelected: (String) -> Unit, // 정렬 기준 선택 시 RouteSearchScreen으로 콜백
    resetMenuItems: Boolean // 초기화 상태를 확인
) {
    var expanded by remember { mutableStateOf(false) }
    var menuItems by remember { mutableStateOf(listOf("최단 거리 순", "최소 시간 순", "최소 비용 순", "최소 환승 순")) }

    // 초기화 상태가 true라면 메뉴 항목을 초기 상태로 재설정
    LaunchedEffect(resetMenuItems) {
        if (resetMenuItems) {
            menuItems = listOf("최단 거리 순", "최소 시간 순", "최소 비용 순", "최소 환승 순")
        }
    }

    Box(
        modifier = Modifier
            .padding(0.dp)
            .height(36.dp)
    ) {
        // Sort 버튼
        Button(
            onClick = { expanded = !expanded },
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
                // PNG 이미지
                Image(
                    painter = painterResource(id = R.drawable.ic_dot),
                    contentDescription = "정렬 아이콘",
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(16.dp)
                        .align(Alignment.CenterVertically)
                )

                // 외부에서 전달받은 정렬 기준 표시
                Text(
                    text = currentSortCriteria,
                    fontSize = 12.sp,
                    color = Color(0xFF252F42),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }

        // DropdownMenu 설정
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(160.dp)
                .wrapContentHeight()
                .border(BorderStroke(0.7.dp, Color(0xFF252f42)), shape = RoundedCornerShape(16.dp))
                .padding(0.dp),
            offset = DpOffset(x = 0.dp, y = (-40).dp)
        ) {
            Column {
                menuItems.forEachIndexed { index, text ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            onSortCriteriaSelected(text) // 선택된 기준을 외부로 전달

                            // 선택된 항목을 리스트의 맨 앞으로 이동시키고 나머지를 뒤로 정렬
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
                                    contentDescription = null,
                                    tint = Color(0xFF252F42),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text, fontSize = 12.sp, color = Color(0xFF252F42))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                            .background(Color.White),
                        contentPadding = PaddingValues(0.dp)
                    )
                    if (index < menuItems.size - 1) {
                        Divider(color = Color(0xFF252f42), thickness = 0.7.dp)
                    }
                }
            }
        }
    }
}
