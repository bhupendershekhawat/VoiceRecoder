package in.arkemtech.blackboardrecorder.Player;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingModels;
import in.arkemtech.blackboardrecorder.R;

public class PlayerFragement extends Fragment implements Runnable{

    private static final String FILE_URL = "mFileURL";
    private static final String FILE_NAME = "mFileName";
    private static final String CURRENT_SONG_INDEX = "currentSongIndex";

    private String mFileURL;
    private String mFileName;

    MediaPlayer mPlayer   = new MediaPlayer();;
    CheckBox repeat, shuffle, Play_checkbox;
    SeekBar seekBar;
    TextView audioTime;
    Button show_List, skip_next ,skip_previous ;
    int currentSongIndex;
    boolean isShuffle = false;

    List<RecordingModels> songsList = new ArrayList<>();
    RecodingViewModel  recodingViewModel;


    public PlayerFragement() {
        // Required empty public constructor
    }


    public static PlayerFragement newInstance(String mFileURL, String mFileName, int currentSongIndex) {
        PlayerFragement fragment = new PlayerFragement();
        Bundle args = new Bundle();
        args.putString(FILE_URL, mFileURL);
        args.putString(FILE_NAME, mFileName);
        args.putInt(CURRENT_SONG_INDEX, currentSongIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFileURL = getArguments().getString(FILE_URL);
            mFileName = getArguments().getString(FILE_NAME);
            currentSongIndex = getArguments().getInt(CURRENT_SONG_INDEX);
        }

        recodingViewModel =  ViewModelProviders.of(requireActivity()).get(RecodingViewModel.class);
        recodingViewModel.getAllRecodings().observe(getActivity(), new Observer<List<RecordingModels>>() {
            @Override
            public void onChanged(List<RecordingModels> recordingModels) {

                setRecoding(recordingModels);



            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_fragement, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        instView(view);
        CreateFirstAudio(mFileURL);



    }

    @Override
    public void onDetach() {
        super.onDetach();
        clearMediaPlayer();
    }

    private void instView(View view) {

        Play_checkbox = view.findViewById(R.id.Play_checkbox);
        seekBar = view.findViewById(R.id.seekBar);
        audioTime = view.findViewById(R.id.audioTime);
        repeat = view.findViewById(R.id.repeat);
        show_List = view.findViewById(R.id.show_List);
        shuffle = view.findViewById(R.id.shuffle);
        skip_previous = view.findViewById(R.id.skip_previous);
        skip_next = view.findViewById(R.id.skip_next);


        seekBar.setOnSeekBarChangeListener(seekBaronSeekBarChangeListener);
        repeat.setOnCheckedChangeListener(repeatOnCheckedChangeListener);
        Play_checkbox.setOnCheckedChangeListener(Play_checkboxOnCheckedChangeListener);
        show_List.setOnClickListener(show_List_OnClickListener);
        shuffle.setOnCheckedChangeListener(shuffleOnCheckedChangeListener);

        skip_previous.setOnClickListener(skip_previousOnClickListener);

        skip_next.setOnClickListener(skip_nextOnClickListener);

    }

    public void setRecoding(List<RecordingModels> modelsArrayList) {
        this.songsList = modelsArrayList;

    }


    View.OnClickListener skip_previousOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            playpreviousSong();
        }
    };

    View.OnClickListener skip_nextOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            playnextSong();
        }
    };

    SeekBar.OnSeekBarChangeListener seekBaronSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int CurrentPosition, boolean b) {
            audioTime.setText(getTimeString(CurrentPosition));

            int x = (int) Math.ceil(CurrentPosition / 1000f);

            if (CurrentPosition == mPlayer.getDuration()){


                if (!mPlayer.isLooping()){

                    Play_checkbox.setChecked(false);
                }

                PlayerFragement.this.seekBar.setProgress(0);

            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mPlayer.seekTo(seekBar.getProgress());

        }
    };


    View.OnClickListener show_List_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            getActivity().onBackPressed();

        }
    };

    CompoundButton.OnCheckedChangeListener shuffleOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

            if (checked){
                isShuffle = true;
            }else {
                isShuffle = false;
            }
        }
    };

    CompoundButton.OnCheckedChangeListener repeatOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

            if (checked){
                loopingEnable();
            }else {
                loopingDisable();
            }
        }
    };


    CompoundButton.OnCheckedChangeListener Play_checkboxOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

            if (checked){

                musicplay();

            }else {

                musicpause();

            }
        }
    };


    public void CreateAudio(int songIndex) {

        try {

            if (mFileName!= null){
                mPlayer.reset();
                mPlayer.setDataSource(songsList.get(songIndex).getFlieUrl());
                mPlayer.prepare();
                seekBar.setMax(mPlayer.getDuration());
                mPlayer.setOnCompletionListener(mPlayerOnCompletionListener);
                Play_checkbox.setChecked(true);
                musicplay();
            }


        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void CreateFirstAudio(String mFileURL) {



        try {

            if (mFileName!= null){
                mPlayer.reset();
                mPlayer.setDataSource(mFileURL);
                mPlayer.prepare();
                seekBar.setMax(mPlayer.getDuration());
                mPlayer.setOnCompletionListener(mPlayerOnCompletionListener);
                Play_checkbox.setChecked(true);
                musicplay();
            }


        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    MediaPlayer.OnCompletionListener mPlayerOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            if (repeat.isChecked()) {
                // repeat is on play same song again
                CreateAudio(currentSongIndex);

            } else if (isShuffle) {
                // shuffle is on - play a random song
                Random rand = new Random();
                currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
                CreateAudio(currentSongIndex);

            } else {
                // no repeat or shuffle ON - play next song
                if (currentSongIndex < (songsList.size() - 1)) {
                    CreateAudio(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                } else {
                    // play first song
                    CreateAudio(0);
                    currentSongIndex = 0;
                }
            }
        }
    };


    private void  playnextSong(){

        if (currentSongIndex < (songsList.size() - 1)) {
            CreateAudio(currentSongIndex + 1);
            currentSongIndex = currentSongIndex + 1;
        } else {
            // play first song
            CreateAudio(0);
            currentSongIndex = 0;
        }

    }

    private void  playpreviousSong(){

        if (currentSongIndex !=0 && currentSongIndex < (songsList.size() - 1)) {
            CreateAudio(currentSongIndex - 1);
            currentSongIndex = currentSongIndex - 1;
        } else {
            // play first song
            CreateAudio(0);
            currentSongIndex = 0;
        }

    }
    private void loopingEnable(){

        mPlayer.setLooping(true);
    }

    private void loopingDisable(){

        mPlayer.setLooping(false);
    }

    public void musicplay() {
        mPlayer.start();
//        Toast.makeText(PlayerFragement.this.getContext(), "Music Started", Toast.LENGTH_LONG).show();
        new Thread(this).start();
    }

    // Pausing the music
    private void musicpause() {
        mPlayer.pause();
//        Toast.makeText(PlayerFragement.this.getContext(), "Music Paused", Toast.LENGTH_LONG).show();

    }

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    public void run() {

        int currentPosition = mPlayer.getCurrentPosition();
        int total = mPlayer.getDuration();



        while (mPlayer != null && mPlayer.isPlaying() && currentPosition < total) {
            try {
                Thread.sleep(1000);
                currentPosition = mPlayer.getCurrentPosition();
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                return;
            }

            seekBar.setProgress(currentPosition);


        }
    }

    private void clearMediaPlayer() {
        mPlayer.stop();
        mPlayer.release();
//        mPlayer= null;
    }

}