package com.poly.tuanlvph48359.screen

import android.util.Log
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
import com.poly.tuanlvph48359.util.UserPreferences
import com.poly.tuanlvph48359.viewmodel.AppViewModel
import com.poly.tuanlvph48359.viewmodel.AuthViewModel
import com.poly.tuanlvph48359.viewmodel.CartViewModel

/**
 * Màn hình đăng nhập - cập nhật để sử dụng API và AppViewModel
 */
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: (userId: String) -> Unit = {},
    authViewModel: AuthViewModel = viewModel()
) {
    val cartViewModel: CartViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Add these two state variables for error handling
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Lấy context để sử dụng Toast và UserPreferences
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    // Khởi tạo AppViewModel để xử lý sự kiện đăng nhập toàn cục
    val appViewModel: AppViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as android.app.Application
        )
    )

    // Sử dụng collectAsState để theo dõi sự thay đổi từ Flow
    val loginState by authViewModel.loginState.collectAsState()

    // Xử lý hiệu ứng khi trạng thái đăng nhập thay đổi
    LaunchedEffect(loginState) {
        when {
            loginState.isError -> {
                isError = true
                errorMessage = loginState.errorMessage ?: "Đã xảy ra lỗi"
            }

            loginState.user != null -> {
                // Đăng nhập thành công
                Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()

                // Sử dụng null-safe call để lấy id
                val userId = loginState.user?.id ?: ""

                // Sử dụng AppViewModel để xử lý đăng nhập và thông báo cho các màn hình khác
                appViewModel.login(userId) {
                    cartViewModel.clearCart()
                    Log.d("LOGIN_DEBUG", "Giỏ hàng đã được xóa trước khi đăng nhập. Size: ${cartViewModel.cartItems.size}")
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }

                // Reset state
                authViewModel.resetLoginState()
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Hello !",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = "WELCOME BACK",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

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

            // Display error message if isError is true
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

            // Liên kết quên mật khẩu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Forgot Password",
                    modifier = Modifier.clickable { /* Xử lý quên mật khẩu */ },
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Nút đăng nhập - với loading indicator
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        isError = true
                        errorMessage = "Vui lòng nhập email và mật khẩu"
                    } else {
                        cartViewModel.clearCart()
                        authViewModel.loginUser(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp),
                enabled = !loginState.isLoading
            ) {
                if (loginState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Log in",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Liên kết đến trang đăng ký
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SIGN UP",
                    modifier = Modifier.clickable { navController.navigate("signup") },
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}