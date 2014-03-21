package mobi.tarantio.qr_codeauth.app.loader;

import android.content.Context;

import org.apache.http.client.utils.URLEncodedUtils;

import java.io.InputStream;
import java.net.URL;

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
            URL url = new URL(requestUrl);
            InputStream inputStream = urlWorker.putStringData(requestUrl,
                    URLEncodedUtils.parse(url.toURI(), "UTF-8"));
            serverResponse = new SimpleServerResponse(true, UrlWorker.convertStreamToString(inputStream));
        } catch (Exception e) {
            setServerResponse(e);
        }
        return serverResponse;
    }
    /* Runs on the UI thread */
    @Override
    public void deliverResult(AbstractServerResponse serverResponse) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            return;
        }
        this.serverResponse = serverResponse;
        if (isStarted()) {
            super.deliverResult(serverResponse);
        }

    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p/>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (serverResponse != null) {
            deliverResult(serverResponse);
        }
        if (takeContentChanged() || serverResponse == null) {
            forceLoad();
        }
    }


    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        serverResponse = null;
    }
}
