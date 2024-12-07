// RouteInputField.kt
package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RouteInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    onDelete: () -> Unit,
    focusManager: FocusManager,
    canDeleteField: Boolean
) {
    var isFocused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, shape = RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(40.dp)
                .padding(vertical = 8.dp)
        ) {
            // 값이 비어 있고 포커스 안 되어 있을 때 라벨 텍스트 표시
            if (value.isEmpty() && !isFocused) {
                Text(
                    text = label,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,  // 값 변경 콜백 호출
                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                singleLine = true, // 한 줄 입력 필드로 설정
                keyboardOptions = KeyboardOptions.Default, // 기본 키보드 설정
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() } // 완료 시 포커스 해제
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged { focusState -> // 포커스 상태 변경 감지
                        isFocused = focusState.isFocused
                    }
            )
        }

        if (value.isNotEmpty() || canDeleteField) {
            IconButton(
                onClick = {
                    if (value.isEmpty() && canDeleteField) {
                        // MeetingPlaceScreen.kt에서 호출 시
                        onDelete() // 빈 필드일 때 필드 자체 삭제
                    } else {
                        // RouteSearchScreen.kt에서 호출 시
                        onValueChange("") // 값이 있을 때는 값 삭제
                    }

                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .size(20.dp)
                    .padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "삭제",
                    tint = Color(0xFF252F42)
                )
            }
        }
    }
}
