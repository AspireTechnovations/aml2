package in.aspiretechnovations.aml.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.model.ProductModel;

public class SubServiceAdapter extends RecyclerView.Adapter<SubServiceAdapter.ProductHolder> {

    private Context context;
    private ArrayList<ProductModel> arrayList;

    public SubServiceAdapter(Context context, ArrayList<ProductModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sub_category,parent,false);

        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {

        ProductModel productModel = arrayList.get(position);

        if (productModel !=  null)
        {
            String title = productModel.getTitle();

            if (title != null)
              holder.tv_service_title.setText(title);
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {

        TextView tv_service_title;


        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            tv_service_title = itemView.findViewById(R.id.tv_service_title);
        }
    }
}
