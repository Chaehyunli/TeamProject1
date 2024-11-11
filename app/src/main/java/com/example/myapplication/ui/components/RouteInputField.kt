// RouteInputField,kt
package com.example.myapplication.ui.components
// 지금 한글 입력 자음에서 마우스 꾹 누른 상태로 모음으로 가야 입력되는 상태
// 한글 키보드 나오게 하는법은 안드로이드 폰 설정에 들어가서 Languages & input 한국어 추가
// 코딩으로 한글하고 숫자만 입력하게 해놓음. 영어 안쳐짐(우리 앱에서만)

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.ImeAction


@OptIn(ExperimentalMaterial3Api::class) // 이거 실험 버전 경고 뜨는 거 무시하는 구문, 앱 실행에는 아무런 문제 없음.
@Composable
fun RouteInputField(
    label: String,
    onDelete: () -> Unit,
    canDelete: Boolean,
    focusManager: FocusManager,
    value: String,  // value 매개변수를 String 타입으로 선언
    onValueChange: (String) -> Unit  // onValueChange 매개변수를 (String)
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
            value = value,
            onValueChange = { newValue ->
                // 입력값이 숫자 또는 한글일 경우에만 onValueChange 호출
                if (newValue.all { it.isDigit() || it in '가'..'힣' }) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp), // 필드 높이 조정
            label = { Text(label) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Default,
                keyboardType = KeyboardType.Text // 한글 입력을 허용하도록 설정
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() } // 엔터(완료) 키를 누르면 포커스 해제
            ),
            textStyle = TextStyle(fontSize = 16.sp),
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
