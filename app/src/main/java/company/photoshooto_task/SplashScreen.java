package company.photoshooto_task;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import company.photoshooto_task.Helper.FontType;

public class SplashScreen extends AppCompatActivity {

    TextView getStart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ViewGroup root = (ViewGroup)findViewById(R.id.root);
        FontType fontType=new FontType(SplashScreen.this,root);

        getStart = (TextView)findViewById(R.id.get_start);

        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}