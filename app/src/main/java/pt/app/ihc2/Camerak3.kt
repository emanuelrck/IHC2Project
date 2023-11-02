package pt.app.ihc2

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class Camerak3 : AppCompatActivity() {

    private lateinit var sceneView: ArSceneView
    lateinit var placeButton: ExtendedFloatingActionButton
    private lateinit var modelNode: ArModelNode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerak3)
        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = Config.LightEstimationMode.DISABLED
        }



        placeButton = findViewById(R.id.place)

        placeButton.setOnClickListener {
            placeModel()
        }



        modelNode = ArModelNode(sceneView.engine,PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = "models/beetles.glb",
                scaleToUnits = 1f,
                centerOrigin = Position(-0.5f)

            )
            {
                sceneView.planeRenderer.isVisible = true
                val materialInstance = it.materialInstances[0]
            }
            onAnchorChanged = {
                placeButton.isGone = it != null
            }

        }
        sceneView.addChild(modelNode)


    }

    private fun placeModel(){
        modelNode.anchor()

        sceneView.planeRenderer.isVisible = false

    }

}