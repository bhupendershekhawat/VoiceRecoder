package in.arkemtech.blackboardrecorder.LocalDB;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "BB_Recorder_Table")
public class RecordingModels {

    private String filename;

    private String flieUrl;

    @PrimaryKey(autoGenerate = true)
    protected int id;

    public RecordingModels(String filename, String flieUrl) {
        this.filename = filename;
        this.flieUrl = flieUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public String getFlieUrl() {
        return flieUrl;
    }
}
