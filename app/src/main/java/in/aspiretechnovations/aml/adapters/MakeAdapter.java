package in.aspiretechnovations.aml.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.callbacks.CallbackFilter;
import in.aspiretechnovations.aml.model.MakeModel;
import in.aspiretechnovations.aml.utils.AppPref;

public class MakeAdapter extends RecyclerView.Adapter<MakeAdapter.MakeHolder> {

    private Context context;
    private ArrayList<MakeModel> arrayList;
    private CallbackFilter callbackFilter;
    private String selected_id = "0";
    private String vehicle_type;

    public MakeAdapter(Context context, ArrayList<MakeModel> arrayList, CallbackFilter callbackFilter, String vehicle_type) {
        this.context = context;
        this.arrayList = arrayList;
        this.callbackFilter = callbackFilter;
        this.vehicle_type = vehicle_type;
    }

    @NonNull
    @Override
    public MakeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_make, parent, false);
        return new MakeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MakeHolder holder, int position) {

        final MakeModel makeModel = arrayList.get(position);

//        Log.d("DBMakerID",makeModel.getMaker_id());
//        Log.d("DBTitle",makeModel.getTitle());
//        Log.d("DBVehicle",makeModel.getVehicle_type());

        if (makeModel != null) {
            String title = makeModel.getTitle();

            selected_id = AppPref.getSelectedMakerId(context);

            if (!selected_id.contentEquals("0")) {
                if (selected_id.contentEquals(makeModel.getMaker_id()))
                    holder.tv_selected.setVisibility(View.VISIBLE);
                else
                    holder.tv_selected.setVisibility(View.GONE);
            }

            if (title != null)
                holder.tv_title.setText(title);

            holder.frame_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tv_selected.setVisibility(View.VISIBLE);
                    selected_id = makeModel.getMaker_id();

                    if (callbackFilter != null)
                        callbackFilter.setMaker(makeModel.getMaker_id(), makeModel.getTitle(), vehicle_type);
                    notifyDataSetChanged();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MakeHolder extends RecyclerView.ViewHolder {

        FrameLayout frame_parent;
        TextView tv_title, tv_selected;

        public MakeHolder(@NonNull View itemView) {
            super(itemView);
            frame_parent = itemView.findViewById(R.id.frame_parent);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_selected = itemView.findViewById(R.id.tv_selected);

        }
    }
}
