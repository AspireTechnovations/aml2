package in.aspiretechnovations.aml.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.aspiretechnovations.aml.model.ProductModel;

public class ProductResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<ProductModel> productModels;

    public ArrayList<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(ArrayList<ProductModel> productModels) {
        this.productModels = productModels;
    }
}
