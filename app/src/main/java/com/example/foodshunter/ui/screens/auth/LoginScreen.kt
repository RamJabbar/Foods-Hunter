package com.example.foodshunter.ui.screens.auth


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodshunter.R
import com.example.foodshunter.ui.theme.FoodsHunterTheme
import com.example.foodshunter.ui.theme.*

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun LoginScreenPreview() {
//    FoodsHunterTheme {
//        LoginScreen(
//            onLoginSuccess = {},
//            onNavigateToRegister = {}
//        )
//    }
//}


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = viewModel()
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.verticalGradient(
                listOf(SoftGreen, SoftWhite, white)
            )
        )
        .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
    Image(
        painter = painterResource(id = R.drawable.logofoodsh),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(280.dp)
    )
    Text(
        modifier = Modifier,
        text = "Foods Hunter",
        color = Oren,
        fontSize = 30.sp,
        fontFamily = FontFamily(
            Font(R.font.lilitaoneregular)
        )
    )
        Spacer(modifier = Modifier.height(5.dp)
        )
        Text(
            modifier = Modifier,
            text = "Cari Makanan Favoritmu!",
            color = HijauTua,
            fontSize = 20.sp,
            fontFamily = FontFamily(
                Font(R.font.tomorrowmediumitalic)
            )

        )
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = {Text("Email")},
            shape = RoundedCornerShape(15.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Email"
                )
            },
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Password"
                )
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            trailingIcon = {
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide" else "Show"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            shape = RoundedCornerShape(15.dp)
        )
        Spacer(modifier = Modifier.height(25.dp)
        )
        Button(
            onClick = { viewModel.login(email, password) },  // ← Pass email
            enabled = email.isNotBlank() &&
                    password.isNotBlank() &&
                    authState !is AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(45.dp),
            shape = RoundedCornerShape(15.dp),
            colors = (ButtonDefaults.buttonColors(
                containerColor =Oren
            ))
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    color = YellowTua,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Text("Login", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(modifier = Modifier.height(12.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Belum punya akun? ",
                fontSize = 14.sp,
            )
            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = "Daftar Sekarang!",
                    fontWeight = FontWeight.SemiBold,
                    color = Oren
                )
            }
        }
    }
}