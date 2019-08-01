package in.aspiretechnovations.aml.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.adapters.SubServiceAdapter;
import in.aspiretechnovations.aml.model.ProductModel;

public class ServiceSelectionActivity extends AppCompatActivity {

    private RecyclerView rv_sub_services;
    private Context context;
    private String[] components = {"Engine Valve","Engine Crankshaft","Piston set","Piston Rings","Valve Springs"};
    private ArrayList<ProductModel> arrayList;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selection);
        rv_sub_services = findViewById(R.id.rv_sub_services);
        arrayList = new ArrayList<>();
        context = this;
        rv_sub_services.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        for(int i = 0; i < components.length; i++)
        {
            ProductModel productModel  = new ProductModel();
            productModel.setTitle(components[i]);
            arrayList.add(productModel);
        }
        rv_sub_services.setAdapter(new SubServiceAdapter(context,arrayList));


    }

}
