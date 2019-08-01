package in.aspiretechnovations.aml.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;

import in.aspiretechnovations.aml.R;
import in.aspiretechnovations.aml.activities.CategoryDisplayActivity;
import in.aspiretechnovations.aml.adapters.MakeAdapter;
import in.aspiretechnovations.aml.adapters.ModelAdapter;
import in.aspiretechnovations.aml.adapters.VariantAdapter;
import in.aspiretechnovations.aml.callbacks.CallbackFilter;
import in.aspiretechnovations.aml.database.GeneralTables;
import in.aspiretechnovations.aml.model.MakeModel;
import in.aspiretechnovations.aml.model.ModelModel;
import in.aspiretechnovations.aml.model.VariantModel;
import in.aspiretechnovations.aml.utils.AppPref;
import in.aspiretechnovations.aml.utils.ConstantUtils;

public class SelectBikeFragment extends Fragment implements CallbackFilter {

    private View view;
    private ExpandableRelativeLayout exMake, exModel, exVariant, exFuelType, exMakeYear;
    TextView txt_model,txt_variant;
    private Button btn_search;
    private FrameLayout frameMake, frameModel, frameVariant, frameFuelType, frameMakeYear;
    private GeneralTables generalTables;
    private ArrayList<MakeModel> makeModels = new ArrayList<>();
    private ArrayList<ModelModel> modelArrayList = new ArrayList<>();
    private ArrayList<VariantModel> variantModels = new ArrayList<>();
    private RecyclerView rv_makers, rv_model, rv_variant;
    private CallbackFilter callbackFilter;
    private RadioGroup rgFuel;
    private int type = 0;
    private ModelAdapter modelAdapter;

    public static SelectBikeFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        SelectBikeFragment selectVehicleFragment = new SelectBikeFragment();
        selectVehicleFragment.setArguments(bundle);
        return selectVehicleFragment;
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_vehicle_select, container, false);
        callbackFilter = this;
        type = getArguments().getInt("type");

        rgFuel = view.findViewById(R.id.rgFuel);
        exMake = view.findViewById(R.id.exMake);
        exModel = view.findViewById(R.id.exModel);
        exVariant = view.findViewById(R.id.exVariant);
        exFuelType = view.findViewById(R.id.exFuelType);
        exMakeYear = view.findViewById(R.id.exMakeYear);
        btn_search = view.findViewById(R.id.btn_search);
        frameMake = view.findViewById(R.id.frameMake);
        frameModel = view.findViewById(R.id.frameModel);
        frameFuelType = view.findViewById(R.id.frameFuelType);
        frameVariant = view.findViewById(R.id.frameVariant);
        frameMakeYear = view.findViewById(R.id.frameMakeYear);
        generalTables = new GeneralTables(getContext());

        rv_makers = view.findViewById(R.id.rv_makers);
        rv_makers.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL,
                        false));

        txt_model = view.findViewById(R.id.txt_model);
        rv_model = view.findViewById(R.id.rv_model);
        rv_model.setLayoutManager(
                new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL,
                        false));

        txt_variant = view.findViewById(R.id.txt_variant);
        rv_variant = view.findViewById(R.id.rv_variant);
        rv_variant.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL,
                        false));

        frameMake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exMake.isExpanded()) {
                    exMake.collapse();
                } else {
                    exMake.expand();
                }
            }
        });

        frameModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exModel.isExpanded()) {
                    exModel.collapse();
                } else {
                    exModel.expand();
                }
            }
        });

        frameVariant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exVariant.isExpanded())
                    exVariant.collapse();
                else
                    exVariant.expand();
            }
        });

        frameFuelType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exFuelType.isExpanded())
                    exFuelType.collapse();
                else
                    exFuelType.expand();
            }
        });

        frameMakeYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exMakeYear.isExpanded())
                    exMakeYear.collapse();
                else
                    exMakeYear.expand();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CategoryDisplayActivity.class).putExtra("type", type));
            }
        });


        if (type == ConstantUtils.TYPE_CARS) {
            frameFuelType.setVisibility(View.VISIBLE);
            exFuelType.setVisibility(View.VISIBLE);
            frameVariant.setVisibility(View.VISIBLE);

            rgFuel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.rbPetrol:
                            AppPref.setSelectFuelType(getContext(), ConstantUtils.FUEL_TYPE_PETROL);
                            break;

                        case R.id.rbDiesel:
                            break;
                    }
                }
            });
        } else {
            frameFuelType.setVisibility(View.GONE);
            exFuelType.setVisibility(View.GONE);
            frameVariant.setVisibility(View.GONE);
        }

        makeModels = generalTables.getMakersList(type);
//        variantModels = generalTables.getVariants(type,"1");

        if (makeModels != null)
            rv_makers.setAdapter(new MakeAdapter(getContext(), makeModels, callbackFilter,"2"));

//        if (variantModels != null)
//            rv_variant.setAdapter(new VariantAdapter(getContext(), variantModels, callbackFilter));


//        modelArrayList = generalTables.getModels("2", type);
//        modelArrayList = generalTables.getModels("2", type);
//        Log.d("MakerIdBike",makeModels.get(0).getMaker_id());
        modelArrayList = generalTables.getModels(makeModels.get(0).getMaker_id(), type);
        modelAdapter = new ModelAdapter(getContext(), modelArrayList, callbackFilter);
        rv_model.setAdapter(modelAdapter);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void setMaker(String maker_id, String name, String vehicletype) {

        modelArrayList.clear();
        rv_model.setAdapter(null);

        AppPref.setSelectedMakerId(getContext(), maker_id);
        AppPref.setSelectedMakerName(getContext(), name);

        modelArrayList = generalTables.getModels(maker_id, type);

//        Log.d("ModelCount", String.valueOf(modelArrayList.size()));

        if (modelArrayList.size() != 0) {
            exModel.expand();
            rv_model.setAdapter(new ModelAdapter(getActivity(), modelArrayList, callbackFilter));
            rv_model.setVisibility(View.VISIBLE);
            txt_model.setVisibility(View.GONE);
        }
        else {
            exModel.collapse();
            txt_model.setVisibility(View.VISIBLE);
            rv_model.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setModel(String model_id, String name) {

        variantModels.clear();
        rv_variant.setAdapter(null);

        AppPref.setSelectedModelId(getContext(), model_id);
        AppPref.setSelectedModelName(getContext(), name);

        variantModels = generalTables.getVariants(type,model_id);

        if (variantModels.size() != 0) {
            exVariant.expand();
            rv_variant.setAdapter(new VariantAdapter(getActivity(), variantModels, callbackFilter));
        }
        else {
            exVariant.collapse();
        }
//        variantModels = generalTables.getVariants(type,"1");
    }

    @Override
    public void setVariant(String variant_id, String name) {
        AppPref.setSelectVariantId(getContext(), variant_id);
        AppPref.setSelectVariantName(getContext(), name);
    }
}
