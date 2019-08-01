package in.aspiretechnovations.aml.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MakeModel implements Serializable {

    /*
            "_id": "1",
            "title": "Hyundai",
            "logo": "",
            "vehicle_type": "1",
            "status": "1",
            "created_at": "2019-05-20 17:04:49",
            "modified_at": "2019-05-20 17:04:49"
    */



    @SerializedName("_id")
    private String maker_id;
    private String title;
    private String logo;
    private String vehicle_type;
    private String status;

    private String created_at;
    private String modified_at;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
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
}
