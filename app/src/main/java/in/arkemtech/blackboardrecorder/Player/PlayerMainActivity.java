package in.arkemtech.blackboardrecorder.Player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import in.arkemtech.blackboardrecorder.R;

import android.content.Intent;
import android.os.Bundle;

public class PlayerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlaylistFragment playlistFragment = new PlaylistFragment();
        fragmentTransaction.replace(R.id.player_container, playlistFragment, "playlistFragment");
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {



        int count = getFragmentManager().getBackStackEntryCount();


        if (count == 0) {



            final PlaylistFragment playlistFragment = (PlaylistFragment)getSupportFragmentManager().findFragmentByTag("playlistFragment");

            if (playlistFragment != null && playlistFragment.isVisible()) {

              finish();

            }else {


                super.onBackPressed();


            }

        } else {


            getFragmentManager().popBackStack();

        }




    }


}