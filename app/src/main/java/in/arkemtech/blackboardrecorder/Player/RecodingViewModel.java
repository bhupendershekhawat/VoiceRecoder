package in.arkemtech.blackboardrecorder.Player;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingModels;
import in.arkemtech.blackboardrecorder.Repository.RecordingRepository;

public class RecodingViewModel extends AndroidViewModel {

   private RecordingRepository repository;
   private LiveData<List<RecordingModels>> allRecodings;

    private MutableLiveData<String> recordingURL;

    public RecodingViewModel(Application application) {
        super(application);
        repository = new RecordingRepository(application);
        allRecodings = repository.getAllRecordings();
    }

    public void  insert (RecordingModels recordingModels){
        repository.insert(recordingModels);
    }

    public void  delete (RecordingModels recordingModels){
        repository.delete(recordingModels);
    }


    public void  deleteAll (RecordingModels recordingModels){
        repository.deleteAll();
    }

    public LiveData<List<RecordingModels>> getAllRecodings(){

        return allRecodings;
    }

    public void setRecordingURL(String recordingURL){
        this.recordingURL.setValue(recordingURL);
    }

    public MutableLiveData<String> getRecordingURL(){
        return recordingURL;
    }
}
