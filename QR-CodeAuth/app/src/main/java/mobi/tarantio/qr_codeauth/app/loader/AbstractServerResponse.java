package mobi.tarantio.qr_codeauth.app.loader;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.apache.http.protocol.HTTP;

import java.io.UnsupportedEncodingException;

public class AbstractServerResponse implements Parcelable {
    @SerializedName("success")
    protected Boolean success;
    protected Boolean _success;
    @SerializedName("_error")
    protected String error;
    /**
     * @see "https://github.com/Mytootru/app_android/wiki/%D0%9A%D0%BE%D0%B4%D1%8B-%D0%BE%D1%88%D0%B8%D0%B1%D0%BE%D0%BA-API"
     */
    @SerializedName("_status")
    protected String status;
    public static final String STATUS_404 = "404";
    public static final String INNER_ERROR = "-1";
    public static final String INNER_ERROR_INTERNET_NOT_AVAILABLE = "-2";
    public static final String INNER_ERROR_HOST_NOT_AVAILABLE = "-3";
    public static final String INNER_ERROR_OFFLINE_API_MANAGER_NOT_AVAILABLE = "-4";

    public AbstractServerResponse() {
    }

    protected AbstractServerResponse(Parcel in) {
        byte successVal = in.readByte();
        success = successVal == 0x02 ? null : successVal != 0x00;
        byte _successVal = in.readByte();
        _success = _successVal == 0x02 ? null : _successVal != 0x00;
        error = in.readString();
        status = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (success == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (success ? 0x01 : 0x00));
        }
        if (_success == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (_success ? 0x01 : 0x00));
        }
        dest.writeString(error);
        dest.writeString(status);
    }

    @SuppressWarnings("unused")
    public static final Creator<AbstractServerResponse> CREATOR = new Creator<AbstractServerResponse>() {
        @Override
        public AbstractServerResponse createFromParcel(Parcel in) {
            return new AbstractServerResponse(in);
        }

        @Override
        public AbstractServerResponse[] newArray(int size) {
            return new AbstractServerResponse[size];
        }
    };

    public void setSuccess(Boolean _success) {
        this._success = _success;
    }

    public String getError() {

        String string = error != null ? error : "";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes(HTTP.UTF_8);
            // Convert from UTF-8 to Unicode
            string = new String(utf8, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "AbstractServerResponse{" +
                "success=" + success +
                ", _success=" + _success +
                ", error='" + error + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return (_success != null && _success) || (success != null && success) && (error == null
                || error.isEmpty());
    }

    public String getStatus() {
        return status;
    }
}
