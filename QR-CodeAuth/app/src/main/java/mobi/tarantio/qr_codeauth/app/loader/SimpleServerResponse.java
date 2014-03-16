package mobi.tarantio.qr_codeauth.app.loader;

public class SimpleServerResponse extends AbstractServerResponse {
    public SimpleServerResponse(Boolean _success, String error) {
        this._success = _success;
        this.error = error;
    }

    public SimpleServerResponse(Boolean _success, String error, String status) {
        this._success = _success;
        this.error = error;
        this.status = status;
    }
}
