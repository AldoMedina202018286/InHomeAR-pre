package pe.edu.upc.inhomear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pe.edu.upc.inhomear.ui.theme.InHomeARTheme
import pe.edu.upc.inhomear.ui.theme.Translucent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InHomeARTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Menu(modifier = Modifier.align(Alignment.BottomCenter))
                    }
                }
            }
        }
    }
}

@Composable
fun Menu(
    modifier: Modifier
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val itemsList = listOf(
        Furniture("Chair", R.drawable.chair),
        Furniture("Armchair", R.drawable.armchair),
        Furniture("Desk", R.drawable.desk),
        Furniture("Table", R.drawable.table),
    )

    fun updateIndex(offset: Int) {
        currentIndex = (currentIndex + offset + itemsList.size) % itemsList.size
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            updateIndex(-1)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_left_24),
                contentDescription = "Back"
            )
        }

        FurnitureCard(imageId = itemsList[currentIndex].image)

        IconButton(onClick = {
            updateIndex(1)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                contentDescription = "Next"
            )
        }
    }
}

@Composable
fun FurnitureCard(
    modifier: Modifier = Modifier,
    imageId: Int,
) {
    Box(
        modifier = modifier
            .padding(2.dp)
            .size(100.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(width = 1.dp, Translucent, shape = RoundedCornerShape(10.dp))
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "Furniture",
            contentScale = ContentScale.FillBounds
        )
    }
}


data class Furniture(
    val name: String,
    val image: Int,
)
