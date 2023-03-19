package in.notyouraveragedev.tensor_image_classification;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class Splashscreen  extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //making splash screen from a library
      //  int logoImg = getResources().getIdentifier("@drawable/ic_banana", null, this.getPackageName());
        EasySplashScreen config = new EasySplashScreen(Splashscreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(4500);
               // .withLogo(R.drawable.ic_banana);

        //config.getLogo().setMaxHeight(575);
        //config.getLogo().setMaxWidth(575);
        View easySplashScreen = config.create();
        //setContentView(easySplashScreen);
    }
}
