package com.danialtavakoli.danialbazaar.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danialtavakoli.danialbazaar.di.myModules
import com.danialtavakoli.danialbazaar.model.repository.TokenInMemory
import com.danialtavakoli.danialbazaar.model.repository.user.UserRepository
import com.danialtavakoli.danialbazaar.ui.features.IntroScreen
import com.danialtavakoli.danialbazaar.ui.features.cart.CartScreen
import com.danialtavakoli.danialbazaar.ui.features.category.CategoryScreen
import com.danialtavakoli.danialbazaar.ui.features.main.MainScreen
import com.danialtavakoli.danialbazaar.ui.features.product.ProductScreen
import com.danialtavakoli.danialbazaar.ui.features.profile.ProfileScreen
import com.danialtavakoli.danialbazaar.ui.features.signIn.SignInScreen
import com.danialtavakoli.danialbazaar.ui.features.signUp.SignUpScreen
import com.danialtavakoli.danialbazaar.ui.theme.BackgroundMain
import com.danialtavakoli.danialbazaar.ui.theme.MainAppTheme
import com.danialtavakoli.danialbazaar.util.KEY_CATEGORY_ARG
import com.danialtavakoli.danialbazaar.util.KEY_PRODUCT_ARG
import com.danialtavakoli.danialbazaar.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContent {
            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                MainAppTheme {
                    Surface(
                        color = BackgroundMain,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val userRepository: UserRepository = get()
                        userRepository.loadToken()
                        DanialBazaarUI()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            DanialBazaarUI()
        }
    }
}

@Composable
fun DanialBazaarUI() {
    val navController = rememberNavController()
    KoinNavHost(navController = navController, startDestination = MyScreens.MainScreen.route) {
        composable(MyScreens.MainScreen.route) {
            if (TokenInMemory.token != null) MainScreen()
            else IntroScreen()
        }
        composable(
            route = MyScreens.ProductScreen.route + "/" + "{$KEY_PRODUCT_ARG}",
            arguments = listOf(navArgument(KEY_PRODUCT_ARG) { type = NavType.StringType })
        ) { ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG, "null")) }
        composable(
            route = MyScreens.CategoryScreen.route + "/" + "{$KEY_CATEGORY_ARG}",
            arguments = listOf(navArgument(KEY_CATEGORY_ARG) { type = NavType.StringType })
        ) { CategoryScreen(it.arguments!!.getString(KEY_CATEGORY_ARG, "null")) }
        composable(MyScreens.ProfileScreen.route) { ProfileScreen() }
        composable(MyScreens.CartScreen.route) { CartScreen() }
        composable(MyScreens.SignUpScreen.route) { SignUpScreen() }
        composable(MyScreens.SignInScreen.route) { SignInScreen() }
    }
}