package com.danialtavakoli.danialbazaar.util

sealed class MyScreens(val route: String) {
    object MainScreen : MyScreens("mainScreen")
    object ProductScreen : MyScreens("productScreen")
    object CategoryScreen : MyScreens("categoryScreen")
    object ProfileScreen : MyScreens("profileScreen")
    object CartScreen : MyScreens("cartScreen")
    object SignUpScreen : MyScreens("signUpScreen")
    object SignInScreen : MyScreens("signInScreen")
}
