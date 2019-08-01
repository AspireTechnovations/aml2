package in.aspiretechnovations.aml.adapters;

import android.content.Context;
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
import in.aspiretechnovations.aml.model.ModelModel;
import in.aspiretechnovations.aml.model.VariantModel;
import in.aspiretechnovations.aml.utils.AppPref;

public class VariantAdapter extends RecyclerView.Adapter<VariantAdapter.MakeHolder> {
    private String selected_id = "0";
    private Context context;
    private ArrayList<VariantModel> arrayList;
    private CallbackFilter callbackFilter;


    public VariantAdapter(Context context, ArrayList<VariantModel> arrayList, CallbackFilter callbackFilter) {
        this.context = context;
        this.arrayList = arrayList;
        this.callbackFilter = callbackFilter;
    }

    @NonNull
    @Override
    public MakeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_make,parent,false);
        return new MakeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MakeHolder holder, int position) {

        final VariantModel variantModel = arrayList.get(position);
        selected_id = AppPref.getKeyVariantId(context);


        if (!selected_id.contentEquals("0"))
        {
            if (selected_id.contentEquals(variantModel.getVariant_id()))
                holder.tv_selected.setVisibility(View.VISIBLE);
            else
                holder.tv_selected.setVisibility(View.GONE);
        }


        if (variantModel != null)
        {

            String title = variantModel.getTitle();

            if (title != null)
                holder.tv_title.setText(title);


            holder.frame_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tv_selected.setVisibility(View.VISIBLE);
                    selected_id = variantModel.getModel_id();

                    if (callbackFilter !=null)
                    callbackFilter.setVariant(variantModel.getVariant_id(),variantModel.getTitle());
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class  MakeHolder extends RecyclerView.ViewHolder {

        FrameLayout frame_parent;
        TextView tv_title,tv_selected;



        public MakeHolder(@NonNull View itemView) {
            super(itemView);
            frame_parent = itemView.findViewById(R.id.frame_parent);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_selected = itemView.findViewById(R.id.tv_selected);

        }
    }
}
