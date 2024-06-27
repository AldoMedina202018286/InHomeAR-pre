package pe.edu.upc.inhomear

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.ar.core.Config
import pe.edu.upc.inhomear.ui.theme.InHomeARTheme
import pe.edu.upc.inhomear.ui.theme.Translucent
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode

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
                        val currentModel = remember {
                            mutableStateOf("Chair")
                        }
                        ARScreen(currentModel.value)
                        Menu(modifier = Modifier.align(Alignment.BottomCenter)) {
                            currentModel.value = it
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Menu(
    modifier: Modifier,
    onClick: (String) -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    val itemsList = listOf(
        Furniture("Chair", R.drawable.chair),
        Furniture("Armchair", R.drawable.armchair),
        Furniture("Desk", R.drawable.desk),
        Furniture("Table", R.drawable.table),
    )

    fun updateIndex(offset: Int) {
        currentIndex = (currentIndex + offset + itemsList.size) % itemsList.size
        onClick(itemsList[currentIndex].name)
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

@Composable
fun ARScreen(
    model: String
) {
    val nodeList = remember {
        mutableListOf<ArNode>()
    }
    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }
    val placeModelButton = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodeList,
            planeRenderer = true,
            onCreate = { arSceneView ->
                arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneView.planeRenderer.isShadowReceiver = false
                modelNode.value = ArModelNode(
                    arSceneView.engine,
                    PlacementMode.INSTANT
                ).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "${model}.glb",
                    ) {

                    }
                    onAnchorChanged = {
                        placeModelButton.value = !isAnchored
                    }
                    onHitResult = { nodeList, hitResult ->
                        placeModelButton.value = nodeList.isTracking
                    }
                }
                nodeList.add(modelNode.value!!)
            },
            onSessionCreate = {
                planeRenderer.isVisible = false
            }
        )
        if (placeModelButton.value) {
            Button(
                onClick = {
                    modelNode.value?.anchor()
                },
                modifier = Modifier.align(Alignment.Center)
                /*enabled = !placeModelButton.value*/
            ) {
                Text("Place Model")
            }
        }
    }

    LaunchedEffect(key1 = model) {
        modelNode.value?.loadModelGlbAsync(
            glbFileLocation = "${model}.glb",
            scaleToUnits = 0.8f
        ) {
            /*modelNode.value?.anchor()*/
        }
        Log.e("Error loading model", "ERROR LOADING MODEL")
    }
}


data class Furniture(
    val name: String,
    val image: Int,
)
