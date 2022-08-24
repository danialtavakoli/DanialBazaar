package com.danialtavakoli.danialbazaar.ui.features.signIn

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.danialtavakoli.danialbazaar.R
import com.danialtavakoli.danialbazaar.ui.theme.BackgroundMain
import com.danialtavakoli.danialbazaar.ui.theme.Blue
import com.danialtavakoli.danialbazaar.ui.theme.MainAppTheme
import com.danialtavakoli.danialbazaar.ui.theme.Shapes
import com.danialtavakoli.danialbazaar.util.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            SignInScreen()
        }
    }
}

@Composable
fun SignInScreen() {
    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Blue) }
    val navigation = getNavController()
    val viewModel = getNavViewModel<SignInViewModel>()
    val context = LocalContext.current
    clearInputs(viewModel)
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .background(Blue)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconApp()
            MainCardView(navigation, viewModel) {
                viewModel.signInUser {
                    if (it == VALUE_SUCCESS) {
                        navigation.navigate(MyScreens.MainScreen.route) {
                            popUpTo(MyScreens.MainScreen.route) {
                                inclusive = true
                            }
                        }
                    } else context.showToast(it)
                }
            }
        }
    }
}

@Composable
fun IconApp() {
    Surface(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_icon_app),
            contentDescription = null,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
fun MainCardView(navigation: NavController, viewModel: SignInViewModel, SignInEvent: () -> Unit) {
    val email = viewModel.email.observeAsState("")
    val password = viewModel.password.observeAsState("")
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        elevation = 10.dp,
        shape = Shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 18.dp, bottom = 18.dp),
                text = "Sign In",
                style = TextStyle(color = Blue, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            )
            MainTextField(email.value, R.drawable.ic_email, "Email") { viewModel.email.value = it }
            PasswordTextField(password.value, R.drawable.ic_password, "Password") {
                viewModel.password.value = it
            }
            Button(
                onClick = {
                    if (email.value.isNotEmpty() && password.value.isNotEmpty())
                        if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches())
                            if (NetworkChecker(context).isInternetConnected) SignInEvent.invoke()
                            else context.showToastInternet()
                        else context.showToast("Email format is not true")
                    else context.showToast("Please enter your information")
                },
                modifier = Modifier.padding(top = 28.dp, bottom = 8.dp),
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "Sign In"
                )
            }
            Row(
                modifier = Modifier.padding(bottom = 18.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have an account?")
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    navigation.navigate(MyScreens.SignUpScreen.route) {
                        popUpTo(MyScreens.SignInScreen.route) {
                            inclusive = true
                        }
                    }
                }) {
                    Text(
                        "Register here",
                        color = Blue
                    )
                }
            }
        }
    }
}

@Composable
fun MainTextField(
    edtValue: String,
    icon: Int,
    hint: String,
    onValueChanges: (String) -> Unit
) {
    OutlinedTextField(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        placeholder = { Text(hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painterResource(icon), null) }
    )
}

@Composable
fun PasswordTextField(
    edtValue: String,
    icon: Int,
    hint: String,
    onValueChanges: (String) -> Unit
) {
    val passwordVisible = remember { mutableStateOf(false) }
    OutlinedTextField(
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = onValueChanges,
        placeholder = { Text(hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        shape = Shapes.medium,
        leadingIcon = { Icon(painterResource(icon), null) },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image =
                if (passwordVisible.value) painterResource(R.drawable.ic_invisible)
                else painterResource(R.drawable.ic_visible)
            Icon(
                painter = image,
                contentDescription = null,
                modifier = Modifier.clickable { passwordVisible.value = !passwordVisible.value })
        }
    )
}

fun clearInputs(viewModel: SignInViewModel) {
    viewModel.email.value = ""
    viewModel.password.value = ""
}