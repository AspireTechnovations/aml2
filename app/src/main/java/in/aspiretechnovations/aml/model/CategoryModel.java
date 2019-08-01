package in.aspiretechnovations.aml.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    /*


            "_id": "1",
            "parent_id": "0",
            "level": "1",
            "title": "Exteriors",
            "image": "",
            "status": "1",
            "created_at": "2019-06-19 16:16:04",
            "modified_at": "2019-06-19 16:16:04",
            "vehicle_type": "3"
     */

    @SerializedName("_id")
    private String category_id;
    private String parent_id;
    private String level;
    private String title;
    private String image;
    private String status;
    private String vehicle_type;
    private String modified_at;
    private String created_at;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getModified_at() {
        return modified_at;
    }

    public void setModified_at(String modified_at) {
        this.modified_at = modified_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
