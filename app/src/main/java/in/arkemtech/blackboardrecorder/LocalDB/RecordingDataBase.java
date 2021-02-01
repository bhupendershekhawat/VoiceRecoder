package in.arkemtech.blackboardrecorder.LocalDB;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {RecordingModels.class}, version = 1)
public abstract class RecordingDataBase extends RoomDatabase {

    public static RecordingDataBase instance;

    public abstract RecordingDao recordingDao();

    public static synchronized RecordingDataBase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext(),
                    RecordingDataBase.class, "recording_DataBase")
                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }



    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private RecordingDao recordingDao;
        private PopulateDbAsyncTask(RecordingDataBase db) {
            recordingDao = db.recordingDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            recordingDao.insert(new RecordingModels("Title 1", "Description 1"));
            recordingDao.insert(new RecordingModels("Title 2", "Description 2"));
            recordingDao.insert(new RecordingModels("Title 3", "Description 3"));
            return null;
        }
    }


}
