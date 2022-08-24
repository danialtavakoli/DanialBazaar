package com.danialtavakoli.danialbazaar.ui.features.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.danialtavakoli.danialbazaar.R
import com.danialtavakoli.danialbazaar.ui.features.product.MainTextField
import com.danialtavakoli.danialbazaar.ui.theme.Blue
import com.danialtavakoli.danialbazaar.ui.theme.Shapes
import com.danialtavakoli.danialbazaar.util.MyScreens
import com.danialtavakoli.danialbazaar.util.showToast
import com.danialtavakoli.danialbazaar.util.styleTime
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val navigation = getNavController()
    val viewModel = getNavViewModel<ProfileViewModel>()
    viewModel.loadUserData()

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileToolbar { navigation.popBackStack() }
            MainAnimation()
            Spacer(modifier = Modifier.padding(top = 6.dp))
            ShowDataSection("Email Address", viewModel.email.value, null)
            ShowDataSection("Address", viewModel.address.value) {
                viewModel.locationDialog.value = true
            }
            ShowDataSection("Postal Code", viewModel.postalCode.value) {
                viewModel.locationDialog.value = true
            }
            ShowDataSection("Login Time", styleTime(viewModel.loginTime.value.toLong()), null)
            Button(
                onClick = {
                    context.showToast("Hope to see you again")
                    viewModel.signOut()
                    navigation.navigate(MyScreens.MainScreen.route) {
                        popUpTo(MyScreens.MainScreen.route) {
                            inclusive = true
                        }
                        navigation.popBackStack()
                        navigation.popBackStack()
                    }
                }, modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 36.dp)
            ) {
                Text(text = "Sign Out")
            }
        }
    }

    if (viewModel.locationDialog.value) AddUserLocationDataDialog(
        false,
        { viewModel.locationDialog.value = false },
        { address, postalCode, _ -> viewModel.setUserLocation(address, postalCode) })
}

@Composable
fun ProfileToolbar(OnBackClicked: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = OnBackClicked) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        elevation = 2.dp,
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "My Profile",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(end = 58.dp)
                    .fillMaxWidth()
            )
        }
    )
}

@Composable
fun MainAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile_anim))
    LottieAnimation(
        composition = composition,
        modifier = Modifier
            .size(270.dp)
            .padding(top = 36.dp, bottom = 16.dp),
        iterations = LottieConstants.IterateForever
    )
}

@Composable
fun ShowDataSection(subject: String, textToShow: String, OnLocationClicked: (() -> Unit)?) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .clickable { OnLocationClicked?.invoke() },
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = subject,
            style = TextStyle(fontSize = 18.sp, color = Blue, fontWeight = FontWeight.Bold)
        )
        Text(
            text = textToShow,
            modifier = Modifier.padding(top = 2.dp),
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
        )
        Divider(color = Blue, thickness = 0.5.dp, modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
fun AddUserLocationDataDialog(
    showSaveLocation: Boolean,
    onDismiss: () -> Unit,
    onSubmitClicked: (String, String, Boolean) -> Unit
) {
    val context = LocalContext.current
    val checkedState = remember { mutableStateOf(true) }
    val userAddress = remember { mutableStateOf("") }
    val userPostalCode = remember { mutableStateOf("") }
    val fraction = if (showSaveLocation) 0.695f else 0.625f

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxHeight(fraction),
            elevation = 8.dp,
            shape = Shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "Add Location Data",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                MainTextField(userAddress.value, "Your address...") {
                    userAddress.value = it
                }
                MainTextField(userPostalCode.value, "Your postal code...") {
                    userPostalCode.value = it
                }
                if (showSaveLocation) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                        )
                        Text(text = "Save To Profile")
                    }
                }

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) { Text(text = "Cancel") }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {
                        if (
                            (userAddress.value.isNotEmpty() || userAddress.value.isNotBlank()) &&
                            (userPostalCode.value.isNotEmpty() || userPostalCode.value.isNotBlank())
                        ) {
                            onSubmitClicked(
                                userAddress.value,
                                userPostalCode.value,
                                checkedState.value
                            )
                            onDismiss.invoke()
                        } else context.showToast("Please enter your information")
                    }) { Text(text = "OK") }
                }
            }
        }
    }
}