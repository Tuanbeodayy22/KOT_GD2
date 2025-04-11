package com.poly.tuanlvph48359.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.poly.tuanlvph48359.model.AddressResponse
import com.poly.tuanlvph48359.util.UserPreferences
import com.poly.tuanlvph48359.viewmodel.AddressViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShippingAddressScreen(
    navController: NavController,
    addressViewModel: AddressViewModel = viewModel()
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userId = userPreferences.getUserId()

    var fullName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var zipcode by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Dropdowns
    var countryExpanded by remember { mutableStateOf(false) }
    var cityExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }

    var selectedCountry by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }
    var selectedDistrict by remember { mutableStateOf("") }

    // Danh sách mẫu
    val countries = listOf("United States", "Canada", "United Kingdom", "Australia", "France", "Germany", "Japan")
    val cities = listOf("New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio")
    val districts = listOf("Manhattan", "Brooklyn", "Queens", "The Bronx", "Staten Island")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar with back button and title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() },
                    tint = Color.Black
                )

                Spacer(modifier = Modifier.width(32.dp))

                Text(
                    text = "Add shipping address",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Full Name
                Text(
                    text = "Full name",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = { Text("Ex: Bruno Pham", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Address
                Text(
                    text = "Address",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = { Text("Ex: 25 Robert Latouche Street", color = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Zipcode
                Text(
                    text = "Zipcode (Postal Code)",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                TextField(
                    value = zipcode,
                    onValueChange = { zipcode = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Country Dropdown
                Text(
                    text = "Country",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp))
                        .clickable { countryExpanded = true }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedCountry.isEmpty()) "Select Country" else selectedCountry,
                            color = if (selectedCountry.isEmpty()) Color.LightGray else Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color.Black
                        )
                    }
                }

                DropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country) },
                            onClick = {
                                selectedCountry = country
                                countryExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // City Dropdown
                Text(
                    text = "City",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .clickable { cityExpanded = true }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedCity.ifEmpty { "Select City" },
                            color = if (selectedCity.isEmpty()) Color.Gray else Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color.Black
                        )
                    }
                }

                DropdownMenu(
                    expanded = cityExpanded,
                    onDismissRequest = { cityExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    cities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                selectedCity = city
                                cityExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // District Dropdown
                Text(
                    text = "District",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(4.dp))
                        .clickable { districtExpanded = true }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedDistrict.isEmpty()) "Select District" else selectedDistrict,
                            color = if (selectedDistrict.isEmpty()) Color.LightGray else Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color.Black
                        )
                    }
                }

                DropdownMenu(
                    expanded = districtExpanded,
                    onDismissRequest = { districtExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    districts.forEach { district ->
                        DropdownMenuItem(
                            text = { Text(district) },
                            onClick = {
                                selectedDistrict = district
                                districtExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Save Button
                Button(
                    onClick = {
                        // Kiểm tra các trường trước khi lưu
                        if (fullName.isNotBlank() && address.isNotBlank()) {
                            // Tạo đối tượng địa chỉ mới
                            val fullAddress = buildString {
                                append(address)
                                if (selectedDistrict.isNotEmpty()) append(", $selectedDistrict")
                                if (selectedCity.isNotEmpty()) append(", $selectedCity")
                                if (zipcode.isNotEmpty()) append(", $zipcode")
                                if (selectedCountry.isNotEmpty()) append(", $selectedCountry")
                            }

                            val newAddress = AddressResponse(
                                id = "0", // API sẽ tự gán ID
                                userId = userId,
                                name = fullName,
                                address = fullAddress,
                                isDefault = false // Mặc định không phải địa chỉ mặc định
                            )

                            // Lưu địa chỉ và quay lại màn hình trước
                            coroutineScope.launch {
                                addressViewModel.addAddress(newAddress)
                                navController.popBackStack()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "SAVE ADDRESS",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}