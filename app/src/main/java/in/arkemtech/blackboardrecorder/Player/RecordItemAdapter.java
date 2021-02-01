package in.arkemtech.blackboardrecorder.Player;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.arkemtech.blackboardrecorder.LocalDB.RecordingModels;
import in.arkemtech.blackboardrecorder.R;

public class RecordItemAdapter extends  RecyclerView.Adapter<RecordItemAdapter.RecordViewHolder> {

    private List<RecordingModels> modelsArrayList = new ArrayList<>();


    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recording_list_dialog_list_dialog_item, parent,false);
        return new RecordViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        RecordingModels recordingModels = modelsArrayList.get(position);

        holder.recodingName.setText(recordingModels.getFilename());
        holder.recodingUrl.setText(recordingModels.getFlieUrl());
    }

    public void setRecoding(List<RecordingModels> modelsArrayList) {
        this.modelsArrayList = modelsArrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return modelsArrayList.size();
    }

    class RecordViewHolder extends RecyclerView.ViewHolder{

        private TextView recodingName ,recodingUrl;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);

            recodingName = itemView.findViewById(R.id.recodingName);
            recodingUrl = itemView.findViewById(R.id.recodingUrl);
        }
    }
}
