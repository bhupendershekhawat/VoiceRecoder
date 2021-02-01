package in.arkemtech.blackboardrecorder.LocalDB;


import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public  interface RecordingDao {

    @Insert
    void insert(RecordingModels recordingModels);

    @Update
    void update(RecordingModels recordingModels);

    @Delete
    void delete(RecordingModels recordingModels);

    @Query("Delete FROM BB_Recorder_Table")
    void deleteAllRecordingd();


    @Query("SELECT *  FROM BB_Recorder_Table ORDER BY id DESC")
    LiveData<List<RecordingModels>> getAllRecordings();



}
