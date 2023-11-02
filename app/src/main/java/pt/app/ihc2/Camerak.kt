package pt.app.ihc2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode
import pt.app.ihc2.ui.theme.IHC2Theme

//models https://www.youtube.com/watch?v=DaWrqUqjrE8&t=205s
class Camerak : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            IHC2Theme {
                Surface (modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background){
                    Box(modifier = Modifier.fillMaxSize()){
                        ARScreen()
                    }

                }

            }
        }
    }
}


@Composable
fun Menu(modifier: Modifier){
    var currentIndex by remember{
        mutableStateOf(0)
    }
    val itemsList = listOf(
        Insect("inseto1", R.drawable.fonte_marker)
    )
    fun updateIndex(offset: Int){
        currentIndex = (currentIndex + itemsList.size) % itemsList.size
    }
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround) {

        IconButton(onClick = {
            updateIndex(-1)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.clipboard),
                contentDescription = "previous"
            )
        }
        CircularImage(imageId = itemsList[currentIndex].imgeId)
        IconButton(onClick = {
            updateIndex(1)
        }) {
            Icon(painter = painterResource(id = R.drawable.caminhada), contentDescription = "next")

        }
    }
}

@Composable
fun ARScreen() {
    val nodes = remember {
        mutableListOf<ArNode>()
    }
    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }

    val placeModelButton = remember {
        mutableStateOf(false)
    }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        nodes = nodes,
        planeRenderer = true,
        onCreate = { arSceneView ->
            arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
            arSceneView.planeRenderer.isShadowReceiver = false
            modelNode.value = ArModelNode(arSceneView.engine, PlacementMode.INSTANT).apply {
                loadModelGlbAsync(
                    glbFileLocation = "models/beetles.glb",
                )
                {

                }
                onAnchorChanged = {
                    placeModelButton.value = !isAnchored
                }
                onHitResult = { node, hitResult ->
                    placeModelButton.value = node.isTracking
                }

            }
            nodes.add(modelNode.value!!)
        }
    )
    if(placeModelButton.value)
    {
        Button(onClick = {
            modelNode.value?.anchor()
        }) {
            Text(text = "Place it")
        }
    }
}

@Composable
fun CircularImage(modifier: Modifier = Modifier,
    imageId: Int){
    Box(modifier = modifier
        .size(140.dp)
        .clip(CircleShape)
        .border(width = 3.dp, Color(0x9E1F1F1F))){
        Image(painter = painterResource(id = imageId), contentDescription = null, contentScale = ContentScale.FillBounds)
    }
}

data class Insect(var name: String, var imgeId: Int)