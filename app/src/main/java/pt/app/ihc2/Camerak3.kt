package pt.app.ihc2


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position


//get models 3d https://sketchfab.com/3d-models/leptinotarsa-decemlineata-4c23e2622a8749979f49ad1632cab94d
class Camerak3 : AppCompatActivity() {

    private lateinit var sceneView: ArSceneView
    lateinit var placeButton: ExtendedFloatingActionButton
    private lateinit var modelNode: ArModelNode


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerak3)

        var numeroModelo = getIntent().getIntExtra("numeroModelo",0)
        var modeloS = ""
        if (numeroModelo == 1){
            modeloS = "models/beetles.glb"
        }else if (numeroModelo == 2){
            modeloS = "models/arvore.glb"
        }else{
            Toast.makeText(this, "Não existem modelos proximos", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

        sceneView = findViewById<ArSceneView?>(R.id.sceneView).apply {
            this.lightEstimationMode = Config.LightEstimationMode.DISABLED
        }

        placeButton = findViewById(R.id.place)

        placeButton.setOnClickListener {
            placeModel()
        }

        modelNode = ArModelNode(sceneView.engine,PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = modeloS,
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