package com.example.foodshunter.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodshunter.data.model.MenuItem
import com.example.foodshunter.ui.theme.Oren
import com.example.foodshunter.ui.theme.harga
import java.text.NumberFormat
import java.util.*

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuItemCardPreview() {
    MenuItemCard(
        menuItem = MenuItem(
            id = "1",
            name = "Nasi Goreng",
            description = "Ayam geprek pedas level dewa",
            price = 18000.0,
            imageUrl = ""
        ),
        onAddToCart = {},
        modifier = Modifier.padding(16.dp)

    )

}


@Composable
fun MenuItemCard(
    menuItem: MenuItem,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Menu Image
            AsyncImage(
                model = menuItem.imageUrl.ifEmpty {
                    "https://via.placeholder.com/100x100/FF6B35/FFFFFF?text=${menuItem.name.take(1)}"
                },
                contentDescription = menuItem.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Menu Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = menuItem.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = menuItem.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = formatRupiah(menuItem.price),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = harga
                    )
                }
            }

            // Add Button
            FilledTonalIconButton(
                onClick = onAddToCart,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(40.dp),
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = Oren
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah ke keranjang",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Format price ke format Rupiah
 */

private fun formatRupiah(price: Double): String{
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(price).replace(",00","")

}