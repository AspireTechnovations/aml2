package in.aspiretechnovations.aml.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.callbacks.CallbackFilter;
import in.aspiretechnovations.aml.model.ModelModel;
import in.aspiretechnovations.aml.utils.AppPref;

public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.ModelHolder> {
    private String selected_id = "0";
    private Context context;
    private ArrayList<ModelModel> arrayList;
    private CallbackFilter callbackFilter;


    public ModelAdapter(Context context, ArrayList<ModelModel> arrayList, CallbackFilter callbackFilter) {
        this.context = context;
        this.arrayList = arrayList;
        this.callbackFilter = callbackFilter;
    }

    @NonNull
    @Override
    public ModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_make, parent, false);
        return new ModelHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ModelHolder holder, int position) {
        final ModelModel modelModel = arrayList.get(position);
        selected_id = AppPref.getKeyModelId(context);

        if (!selected_id.contentEquals("0")) {
            if (selected_id.contentEquals(modelModel.getModel_id()))
                holder.tv_selected.setVisibility(View.VISIBLE);
            else
                holder.tv_selected.setVisibility(View.GONE);
        }

        String title = modelModel.getTitle();

        if (title != null)
            holder.tv_title.setText(title);

        holder.frame_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_selected.setVisibility(View.VISIBLE);
                selected_id = modelModel.getModel_id();

                if (callbackFilter != null)
                    callbackFilter.setModel(modelModel.getModel_id(), modelModel.getTitle());
                notifyDataSetChanged();
            }
        });
    }

    /*public void addItems(ModelModel modelModel) {
        arrayList.add(modelModel);

        notifyItemInserted(arrayList.size() - 1);
        notifyDataSetChanged();

    }

    public void notifYInsert() {

        notifyItemRangeInserted(0, arrayList.size());

    }

    public void clear() {

        arrayList.clear();
        notifyDataSetChanged();

    }*/

    @Override
    public int getItemCount() {
        return arrayList.size();

    }

    public class ModelHolder extends RecyclerView.ViewHolder {

        FrameLayout frame_parent;
        TextView tv_title, tv_selected;


        public ModelHolder(@NonNull View itemView) {
            super(itemView);
            frame_parent = itemView.findViewById(R.id.frame_parent);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_selected = itemView.findViewById(R.id.tv_selected);
        }
    }
}
