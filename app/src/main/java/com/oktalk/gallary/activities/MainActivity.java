package com.oktalk.gallary.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.oktalk.gallary.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LinearLayout lnrImages;
    Button addPhotos;
    private ArrayList<String> imagesPathList;

    private ImageView imageView;
    private final int PICK_IMAGE_MULTIPLE =1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        imageView = (ImageView) findViewById(R.id.iv_root);

        lnrImages = (LinearLayout) findViewById(R.id.lnrImages);
        addPhotos = (Button) findViewById(R.id.btnAddPhots);



        addPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });


    }

    public void onButtonClick(View view){
        switch (view.getId()){
            case R.id.btnAddPhots :
                Intent intent = new Intent(MainActivity.this, CustomPhotoGallaryActivity.class);
                startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_MULTIPLE) {
                imagesPathList = new ArrayList<String>();
                String[] imagePath = data.getStringExtra("data").split("\\|"); //check if all Uri are there
                try {
                    lnrImages.removeAllViews();
                }catch (Throwable e){
                    e.printStackTrace();
                }

                imageView.setVisibility(View.GONE);

                for(String path : imagePath){
                    imagesPathList.add(path);
                    ImageView imageView = new ImageView(this);
                    Picasso.with(getApplicationContext()).load("file:///" + path).resize(700, 600)
                            .centerCrop().into(imageView);
                    imageView.setPadding(1, 5, 1, 5);
                    imageView.setAdjustViewBounds(true);
                    lnrImages.addView(imageView);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
