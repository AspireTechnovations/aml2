package in.aspiretechnovations.aml.model;

import java.io.Serializable;

public class UserModel implements Serializable {



    private String user_id;
    private String user_type;
    private String name;
    private String email_id;
    private String mobile_no;
    private String is_mobile_verified;
    private String is_registered;
    private String status;
    private String created_at;
    private String modified_at;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getIs_mobile_verified() {
        return is_mobile_verified;
    }

    public void setIs_mobile_verified(String is_mobile_verified) {
        this.is_mobile_verified = is_mobile_verified;
    }

    public String getIs_registered() {
        return is_registered;
    }

    public void setIs_registered(String is_registered) {
        this.is_registered = is_registered;
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
