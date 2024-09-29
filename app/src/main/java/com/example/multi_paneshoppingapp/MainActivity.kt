package com.example.multi_paneshoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multi_paneshoppingapp.ui.theme.MultipaneShoppingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultipaneShoppingAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Shopper(
                        products = products,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Shopper(products: List<Product>, modifier: Modifier = Modifier) {
    val selectedProduct = remember { mutableStateOf<Product?>(null) }

    // Step 1: Detect the screen orientation
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == 2 // 2 means landscape

    if (isLandscape) {
        // Step 2: Landscape mode - display both the product list and details side by side
        Row(modifier = modifier.fillMaxSize()) {
            ProductList(products = products, onProductClick = { product ->
                selectedProduct.value = product
            }, modifier = Modifier.weight(1f))

            ProductDetails(selectedProduct = selectedProduct.value, modifier = Modifier.weight(1f))
        }
    } else {
        // Step 3: Portrait mode - display either the product list or the details screen
        if (selectedProduct.value == null) {
            ProductList(products = products, onProductClick = { product ->
                selectedProduct.value = product
            }, modifier = Modifier.fillMaxSize())
        } else {
            ProductDetails(selectedProduct = selectedProduct.value, modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun ProductList(products: List<Product>, onProductClick: (Product) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(products) { product ->
            Text(
                text = product.name,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onProductClick(product) }
            )
        }
    }
}

@Composable
fun ProductDetails(selectedProduct: Product?, modifier: Modifier = Modifier) {
    selectedProduct?.let {
        Text(
            text = "Name: ${it.name}\nPrice: ${it.price}\nDescription: ${it.description}",
            modifier = modifier.padding(16.dp)
        )
    } ?: run {
        Text(
            text = "Select a product to view details",
            modifier = modifier.padding(16.dp)
        )
    }
}

data class Product(val name: String, val price: String, val description: String)

val products = listOf(
    Product("Product A", "$100", "This is a great product A."),
    Product("Product B", "$150", "This is product B with more features."),
    Product("Product C", "$200", "Premium product C.")
)

@Preview(showBackground = true)
@Composable
fun ShopperPreview() {
    MultipaneShoppingAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Shopper(
                products = products,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}