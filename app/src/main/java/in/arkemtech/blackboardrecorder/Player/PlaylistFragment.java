package in.arkemtech.blackboardrecorder.Player;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingModels;
import in.arkemtech.blackboardrecorder.R;
import in.arkemtech.blackboardrecorder.utils.ItemClickSupport;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {

    private RecyclerView recyclerView;
    RecodingViewModel recodingViewModel;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlaylistFragment() {
        // Required empty public constructor
    }




    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        instView(view);
        return view;
    }

    private void instView(View view) {

        recyclerView = view.findViewById(R.id.RCW_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final RecordItemAdapter adapter = new RecordItemAdapter();

        recodingViewModel =  ViewModelProviders.of(this).get(RecodingViewModel.class);
        recodingViewModel.getAllRecodings().observe(this, new Observer<List<RecordingModels>>() {
            @Override
            public void onChanged(List<RecordingModels> recordingModels) {

                adapter.setRecoding(recordingModels);
                Toast.makeText(getContext(), "onChanged" + recordingModels.size(), Toast.LENGTH_SHORT).show();


            }
        });


        recyclerView.setAdapter(adapter);

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {

                String  mFileURL = ((TextView)view.findViewById(R.id.recodingUrl)).getText().toString();
                String  recodingName = ((TextView)view.findViewById(R.id.recodingName)).getText().toString();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PlayerFragement playerFragement =  PlayerFragement.newInstance(mFileURL,recodingName,position+1);
        fragmentTransaction.replace(R.id.player_container, playerFragement, "playerFragement");
        fragmentTransaction.addToBackStack("playerFragement");
        fragmentTransaction.commit();

            }
        });

    }


}