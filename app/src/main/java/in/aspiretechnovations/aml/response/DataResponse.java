package in.aspiretechnovations.aml.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import in.aspiretechnovations.aml.model.MakeModel;
import in.aspiretechnovations.aml.model.ModelModel;
import in.aspiretechnovations.aml.model.VariantModel;

public class DataResponse extends BaseResponse {


    @SerializedName("data")
    private DataModel dataModel;

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public  class DataModel implements Serializable{

        @SerializedName("maker")
        private ArrayList<MakeModel> makeModels;

        @SerializedName("models")
        private ArrayList<ModelModel> modelArrayList;


        @SerializedName("variants")
        private ArrayList<VariantModel> variantModels;


        public ArrayList<MakeModel> getMakeModels() {
            return makeModels;
        }

        public void setMakeModels(ArrayList<MakeModel> makeModels) {
            this.makeModels = makeModels;
        }

        public ArrayList<ModelModel> getModelArrayList() {
            return modelArrayList;
        }

        public void setModelArrayList(ArrayList<ModelModel> modelArrayList) {
            this.modelArrayList = modelArrayList;
        }

        public ArrayList<VariantModel> getVariantModels() {
            return variantModels;
        }

        public void setVariantModels(ArrayList<VariantModel> variantModels) {
            this.variantModels = variantModels;
        }
    }

}
