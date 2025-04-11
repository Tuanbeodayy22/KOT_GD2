package com.poly.tuanlvph48359

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.poly.tuanlvph48359.api.RetrofitClient
import com.poly.tuanlvph48359.repository.FavoriteRepository
import com.poly.tuanlvph48359.screen.*
import com.poly.tuanlvph48359.util.UserPreferences
import com.poly.tuanlvph48359.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences = UserPreferences(this)
        val isLoggedIn = userPreferences.isLoggedIn()
        val userId = userPreferences.getUserId()

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color.Black,
                    onPrimary = Color.White,
                    background = Color.White,
                    surface = Color.White
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FurnitureApp(isLoggedIn, userId)
                }
            }
        }
    }
}

@Composable
fun FurnitureApp(isLoggedIn: Boolean, userId: String) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Khởi tạo AppViewModel để quản lý sự kiện toàn cục
    val appViewModel: AppViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )

    // Theo dõi sự kiện thay đổi người dùng
    val userChangeEvent by appViewModel.userChangeEvent.collectAsState()

    // Cart ViewModel
    val cartViewModel: CartViewModel = viewModel()

    // Favorite ViewModel
    val apiService = RetrofitClient.apiService
    val favoriteRepository = FavoriteRepository(apiService)
    val favoriteViewModelFactory = FavoriteViewModelFactory(context.applicationContext as Application, favoriteRepository)

    val favoriteViewModel: FavoriteViewModel = viewModel(
        factory = favoriteViewModelFactory
    )

    // Khởi tạo dữ liệu
    LaunchedEffect(key1 = userChangeEvent) {
        // Dữ liệu sẽ được làm mới khi có sự kiện thay đổi người dùng
        val currentUserId = UserPreferences(context).getUserId()
        favoriteViewModel.loadFavorites()
    }
    LaunchedEffect(key1 = userChangeEvent) {
        // Dữ liệu sẽ được làm mới khi có sự kiện thay đổi người dùng
        val currentUserId = UserPreferences(context).getUserId()
        if (currentUserId.isEmpty()) {
            // Nếu không có userId, tức là đã đăng xuất
            favoriteViewModel.resetState()
        } else {
            favoriteViewModel.loadFavorites()
        }
    }

    // Theo dõi sự kiện đăng xuất
    LaunchedEffect(Unit) {
        appViewModel.logoutEvent.collect {
            favoriteViewModel.resetState() // Xóa dữ liệu yêu thích khi đăng xuất
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "boarding"
    ) {
        composable("boarding") { BoardingScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("home") { HomeScreen(navController = navController, cartViewModel = cartViewModel) }
        composable("cart") { CartScreen(navController = navController, cartViewModel = cartViewModel) }
        composable("checkout") { CheckoutScreen(navController = navController, cartViewModel = cartViewModel) }
        composable("success") { SuccessScreen(navController) }
        composable("favorites") { FavoriteScreen(navController = navController) }
        composable("notification") { NotificationScreen(navController) }
        composable("review") { ReviewScreen(navController) }
        composable("profile") { ProfileScreen(navController = navController) }
        composable("shippingAddress") { ShippingAddressScreen(navController) }
        composable("myOrder") { MyOrderScreen(navController) }
        composable("paymentMethod") { PaymentMethodScreen(navController) }
        composable("myReviews") { MyReviewsScreen(navController) }
        composable("setting") { SettingScreen(navController) }
        composable("addShippingAddress") { AddShippingAddressScreen(navController) }
        composable(
            "product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getInt("productId") ?: 1
            val currentUserId = UserPreferences(context).getUserId()
            ProductScreen(
                navController = navController,
                productId = productId,
                cartViewModel = cartViewModel,
                favoriteViewModel = favoriteViewModel,
                userId = currentUserId
            )
        }
    }
}

@Composable
fun BoardingScreen(navController: androidx.navigation.NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.furniture_background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "MAKE YOUR",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "HOME BEAUTIFUL",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Text(
                text = "The best simple place where you discover most wonderful furnitures and make your home beautiful",
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = {
                    navController.navigate("login")
                },
                modifier = Modifier
                    .height(48.dp)
                    .width(160.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Get Started",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
