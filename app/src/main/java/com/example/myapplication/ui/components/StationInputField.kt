// StationInputField.kt
package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// StationInputField: 역 또는 호선 검색을 위한 입력 필드 컴포넌트
@Composable
fun StationInputField(
    modifier: Modifier = Modifier,
    value: String, // 입력된 텍스트 값
    onValueChange: (String) -> Unit, // 입력값 변경 시 호출되는 콜백
    focusManager: FocusManager, // FocusManager, 포커스 관리
    onSearchClick: () -> Unit // 검색 버튼 클릭 이벤트
) {
    var isFocused by remember { mutableStateOf(false) } // 포커스 상태 추적
    val focusRequester = remember { FocusRequester() } // FocusRequester 객체 생성

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .height(40.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            // 값이 비어있고 포커스되지 않았을 경우 '역, 호선 검색' 텍스트 표시
            if (value.isEmpty() && !isFocused) {
                Text(
                    text = "역, 호선 검색",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
            // 텍스트 입력 필드
            BasicTextField(
                value = value,
                onValueChange = onValueChange, // 값이 변경될 때 호출
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                singleLine = true, // 한 줄 입력 필드로 설정
                keyboardOptions = KeyboardOptions.Default, // 기본 키보드 설정
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() } // 완료 시 포커스 해제
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester) // 포커스 요청자 연결
                    .onFocusChanged { focusState -> // 포커스 변화 추적
                        isFocused = focusState.isFocused
                    }
            )
        }

        // 검색 아이콘 버튼
        IconButton(
            onClick = {
                if (isFocused) {
                    onSearchClick() // 포커스 상태라면 검색 실행
                } else {
                    focusRequester.requestFocus() // 포커스되지 않으면 포커스 요청
                }
            },
            modifier = Modifier.size(36.dp)
        ) {
            // 검색 아이콘 표시
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색",
                tint = Color(0xFF252F42)
            )
        }
    }
}
