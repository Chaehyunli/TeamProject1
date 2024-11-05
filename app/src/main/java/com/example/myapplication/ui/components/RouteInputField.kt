package com.example.myapplication.ui.components
// 입력 필드 아무곳이나 재사용 가능
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import com.example.myapplication.ui.navigation.AppNavHost // 아이콘 누르면 경로 이동

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteInputField(
    label: String,
    onDelete: () -> Unit,
    canDelete: Boolean,
    focusManager: FocusManager // focusManager 매개변수 추가
) {
    Row( // 입력 필드 전체(Row)에 대한 외부 스타일을 지정
        modifier = Modifier
            .fillMaxWidth() // 입력 필드를 화면의 전체 너비로 채우도록
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)) // 입력 필드에 그림자 효과를 주고 모서리를 둥글게
            .background(Color.White, RoundedCornerShape(8.dp)) // 배경색을 흰색으로 설정하고 모서리를 둥글게
            .padding(horizontal = 8.dp), // 양쪽 여백 추가, 양쪽 끝에서 텍스트 필드와 삭제 버튼 사이에 약간의 간격 만듦
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .weight(1f)
                .height(56.dp), // 필드 높이 조정
            label = { Text(label) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() } // 엔터(완료) 키를 누르면 포커스 해제
            ),
            textStyle = TextStyle(fontSize = 16.sp)
        )
        if (canDelete) {
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(24.dp) // 아이콘 크기 조정
                    .padding(start = 4.dp) // 아이콘과 텍스트 필드 사이 간격 조정
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
