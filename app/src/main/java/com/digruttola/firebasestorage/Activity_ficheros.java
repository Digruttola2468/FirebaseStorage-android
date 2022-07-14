package com.digruttola.firebasestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Activity_ficheros extends AppCompatActivity {

    VideoView viewVideo;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference videoRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/fir-storage-c7c2d.appspot.com/o/Archivos%2FVID-20220115-WA0007.mp4?alt=media&token=42dd372a-2143-4223-9b49-ab4b7c6a024e");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficheros);

        viewVideo = findViewById(R.id.videoView);

        videoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TAG",String.valueOf(uri));
                viewVideo.setVideoURI(uri);

                viewVideo.start();

                MediaController mediaController = new MediaController(Activity_ficheros.this);
                viewVideo.setMediaController(mediaController);
                mediaController.setAnchorView(viewVideo);
            }
        });



    }
}