// RouteInputField,kt
package com.example.myapplication.ui.components
// 지금 한글 입력 자음에서 마우스 꾹 누른 상태로 모음으로 가야 입력되는 상태
// 한글 키보드 나오게 하는법은 안드로이드 폰 설정에 들어가서 Languages & input 한국어 추가
// 코딩으로 한글하고 숫자만 입력하게 해놓음. 영어 안쳐짐(우리 앱에서만)

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
    modifier: Modifier,
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
                onValueChange = onValueChange,
                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Default,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    }
            )
        }

        if (value.isNotEmpty() || canDeleteField) {
            IconButton(
                onClick = {
                    if (value.isEmpty() && canDeleteField) {
                        onDelete() // 빈 필드일 때 필드 자체 삭제
                    } else {
                        onValueChange("") // 값이 있을 때는 값 삭제
                    }
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
