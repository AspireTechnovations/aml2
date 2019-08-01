package in.aspiretechnovations.aml.response;

import com.google.gson.annotations.SerializedName;

import in.aspiretechnovations.aml.model.UserModel;

public class UserResponse extends BaseResponse {

    @SerializedName("data")
    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
