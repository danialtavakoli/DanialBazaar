package com.danialtavakoli.danialbazaar.ui.features.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danialtavakoli.danialbazaar.model.data.Product
import com.danialtavakoli.danialbazaar.ui.theme.Blue
import com.danialtavakoli.danialbazaar.ui.theme.Shapes
import com.danialtavakoli.danialbazaar.util.MyScreens
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun CategoryScreen(categoryName: String) {
    val viewModel = getNavViewModel<CategoryViewModel>()
    viewModel.loadData(categoryName)
    val navigation = getNavController()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CategoryToolbar(categoryName)
        val data = viewModel.dataProducts
        CategoryList(data.value) {
            navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
        }
    }
}

@Composable
fun CategoryItem(product: Product, onProductClicked: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .clickable { onProductClicked.invoke(product.productId) },
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
                        text = product.price + " Tomans",
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
                Surface(
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 8.dp)
                        .align(Alignment.Bottom)
                        .clip(Shapes.large),
                    color = Blue
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = product.soldItem + " Sold",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryList(data: List<Product>, onProductClicked: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 16.dp)) {
        items(data.size) { CategoryItem(data[it], onProductClicked) }
    }
}

@Composable
fun CategoryToolbar(categoryName: String) {
    TopAppBar(
        elevation = 0.dp,
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = categoryName,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    )
}