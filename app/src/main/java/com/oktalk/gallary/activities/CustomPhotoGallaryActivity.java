package com.oktalk.gallary.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.oktalk.gallary.R;

public class CustomPhotoGallaryActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_FROM_EXTERNAL_SOURCE = 5;
    private GridView grdImages;
    private Button btnSelect;

    private ImageAdapter imageAdapter;
    private String[] arrPath;
    private boolean[] thumbnailsselection;
    private int ids[];
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_photo_gallary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        grdImages= (GridView) findViewById(R.id.grdImages);
        btnSelect= (Button) findViewById(R.id.btnSelect);



        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

            } else {

                //TODO : Request for permissions

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_FROM_EXTERNAL_SOURCE);


            }
        }



    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_FROM_EXTERNAL_SOURCE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO : permission is granted
                    Toast.makeText(getApplicationContext(), "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();

                    final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
                    final String orderBy = MediaStore.Images.Media._ID;
                    Cursor imagecursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                    int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
                    this.count = imagecursor.getCount();
                    this.arrPath = new String[this.count];
                    ids = new int[count];
                    this.thumbnailsselection = new boolean[this.count];
                    for (int i = 0; i < this.count; i++) {
                        imagecursor.moveToPosition(i);
                        ids[i] = imagecursor.getInt(image_column_index);
                        int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        arrPath[i] = imagecursor.getString(dataColumnIndex);
                    }

                    imageAdapter = new ImageAdapter();
                    grdImages.setAdapter(imageAdapter);
                    imagecursor.close();


                    btnSelect.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            final int len = thumbnailsselection.length;
                            int cnt = 0;
                            String selectImages = "";

                            for (int i = 0; i < len; i++) {
                                if (thumbnailsselection[i]) {
                                    cnt++;
                                    selectImages = selectImages + arrPath[i] + "|";
                                }
                            }
                            if (cnt == 0) {
                                Toast.makeText(getApplicationContext(), "Please select at least one image", Toast.LENGTH_LONG).show();
                            } else {

                                Log.d("SelectedImages", selectImages);
                                Intent intent = new Intent();
                                intent.putExtra("data", selectImages);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                        }
                    });

                } else {
                    //TODO: If permission is denied
                    Toast.makeText(getApplicationContext(), "PERMISSION DENIED", Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }


    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        super.onBackPressed();

    }


    /**
     * This method used to set bitmap.
     * @param iv represented ImageView
     * @param id represented id
     */

    private void setBitmap(final ImageView iv, final int id) {

        new AsyncTask<Void, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Void... params) {
                return MediaStore.Images.Thumbnails.getThumbnail(getApplicationContext().getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                iv.setImageBitmap(result);
            }
        }.execute();
    }



    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;

        public ImageAdapter() {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.gallary_item, null);
                holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
                holder.chkImage = (CheckBox) convertView.findViewById(R.id.chkImage);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.chkImage.setId(position);
            holder.imgThumb.setId(position);
            holder.chkImage.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int id = cb.getId();
                    if (thumbnailsselection[id]) {
                        cb.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        cb.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
            holder.imgThumb.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    int id = holder.chkImage.getId();
                    if (thumbnailsselection[id]) {
                        holder.chkImage.setChecked(false);
                        thumbnailsselection[id] = false;
                    } else {
                        holder.chkImage.setChecked(true);
                        thumbnailsselection[id] = true;
                    }
                }
            });
            try {
                setBitmap(holder.imgThumb, ids[position]);
            } catch (Throwable e) {
            }
            holder.chkImage.setChecked(thumbnailsselection[position]);
            holder.id = position;
            return convertView;
        }
    }


    /**
     * Inner class
     * @author tasol
     */
    class ViewHolder {
        ImageView imgThumb;
        CheckBox chkImage;
        int id;
    }

}