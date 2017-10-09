package trainedge.social_wallpaper_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import trainedge.social_wallpaper_app.R;

public class Browse extends AppCompatActivity implements View.OnClickListener {
    static final int REQUEST_IMAGE_GET = 1;
    private Uri fullPhotoUri;
    private ImageView iv1;
    private Boolean imageSelected = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private TextView tvProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse);
        iv1 = (ImageView) findViewById(R.id.Iv1);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        Button btnupload = (Button) findViewById(R.id.btnupload);
        iv1.setOnClickListener(this);
        btnupload.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Iv1:
                selectImage();
                break;
            case R.id.btnupload:
                if (imageSelected) {
                    uploadImage();
                } else {
                    Toast.makeText(Browse.this, "Select an Image to be uploaded", Toast.LENGTH_SHORT).show();


                }
                break;

        }

    }

    private void uploadImage() {
        iv1.setDrawingCacheEnabled(true);
        iv1.buildDrawingCache();
        Bitmap bitmap = iv1.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        String path = "ImageUpload/" + UUID.randomUUID() + ".jpeg";
        StorageReference storageRef = storage.getReference(path);


        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(Browse.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @VisibleForTesting final
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference reference = database.getReference("Images");
                        reference.push().setValue(downloadUrl.toString());

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        Intent intent = new Intent(Browse.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }.execute(downloadUrl.toString());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                @VisibleForTesting
                long byteCount = taskSnapshot.getTotalByteCount();
                @VisibleForTesting
                long transferred = taskSnapshot.getBytesTransferred();
                tvProgress.setText(transferred + "/" + byteCount);
            }
        });

    }

    public void selectImage() {
        Intent home = new Intent(Intent.ACTION_GET_CONTENT);
        home.setType("image/*");
        if (home.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(home, REQUEST_IMAGE_GET);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            fullPhotoUri = data.getData();
            imageSelected = true;
            iv1.setImageURI(fullPhotoUri);
            // Do work with photo saved at fullPhotoUri
        }
    }
}

