package in.aspiretechnovations.aml.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelModel implements Serializable {

    /*
    "_id": "1",
                "maker_id": "1",
                "title": "i20 Active",
                "status": "1",
                "created_at": "2019-05-20 17:06:35",
                "modified_at": "2019-05-20 17:06:35"
     */

    @SerializedName("_id")
    private String model_id;
    private String maker_id;
    private String title;
    private String vehicle_type;

    private String status;

    private String created_at;
    private String modified_at;


    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getMaker_id() {
        return maker_id;
    }

    public void setMaker_id(String maker_id) {
        this.maker_id = maker_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }
}
