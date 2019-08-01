package in.aspiretechnovations.aml.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.activities.ProductListActivity;
import in.aspiretechnovations.aml.model.CategoryModel;
import in.aspiretechnovations.aml.utils.ConstantUtils;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private ArrayList<CategoryModel> arrayList;
    private Context context;

    public CategoryAdapter(ArrayList<CategoryModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category,parent,false);

        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {

        final CategoryModel categoryModel = arrayList.get(position);

        if (categoryModel != null)
        {

            String title= categoryModel.getTitle();
            String image= categoryModel.getImage();

            if (title != null)
                holder.tv_title.setText(title);

                holder.cv_parent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        context.startActivity(new Intent(context, ProductListActivity.class).putExtra(ConstantUtils.KEY_CATEGORY_ID,categoryModel.getCategory_id()));
                    }
                });

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        ImageView im_category;
        CardView cv_parent;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            im_category = itemView.findViewById(R.id.im_category);
            cv_parent = itemView.findViewById(R.id.cv_parent);
        }
    }
}
