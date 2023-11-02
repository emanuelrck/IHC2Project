package pt.app.ihc2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Clipboard extends AppCompatActivity {

    private boolean visitedFonte;
    private boolean visitedArv;
    private Button FonteT;
    private Button FonteF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard);

        initBtns();
        showBtns();

    }
    private void initBtns(){
        setContentView(R.layout.activity_clipboard);
        FonteF = findViewById(R.id.desactiveFonte);
        FonteT = findViewById(R.id.activeFonte);
    }

    private void openFonteInfo(){
        Toast.makeText(this, "Fonte info window", Toast.LENGTH_SHORT).show();
    }
    private  void showBtns(){
        visitedFonte = getIntent().getBooleanExtra("fonte",false);
        if (visitedFonte){
            FonteF.setVisibility(View.GONE);
            FonteT.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    openFonteInfo();
                }
            });
        }else {
            FonteT.setVisibility(View.GONE);
        }
    }
}