package in.aspiretechnovations.aml.response;

/**
 * Created by abc on 12/12/16.
 */

public class BaseResponse {

    private String status;
    private String message;
    private String code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
