//홈 화면 검색 바
// StationInputField.kt
package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField

@JvmOverloads
@Composable
fun StationInputField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    focusManager: FocusManager,
    onSearchClick: () -> Unit // 검색 버튼 클릭 이벤트
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(24.dp))
            .background(Color.White, RoundedCornerShape(24.dp))
            .height(56.dp) // 최적화된 높이
            .padding(horizontal = 8.dp), // 양쪽 여백을 줄임
        verticalAlignment = Alignment.CenterVertically
    ) {
        // BasicTextField와 Box로 텍스트 필드 구성
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = "역, 호선 검색",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.size(36.dp) // 아이콘 버튼 크기 조정
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = Color(0xFF252F42)
            )
        }
    }
}
