package in.aspiretechnovations.aml.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CartModel implements Serializable {

    private String sub_total;
    private String total;

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    private ArrayList<ProductModel> productModels;


    public ArrayList<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(ArrayList<ProductModel> productModels) {
        this.productModels = productModels;
    }
}
