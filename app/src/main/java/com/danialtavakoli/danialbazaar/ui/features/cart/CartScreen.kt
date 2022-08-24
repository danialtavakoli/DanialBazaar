package com.danialtavakoli.danialbazaar.ui.features.cart

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.danialtavakoli.danialbazaar.R
import com.danialtavakoli.danialbazaar.model.data.Product
import com.danialtavakoli.danialbazaar.ui.features.profile.AddUserLocationDataDialog
import com.danialtavakoli.danialbazaar.ui.theme.Blue
import com.danialtavakoli.danialbazaar.ui.theme.PriceBackground
import com.danialtavakoli.danialbazaar.ui.theme.Shapes
import com.danialtavakoli.danialbazaar.util.*
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun CartScreen() {
    val context = LocalContext.current
    val viewModel = getNavViewModel<CartViewModel>()
    val navigation = getNavController()
    val getDataDialogState = remember { mutableStateOf(false) }
    viewModel.loadCartData()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 74.dp)
        ) {
            CartToolbar(
                { navigation.popBackStack() },
                { navigation.navigate(MyScreens.ProfileScreen.route) }
            )
            if (viewModel.productList.value.isNotEmpty())
                CartList(
                    viewModel.productList.value,
                    viewModel.isChangingNumber.value,
                    { viewModel.addItem(it) },
                    { viewModel.removeItem(it) },
                    { navigation.navigate(MyScreens.ProductScreen.route + "/" + it) }
                )
            else NoDataAnimation()
        }
        PurchaseAll(totalPrice = viewModel.totalPrice.value.toString()) {
            if (viewModel.productList.value.isNotEmpty()) {
                val userLocation = viewModel.getUserLocation()
                if (userLocation.first == "Click to add" || userLocation.second == "Click to add")
                    getDataDialogState.value = true
                else viewModel.purchaseAll(
                    userLocation.first,
                    userLocation.second
                ) { success, link ->
                    if (success) {
                        context.showToast("Pay using zarinpal")
                        viewModel.setPurchaseStatus(PAYMENT_PENDING)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        context.startActivity(intent)
                    } else context.showToast("There is problem in payment")
                }
            } else context.showToast("Please add some products first")
        }
        if (getDataDialogState.value)
            AddUserLocationDataDialog(
                true,
                { getDataDialogState.value = false },
                { address, postalCode, isChecked ->
                    if (NetworkChecker(context).isInternetConnected) {
                        if (isChecked) viewModel.setUserLocation(address, postalCode)
                        viewModel.purchaseAll(address, postalCode) { success, link ->
                            if (success) {
                                context.showToast("Pay using zarinpal")
                                viewModel.setPurchaseStatus(PAYMENT_PENDING)
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                context.startActivity(intent)
                            } else context.showToast("There is problem in payment")
                        }
                    } else context.showToastInternet()
                }
            )
    }
}

@Composable
fun CartItem(
    product: Product,
    isChangingNumber: Pair<String, Boolean>,
    OnAddItemClicked: (String) -> Unit,
    OnRemoveItemClicked: (String) -> Unit,
    OnItemClicked: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { OnItemClicked.invoke(product.productId) },
        elevation = 4.dp,
        shape = Shapes.large
    ) {
        Column {
            AsyncImage(
                model = product.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(
                        text = product.name,
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "From " + product.category + " Group",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        text = "Product authenticity guarantee",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Available in stock to ship",
                        style = TextStyle(fontSize = 14.sp)
                    )
                    Surface(
                        modifier = Modifier
                            .padding(bottom = 6.dp, top = 18.dp)
                            .clip(Shapes.large),
                        color = PriceBackground
                    ) {
                        Text(
                            text = stylePrice(
                                (product.price.toInt() *
                                        (product.quantity ?: "1").toInt()).toString()
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        )
                    }

                }
                Surface(
                    modifier = Modifier
                        .padding(bottom = 14.dp, end = 8.dp)
                        .align(Alignment.Bottom)
                ) {
                    Card(border = BorderStroke(2.dp, Blue)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (product.quantity?.toInt() == 1) {
                                IconButton(onClick = { OnRemoveItemClicked.invoke(product.productId) }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                }
                            } else {
                                IconButton(onClick = { OnRemoveItemClicked.invoke(product.productId) }) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_minus),
                                        contentDescription = null,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                }
                            }
                            if (isChangingNumber.first == product.productId && isChangingNumber.second) {
                                Text(
                                    text = "...",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            } else {
                                Text(
                                    text = product.quantity ?: "1",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            IconButton(onClick = { OnAddItemClicked.invoke(product.productId) }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartList(
    data: List<Product>,
    isChangingNumber: Pair<String, Boolean>,
    OnAddItemClicked: (String) -> Unit,
    OnRemoveItemClicked: (String) -> Unit,
    OnItemClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(data.size) {
            CartItem(
                product = data[it],
                isChangingNumber = isChangingNumber,
                OnAddItemClicked = OnAddItemClicked,
                OnRemoveItemClicked = OnRemoveItemClicked,
                OnItemClicked = OnItemClicked
            )
        }
    }
}

@Composable
fun CartToolbar(
    OnBackClicked: () -> Unit,
    OnProfileClicked: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { OnBackClicked.invoke() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        elevation = 2.dp,
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "My Cart",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                textAlign = TextAlign.Center
            )
        },
        actions = {
            IconButton(
                onClick = { OnProfileClicked.invoke() },
                modifier = Modifier.padding(end = 6.dp)
            ) { Icon(imageVector = Icons.Default.Person, contentDescription = null) }
        }
    )
}


@Composable
fun NoDataAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}

@Composable
fun PurchaseAll(totalPrice: String, OnPurchaseClicked: () -> Unit) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val fraction =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.07f
    Surface(
        color = Color.White, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (NetworkChecker(context).isInternetConnected) OnPurchaseClicked.invoke()
                    else context.showToastInternet()
                },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(182.dp, 40.dp)
            ) {
                Text(
                    text = "Let's Purchase!",
                    modifier = Modifier.padding(2.dp),
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }
            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(Shapes.large),
                color = PriceBackground
            ) {
                Text(
                    text = "Total: " + stylePrice(totalPrice),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}