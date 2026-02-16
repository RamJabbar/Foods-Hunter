package com.example.foodshunter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodshunter.data.model.Restaurant
import com.example.foodshunter.ui.theme.*
import com.example.foodshunter.R


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun RestaurantCardPreview() {
//    RestaurantCard(
//        restaurant = Restaurant(
//            id = "1",
//            name = "Warung Makan Enak",
//            description = "Makanan khas nusantara dengan rasa autentik dan harga terjangkau",
//            imageUrl = "",
//            rating = 4.6,
//            category = "Nusantara"
//        ),
//        onClick = {},
//        modifier = Modifier.padding(16.dp)
//
//    )
//}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onClick: () -> Unit,
    modifier: Modifier

) {
    val cardshape = RoundedCornerShape(18.dp)
    Card(
        modifier = modifier
            .clip(cardshape)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = cardshape,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cardRestaurant)
    ) {
        Column {
            AsyncImage(
                model = restaurant.imageUrl.ifEmpty {
                    "https://via.placeholder.com/400x200/FF6B35/FFFFFF?text=${restaurant.name}"
                },
                contentDescription = restaurant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(15.dp)) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.robotovariablefontwdthwght))
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = restaurant.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = white,
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Font(R.font.tomorrowmediumitalic))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Oren,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            color = white,
                            text = String.format("%.1f", restaurant.rating),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = restaurant.category,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = white,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
