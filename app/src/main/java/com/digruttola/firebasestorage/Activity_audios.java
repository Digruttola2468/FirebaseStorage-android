package com.digruttola.firebasestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Activity_audios extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference audioRef = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/v0/b/fir-storage-c7c2d.appspot.com/o/Archivos%2FKaleb%20Di%20Masi%20%E2%9D%8C%20Omar%20Varela%20-%20Pal%20Cache.mp3?alt=media&token=e7a3a0e5-ecce-4ebe-84a1-5457317ad835");

    MediaPlayer mediaPlayer;

    private Button btStart,btStop,btPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audios);

        btStart = findViewById(R.id.btAudiosStart);
        btStop = findViewById(R.id.btAudiosStop);
        btPause = findViewById(R.id.btAudioPause);

        audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );

                try {

                    mediaPlayer.setDataSource(Activity_audios.this, uri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                }catch (Exception e){
                    Log.w("TAG",e.getMessage());
                }
            }
        });


        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
            }
        });

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

    }
}