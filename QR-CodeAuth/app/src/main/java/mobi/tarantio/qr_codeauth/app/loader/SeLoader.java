package mobi.tarantio.qr_codeauth.app.loader;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URI;

import mobi.tarantio.qr_codeauth.app.UrlWorker;

public class SeLoader extends GracefulLoader<AbstractServerResponse> {
    private String requestUrl;

    SeLoader(Context context, String params) {
        super(context);
        this.requestUrl = params;
    }

    @Override
    public AbstractServerResponse loadInBackground() {
        try {
            UrlWorker urlWorker = new UrlWorker();
            InputStream inputStream = urlWorker.putStringData(requestUrl,requestUrl);
        } catch (Exception e) {
            setServerResponse(e);
        }
        return null;
    }
}
