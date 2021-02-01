package in.arkemtech.blackboardrecorder.MainRecoder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingModels;
import in.arkemtech.blackboardrecorder.Player.PlayerMainActivity;
import in.arkemtech.blackboardrecorder.Player.RecodingViewModel;
import in.arkemtech.blackboardrecorder.R;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecorderMainActivity extends AppCompatActivity {

    private Button playList, save;

    CheckBox record;

    private MediaRecorder mRecorder;

    private MediaPlayer mPlayer;

    private static String mFileName = null;

    String mFileName1 = null;
    String mFileName2 = null;

    String recordingName;

    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    public static final int ADD_RECORDING_REQUEST = 1;

    boolean recording = false;

    private  RecodingViewModel recodingViewModel;

    private long pauseOffset;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playList = (Button) findViewById(R.id.playList);
        save = (Button) findViewById(R.id.save);
        record = findViewById(R.id.record);

        recodingViewModel =  ViewModelProviders.of(this).get(RecodingViewModel.class);


        record.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {

                    if (recording) {
                        resumeRecord();
                    } else {
                        StartRecording();
                    }


                } else {

                    if (recording) {
                        pauseRecord();
                    }

                }

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recording) {

                    stopRecording();
                }


            }
        });

        playList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoPlayer();

            }
        });


    }

    private void pauseRecord() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mRecorder.pause();
            Toast.makeText(RecorderMainActivity.this, "Recording paused", Toast.LENGTH_LONG).show();

        }

    }


    private void gotoPlayer() {

        Intent intent = new Intent(RecorderMainActivity.this, PlayerMainActivity.class);
//        intent.putExtra("mFileName", mFileName);
        startActivity(intent);
    }

    private void resumeRecord() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mRecorder.resume();
            Toast.makeText(RecorderMainActivity.this, "Recording resumed", Toast.LENGTH_LONG).show();

        }

    }



    private void StartRecording() {

        if (CheckPermissions()) {


            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/"+ System.currentTimeMillis()+".3gp";

            recordingName = "Recording"+System.currentTimeMillis()+".3gp";


            mRecorder = new MediaRecorder();

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mRecorder.setOutputFile(mFileName);
            try {

                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }


            mRecorder.start();

            recording = true;

            Toast.makeText(RecorderMainActivity.this, "Recording Started", Toast.LENGTH_LONG).show();


        } else {

            RequestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                        StartRecording();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(RecorderMainActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }



    public void stopRecording() {


        savefileintoDB(mFileName);
        mRecorder.stop();
        recording = false;
        mRecorder.release();
//        mRecorder = null;
        record.setChecked(false);


    }

    private void savefileintoDB( String mFileName){

        RecordingModels note = new RecordingModels(recordingName, mFileName);
        recodingViewModel.insert(note);
        Toast.makeText(RecorderMainActivity.this, "Recording Saved", Toast.LENGTH_LONG).show();

    }


}

