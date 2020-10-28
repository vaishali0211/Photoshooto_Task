package company.photoshooto_task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import company.photoshooto_task.Fragment.CameraShoppingFragment;
import company.photoshooto_task.Fragment.HomeFragment;
import company.photoshooto_task.Fragment.ProfileFragment;

public class Home extends AppCompatActivity {

    BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new HomeFragment()).commit();

        bnv = (BottomNavigationView)findViewById(R.id.bottomNavigation);
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment temp = null;

                switch (item.getItemId())
                {
                    case R.id.menu_home : temp = new HomeFragment();
                    break;
                    case R.id.menu_camera : temp = new CameraShoppingFragment();
                    break;
                    case R.id.menu_profile : temp = new ProfileFragment();
                    break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,temp).commit();
                return true;
            }
        });
    }
}