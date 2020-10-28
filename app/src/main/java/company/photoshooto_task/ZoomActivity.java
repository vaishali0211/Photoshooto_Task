package company.photoshooto_task;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zolad.zoominimageview.ZoomInImageView;

public class ZoomActivity extends AppCompatActivity {

    ZoomInImageView img;
    String image="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        img = (ZoomInImageView)findViewById(R.id.img_zoom);
        image = getIntent().getExtras().getString("Image");

        Picasso.get().load(image).into(img);

    }
}