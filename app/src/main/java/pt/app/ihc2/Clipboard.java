package pt.app.ihc2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Clipboard extends AppCompatActivity {

    private boolean visitedFonte;
    private boolean visitedArv;
    private Button FonteT;
    private Button FonteF;
    private Button ArvF;
    private Button ArvT;
    private Button voltar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard);

        initBtns();
        showBtns();
        voltar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
    private void initBtns(){
        setContentView(R.layout.activity_clipboard);
        FonteF = findViewById(R.id.desactiveFonte);
        FonteT = findViewById(R.id.activeFonte);
        ArvF = findViewById(R.id.desactiveArv);
        ArvT = findViewById(R.id.activeArv);
        voltar = findViewById(R.id.voltar);
    }

    private void openFonteInfo(){
        Intent intent = new Intent(this,FonteInfo.class);
        startActivity(intent);

    }
    private void openArvInfo(){
        Toast.makeText(this, "Arv info window", Toast.LENGTH_SHORT).show();
    }

    private  void showBtns(){
        visitedFonte = getIntent().getBooleanExtra("fonte",false);
        visitedArv = getIntent().getBooleanExtra("arvore",false);


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

        if (visitedArv){
            ArvF.setVisibility(View.GONE);
            ArvT.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    openArvInfo();
                }
            });
        }else {
            ArvT.setVisibility(View.GONE);
        }
    }
}