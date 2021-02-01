package in.arkemtech.blackboardrecorder.Repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import androidx.lifecycle.LiveData;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingDao;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingDataBase;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingModels;

public class RecordingRepository {

    private RecordingDao recordingDao;
    private LiveData<List<RecordingModels>> allRecordings;


    public RecordingRepository(Application application) {

        RecordingDataBase recordingDataBase =   RecordingDataBase.getInstance(application);

        this.recordingDao = recordingDataBase.recordingDao();
        this.allRecordings = recordingDao.getAllRecordings();
    }

    public void insert(RecordingModels models) {
        new InsertRecordingAsyncTask(recordingDao).execute(models);
    }


    public void update (RecordingModels models) {
        new updateRecordingAsyncTask(recordingDao).execute(models);
    }

    public void delete (RecordingModels models) {
        new deleteRecordingAsyncTask(recordingDao).execute(models);
    }


    public void deleteAll () {
        new deleteAllRecordingAsyncTask(recordingDao).execute();
    }


    public LiveData<List<RecordingModels>> getAllRecordings() {
        return allRecordings;
    }

    private static class InsertRecordingAsyncTask  extends AsyncTask<RecordingModels ,Void , Void> {

          private RecordingDao recordingDao;

        public InsertRecordingAsyncTask(RecordingDao recordingDao) {
            this.recordingDao = recordingDao;
        }


        @Override
        protected Void doInBackground(RecordingModels... recordingModels) {

            recordingDao.insert(recordingModels[0]);

            return null;
        }
    }


    private static class updateRecordingAsyncTask  extends AsyncTask<RecordingModels ,Void , Void> {

        private RecordingDao recordingDao;

        public updateRecordingAsyncTask(RecordingDao recordingDao) {
            this.recordingDao = recordingDao;
        }


        @Override
        protected Void doInBackground(RecordingModels... recordingModels) {

            recordingDao.update(recordingModels[0]);

            return null;
        }
    }


    private static class deleteRecordingAsyncTask  extends AsyncTask<RecordingModels ,Void , Void> {

        private RecordingDao recordingDao;

        public deleteRecordingAsyncTask(RecordingDao recordingDao) {
            this.recordingDao = recordingDao;
        }


        @Override
        protected Void doInBackground(RecordingModels... recordingModels) {

            recordingDao.delete(recordingModels[0]);

            return null;
        }
    }


    private static class deleteAllRecordingAsyncTask  extends AsyncTask<Void, Void, Void>{

        private RecordingDao recordingDao;
        public deleteAllRecordingAsyncTask(RecordingDao recordingDao) {
            this.recordingDao = recordingDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            recordingDao.deleteAllRecordingd();
            return null;
        }
    }
}
