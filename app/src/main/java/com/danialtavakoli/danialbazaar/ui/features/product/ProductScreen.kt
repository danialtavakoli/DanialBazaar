package com.danialtavakoli.danialbazaar.ui.features.product

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.danialtavakoli.danialbazaar.R
import com.danialtavakoli.danialbazaar.model.data.Comment
import com.danialtavakoli.danialbazaar.model.data.Product
import com.danialtavakoli.danialbazaar.ui.theme.*
import com.danialtavakoli.danialbazaar.util.*
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    MainAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundMain
        ) {
            ProductScreen("")
        }
    }
}

@Composable
fun ProductScreen(productID: String) {
    val context = LocalContext.current
    val viewModel = getNavViewModel<ProductViewModel>()
    viewModel.loadData(productID, NetworkChecker(context).isInternetConnected)
    val navigation = getNavController()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 58.dp)
        ) {
            ProductToolbar(
                productName = "Details",
                badgeNumber = viewModel.badgeNumber.value,
                OnBackClicked = { navigation.popBackStack() },
                OnCartClicked = {
                    if (NetworkChecker(context).isInternetConnected)
                        navigation.navigate(MyScreens.CartScreen.route)
                    else context.showToastInternet()
                }
            )
            val comments =
                if (NetworkChecker(context).isInternetConnected) viewModel.comments.value else listOf()
            ProductItem(
                viewModel.thisProduct.value,
                comments,
                { navigation.navigate(MyScreens.CategoryScreen.route + "/" + it) },
                { viewModel.addNewComment(productID, it) { text -> context.showToast(text) } }
            )

        }
        AddToCart(viewModel.thisProduct.value.price, viewModel.isAddingProduct.value) {
            if (NetworkChecker(context).isInternetConnected)
                viewModel.addProductToCart(productID) { context.showToast(it) }
            else context.showToastInternet()
        }
    }
}

@Composable
fun ProductItem(
    data: Product,
    comments: List<Comment>,
    OnCategoryClicked: (String) -> Unit,
    OnAddNewComment: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        ProductDesign(data, OnCategoryClicked)
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 14.dp, bottom = 14.dp)
        )
        ProductDetail(data, comments.size)
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
        )
        ProductComments(comments, OnAddNewComment)
    }
}

@Composable
fun ProductComments(comments: List<Comment>, AddNewComment: (String) -> Unit) {
    val showCommentDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    if (comments.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Comments",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium)
            )
            TextButton(onClick = {
                if (NetworkChecker(context).isInternetConnected) showCommentDialog.value = true
                else context.showToastInternet()
            }) {
                Text(text = "Add new comment")
            }
        }
        comments.forEach { CommentBody(comment = it) }
    } else {
        TextButton(onClick = {
            if (NetworkChecker(context).isInternetConnected) showCommentDialog.value = true
            else context.showToastInternet()
        }) {
            Text(text = "Add new comment")
        }
    }
    if (showCommentDialog.value) AddNewCommentDialog(
        OnDismiss = { showCommentDialog.value = false },
        OnPositiveClick = { AddNewComment.invoke(it) }
    )
}

@Composable
fun AddNewCommentDialog(OnDismiss: () -> Unit, OnPositiveClick: (String) -> Unit) {
    val context = LocalContext.current
    val comment = remember { mutableStateOf("") }
    Dialog(onDismissRequest = OnDismiss) {
        Card(
            modifier = Modifier.fillMaxHeight(0.53f),
            elevation = 8.dp,
            shape = Shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Write your comment",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                MainTextField(edtValue = comment.value, hint = "Write something") {
                    comment.value = it
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { OnDismiss.invoke() }) { Text(text = "Cancel") }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {
                        if (comment.value.isNotEmpty() && comment.value.isNotBlank()) {
                            if (NetworkChecker(context).isInternetConnected) {
                                OnPositiveClick(comment.value)
                                OnDismiss.invoke()
                            } else context.showToastInternet()
                        } else context.showToast("Please write first")
                    }) {
                        Text(text = "OK")
                    }
                }
            }
        }
    }
}

@Composable
fun MainTextField(edtValue: String, hint: String, OnValueChanges: (String) -> Unit) {
    OutlinedTextField(
        label = { Text(hint) },
        value = edtValue,
        singleLine = true,
        onValueChange = OnValueChanges,
        placeholder = { Text(text = "Write something") },
        modifier = Modifier.fillMaxWidth(0.9f),
        shape = Shapes.medium,
        maxLines = 2
    )
}

@Composable
fun CommentBody(comment: Comment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        elevation = 0.dp,
        border = BorderStroke(1.dp, Color.LightGray),
        shape = Shapes.large
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = comment.userEmail,
                style = TextStyle(fontSize = 15.sp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = comment.text,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Composable
fun ProductDetail(product: Product, commentNumber: Int) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.ic_details_comment),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                val commentText =
                    if (NetworkChecker(context).isInternetConnected) "$commentNumber Comments" else "No internet"
                Text(
                    text = commentText,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_details_material),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = product.material,
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_details_sold),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = product.soldItem + " Sold",
                    modifier = Modifier.padding(start = 6.dp),
                    fontSize = 13.sp
                )
            }
        }
        Surface(
            modifier = Modifier
                .clip(Shapes.large)
                .align(Alignment.Bottom),
            color = Blue
        ) {
            Text(
                text = product.tags,
                color = Color.White,
                modifier = Modifier.padding(6.dp),
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun ProductDesign(product: Product, OnCategoryClicked: (String) -> Unit) {
    AsyncImage(
        model = product.imgUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(Shapes.medium)
    )
    Text(
        text = product.name,
        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium),
        modifier = Modifier.padding(top = 14.dp)
    )
    Text(
        text = product.detailText,
        style = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Justify),
        modifier = Modifier.padding(top = 4.dp)
    )
    TextButton(onClick = { OnCategoryClicked.invoke(product.category) }) {
        Text(
            text = "#" + product.category,
            style = TextStyle(fontSize = 13.sp)
        )
    }
}

@Composable
fun ProductToolbar(
    productName: String,
    badgeNumber: Int,
    OnBackClicked: () -> Unit,
    OnCartClicked: () -> Unit
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
                text = productName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                textAlign = TextAlign.Center
            )
        },
        actions = {
            IconButton(
                onClick = { OnCartClicked.invoke() },
                modifier = Modifier.padding(end = 6.dp)
            ) {
                if (badgeNumber == 0)
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                else
                    BadgedBox(badge = { Badge { Text(text = badgeNumber.toString()) } }) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    }
            }
        }
    )
}

@Composable
fun AddToCart(price: String, isAddingProduct: Boolean, OnCartClicked: () -> Unit) {
    val configuration = LocalConfiguration.current
    val fraction =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.08f
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(fraction)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { OnCartClicked.invoke() },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(182.dp, 40.dp)
            ) {
                if (isAddingProduct) DotsTyping()
                else Text(
                    text = "Add product to cart",
                    modifier = Modifier.padding(2.dp),
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                )
            }
            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(Shapes.large),
                color = PriceBackground,
            ) {
                Text(
                    text = stylePrice(price),
                    style = TextStyle(fontSize = 14.sp),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun DotsTyping() {
    val dotSize = 10.dp
    val delayUnit = 350
    val maxOffset = 10f

    @Composable
    fun Dot(offset: Float) = Spacer(
        Modifier
            .size(dotSize)
            .offset(y = -offset.dp)
            .background(
                color = Color.White,
                shape = CircleShape
            )
            .padding(start = 8.dp, end = 8.dp)
    )

    val infiniteTransition = rememberInfiniteTransition()

    @Composable
    fun animateOffsetWithDelay(delay: Int) = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = delayUnit * 4
                0f at delay with LinearEasing
                maxOffset at delay + delayUnit with LinearEasing
                0f at delay + delayUnit * 2
            }
        )
    )

    val offset1 by animateOffsetWithDelay(0)
    val offset2 by animateOffsetWithDelay(delayUnit)
    val offset3 by animateOffsetWithDelay(delayUnit * 2)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = maxOffset.dp)
    ) {
        val spaceSize = 2.dp
        Dot(offset1)
        Spacer(Modifier.width(spaceSize))
        Dot(offset2)
        Spacer(Modifier.width(spaceSize))
        Dot(offset3)
    }
}
