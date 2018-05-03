package arindatiko.example.com.travelmecustomer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ranggarizky on 1/19/2017.
 */
public class ResponseApi {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("data")
    @Expose
    private User data;

   /* @SerializedName("checkin_id")
    @Expose
    private String checkin_id;
*/

    public void setData(User data) {
        this.data = data;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    /*public void setCheckin_id(String checkin_id) {
        this.checkin_id = checkin_id;
    }

    public String getCheckin_id() {
        return checkin_id;
    }*/
}
