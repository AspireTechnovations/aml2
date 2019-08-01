package in.aspiretechnovations.aml.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.aspiretechnovations.aml.model.CategoryModel;

public class CategoryResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<CategoryModel> categoryModels;

    public ArrayList<CategoryModel> getCategoryModels() {
        return categoryModels;
    }

    public void setCategoryModels(ArrayList<CategoryModel> categoryModels) {
        this.categoryModels = categoryModels;
    }
}
