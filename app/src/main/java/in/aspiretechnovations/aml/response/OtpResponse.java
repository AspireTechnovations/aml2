package in.aspiretechnovations.aml.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtpResponse extends BaseResponse {

    @SerializedName("data")
    private OtpModel otpModel;

    public OtpModel getOtpModel() {
        return otpModel;
    }

    public void setOtpModel(OtpModel otpModel) {
        this.otpModel = otpModel;
    }

    public class OtpModel implements Serializable{

        private String otp;

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }
}
