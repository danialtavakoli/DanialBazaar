package com.danialtavakoli.danialbazaar.ui.features

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.danialtavakoli.danialbazaar.R
import com.danialtavakoli.danialbazaar.ui.theme.BackgroundMain
import com.danialtavakoli.danialbazaar.ui.theme.Blue
import com.danialtavakoli.danialbazaar.ui.theme.MainAppTheme
import com.danialtavakoli.danialbazaar.util.MyScreens
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController


@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            IntroScreen()
        }
    }
}

@Composable
fun IntroScreen() {
    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Blue) }
    val navigation = getNavController()
    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(R.drawable.img_intro),
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.78f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            onClick = { navigation.navigate(MyScreens.SignUpScreen.route) },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(
                text = "Sign Up"
            )
        }
        Button(
            onClick = { navigation.navigate(MyScreens.SignInScreen.route) },
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
        ) {
            Text(
                text = "Sign In",
                color = Blue
            )
        }
    }
}