package in.notyouraveragedev.tensor_image_classification;

import android.Manifest;
import android.bluetooth.le.ScanSettings;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.DialogCompat;
import android.graphics.drawable.AnimatedImageDrawable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.notyouraveragedev.tensor_image_classification.classifier.ImageClassifier;

/**
 * The Main Activity Class
 * <p>
 * Created by A Anand on 11-05-2020
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Requests Codes to identify camera and permission requests
     */
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1000;
    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final String TAG = "RESULTS";

    /**
     * UI Elements
     */
    private ImageView imageView;
    private ListView listView;
    private ImageClassifier imageClassifier;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initalizing ui elements
        initializeUIElements();
    }

    /**
     * Method to initalize UI Elements. this method adds the on click
     */
    private void initializeUIElements() {
        imageView = findViewById(R.id.iv_capture);
        listView = findViewById(R.id.lv_probabilities);
        Button takepicture = findViewById(R.id.bt_take_picture);
        ImageButton faqbut = findViewById(R.id.imageButton2);
        ImageButton ext = findViewById(R.id.imageButton3);

        /*
         * Creating an instance of our tensor image classifier
         */
        try {
            imageClassifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("Image Classifier Error", "ERROR: " + e);
        }

        // adding on click listener to button
        takepicture.setOnClickListener(new View.OnClickListener() {
            FrameLayout lol = findViewById(R.id.logowelcome);
            @Override
            public void onClick(View v) {
                // checking whether camera permissions are available.
                // if permission is avaialble then we open camera intent to get picture
                // otherwise reqeusts for permissions
                lol.setVisibility(View.INVISIBLE);
                if (hasPermission()) {
                    openCamera();
                } else {
                    requestPermission();
                }
            }
        });

        faqbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfaq();
            }
        });

        final DialogInterface.OnClickListener dialogClickListeners = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };
        ext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builde = new AlertDialog.Builder(MainActivity.this);
                builde.setMessage(
                        "Are you sure you want to exit?")
                        .setIcon(R.drawable.ic_exit)
                        .setPositiveButton("Yes ", dialogClickListeners)
                        .setNegativeButton("Cancel", dialogClickListeners).show();
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // if this is the result of our camera image request
        TextView textView = (TextView) findViewById(R.id.TextView);
        TextView bf = (TextView) findViewById(R.id.benfts);
        ScrollView unripelist = (ScrollView) findViewById(R.id.unripebens);
        ScrollView ripelist = (ScrollView) findViewById(R.id.ripebens);
        ScrollView overripelist = (ScrollView) findViewById(R.id.overripebens);
        bf.setVisibility(View.VISIBLE);

        if (requestCode == CAMERA_REQUEST_CODE) {
            // getting bitmap of the image
            Bitmap photo = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
            // displaying this bitmap in imageview
            imageView.setImageBitmap(photo);

            // pass this bitmap to classifier to make prediction
            List<ImageClassifier.Recognition> predicitons = imageClassifier.recognizeImage(
                    photo, 0);

            // creating a list of string to display in list view
            final List<String> predicitonsList = new ArrayList<>();
            for (ImageClassifier.Recognition recog : predicitons) {
                    predicitonsList.add(recog.getName());
                    predicitonsList.add(String.valueOf(recog.getConfidence()));

            }
            ripelist.setVisibility(View.INVISIBLE);
            unripelist.setVisibility((View.INVISIBLE));
            overripelist.setVisibility(View.INVISIBLE);
            if (predicitonsList.get(0).equals("Kardava")) {
                textView.setText("The Banana is Overripe Kardava.");
            }
            else if(predicitonsList.get(0).equals("Lakatan")){
                textView.setText("The Banana is Ripe Kardava.");
                ripelist.setVisibility(View.VISIBLE);
            }
            else if(predicitonsList.get(0).equals("Tundan")){
                textView.setText("The Banana is Unripe Kardava.");
                unripelist.setVisibility(View.VISIBLE);
            }
            else if(predicitonsList.get(0).equals("0 lakatan")){
                textView.setText("The Banana is Unripe Lakatan.");
                ripelist.setVisibility(View.VISIBLE);
            }
            else if(predicitonsList.get(0).equals("1 lakatan")){
                textView.setText("The Banana is Ripe Lakatan.");
                unripelist.setVisibility(View.VISIBLE);
            }
            else if(predicitonsList.get(0).equals("2 lakatan")){
                textView.setText("The Banana is Overripe Lakatan.");
                overripelist.setVisibility(View.VISIBLE);
            }
            else if(predicitonsList.get(0).equals("0 tundan")){
                textView.setText("The Banana is Unripe Latundan.");
                unripelist.setVisibility(View.VISIBLE);
            }
            else if(predicitonsList.get(0).equals("1 tundan")){
                textView.setText("The Banana is Ripe Latundan.");
                ripelist.setVisibility(View.VISIBLE);
            }
            else if(predicitonsList.get(0).equals("2 tundan")){
                textView.setText("The Banana is Unripe.");
                overripelist.setVisibility(View.VISIBLE);
            }
            else{
                textView.setText("Banana not Found!");
                bf.setVisibility(View.INVISIBLE);
                unripelist.setVisibility(View.INVISIBLE);
                ripelist.setVisibility(View.INVISIBLE);
                overripelist.setVisibility(View.INVISIBLE);
            }

            // creating an array adapter to display the classification result in list view
            ArrayAdapter<String> predictionsAdapter = new ArrayAdapter<>(
                    this, R.layout.support_simple_spinner_dropdown_item, predicitonsList);
            listView.setAdapter(predictionsAdapter);
            //Log.d(TAG, "onActivityResult: Outside Loop "+predicitonsList.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // if this is the result of our camera permission request
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (hasAllPermissions(grantResults)) {
                openCamera();
            } else {
                requestPermission();
            }
        }
    }

    /**
     * checks whether all the needed permissions have been granted or not
     *
     * @param grantResults the permission grant results
     * @return true if all the reqested permission has been granted,
     * otherwise returns false
     */
    private boolean hasAllPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    /**
     * Method requests for permission if the android version is marshmallow or above
     */
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // whether permission can be requested or on not
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(this, "Camera Permission Required", Toast.LENGTH_SHORT).show();
            }
            // request the camera permission permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * creates and starts an intent to get a picture from camera
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    /**
     * checks whether camera permission is available or not
     *
     * @return true if android version is less than marshmallo,
     * otherwise returns whether camera permission has been granted or not
     */
    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void openfaq(){
        Intent intent2 = new Intent(MainActivity.this, Faqactivity.class);
        startActivity(intent2);
    }

}
