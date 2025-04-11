package com.poly.tuanlvph48359.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.poly.tuanlvph48359.viewmodel.AuthViewModel

/**
 * Màn hình đăng ký - cập nhật để sử dụng API
 */
@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Biến quản lý lỗi
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Lấy context để sử dụng Toast
    val context = LocalContext.current

    // Theo dõi trạng thái đăng ký từ ViewModel
    val registerState by authViewModel.registerState.collectAsState()

    // Xử lý hiệu ứng khi trạng thái đăng ký thay đổi
    LaunchedEffect(registerState) {
        when {
            registerState.isLoading -> {
                // Đang xử lý - hiển thị loading indicator tự động trong nút
            }
            registerState.isError -> {
                // Có lỗi
                isError = true
                errorMessage = registerState.errorMessage ?: "Đã xảy ra lỗi"
                // Reset state để tránh hiển thị lỗi liên tục
                authViewModel.resetRegisterState()
            }
            registerState.user != null -> {
                // Đăng ký thành công
                Toast.makeText(context, "Đăng ký thành công! Vui lòng đăng nhập", Toast.LENGTH_SHORT).show()

                // Điều hướng sang màn hình đăng nhập
                navController.navigate("login") {
                    popUpTo("signup") { inclusive = true }
                }

                // Reset state
                authViewModel.resetRegisterState()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(72.dp))

            // Logo tròn với biểu tượng ở giữa
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .border(1.dp, Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                // Thay biểu tượng này bằng biểu tượng thực tế của bạn
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "App Logo",
                    tint = Color.Black,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Tiêu đề WELCOME
            Text(
                text = "WELCOME",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Trường nhập tên
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    isError = false // Reset error when user types
                },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.Gray
                ),
                singleLine = true,
                shape = RoundedCornerShape(0.dp) // Viền vuông như thiết kế
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Trường nhập email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isError = false // Reset error when user types
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.Gray
                ),
                singleLine = true,
                shape = RoundedCornerShape(0.dp) // Viền vuông như thiết kế
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Trường nhập mật khẩu
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isError = false // Reset error when user types
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.Gray
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(0.dp) // Viền vuông như thiết kế
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Trường xác nhận mật khẩu
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    isError = false // Reset error when user types
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.Gray
                ),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(0.dp) // Viền vuông như thiết kế
            )

            // Hiển thị thông báo lỗi nếu có
            if (isError) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Nút đăng ký với loading indicator
            Button(
                onClick = {
                    // Kiểm tra dữ liệu nhập
                    when {
                        name.isBlank() -> {
                            isError = true
                            errorMessage = "Vui lòng nhập tên"
                        }
                        email.isBlank() -> {
                            isError = true
                            errorMessage = "Vui lòng nhập email"
                        }
                        password.isBlank() -> {
                            isError = true
                            errorMessage = "Vui lòng nhập mật khẩu"
                        }
                        password != confirmPassword -> {
                            isError = true
                            errorMessage = "Mật khẩu xác nhận không khớp"
                        }
                        else -> {
                            // Gọi API đăng ký
                            authViewModel.registerUser(name, email, password)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp),
                enabled = !registerState.isLoading // Vô hiệu hóa nút khi đang loading
            ) {
                if (registerState.isLoading) {
                    // Hiển thị loading spinner khi đang xử lý
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "SIGN UP",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Liên kết quay lại đăng nhập
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have account?",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "SIGN IN",
                    modifier = Modifier.clickable { navController.navigate("login") },
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}