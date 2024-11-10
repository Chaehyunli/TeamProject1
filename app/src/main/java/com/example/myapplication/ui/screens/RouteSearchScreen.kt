//길찾기 화면
package com.example.myapplication.ui.screens

import SortButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.components.SearchButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSearchScreen(onBack: () -> Unit) {
    Scaffold(
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(0.dp)
                    .background(color=Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Box - Full width and at the top of the screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFcbd2df))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            // Back and Switch Icons
                            IconButton(

                                onClick = onBack,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "뒤로가기",
                                    tint = Color(0xFF252f42)
                                )
                            }
                            IconButton(
                                onClick = { /* 전환 로직 */ },
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = "전환",
                                    tint = Color(0xFF252f42)
                                )
                            }
                        }

                        // Departure and Arrival Fields
                        Column(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(10.dp))
                                .weight(4.5f)
                        ) {
                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                label = { Text("출발지 입력") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White, // Sets the background inside the field to white
                                    focusedBorderColor = Color.Gray, // Optional: Set border color when focused
                                    unfocusedBorderColor = Color.LightGray // Optional: Set border color when not focused
                                ),
                                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            )
                            OutlinedTextField(
                                value = "",
                                onValueChange = {},
                                label = { Text("도착지 입력") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    containerColor = Color.White, // Sets the background inside the field to white
                                    focusedBorderColor = Color.Gray, // Optional: Set border color when focused
                                    unfocusedBorderColor = Color.LightGray // Optional: Set border color when not focused
                                ),
                                shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Sorting and Search Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SortButton()
                    Spacer(modifier = Modifier.width(35.dp))
                    SearchButton()
                }

                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = Color(0xFF808590), thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(38.dp))

                // Guide message
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.backgroundlogo), // 사용할 이미지 리소스 ID
                        contentDescription = "배경 이미지",
                        modifier = Modifier
                            .size(200.dp) // 이미지 크기 조정
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    )
}
