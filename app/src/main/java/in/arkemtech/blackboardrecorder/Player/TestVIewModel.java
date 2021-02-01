package in.arkemtech.blackboardrecorder.Player;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingModels;
import in.arkemtech.blackboardrecorder.Repository.RecordingRepository;

public class TestVIewModel extends AndroidViewModel {


    private RecordingRepository repository;
    private LiveData<List<RecordingModels>> allRecodings;

    public TestVIewModel(@NonNull Application application) {
        super(application);
        repository = new RecordingRepository(application);
        allRecodings = repository.getAllRecordings();
    }
}
