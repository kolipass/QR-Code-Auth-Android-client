package mobi.tarantio.qr_codeauth.app.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import mobi.tarantio.qr_codeauth.app.Log;

/**
 * Лоадер, который знает, завершился он с удачей или нет. Хранит в себеответ от сервера или локальную ошибку при неудаче
 * Created by kolipass on 11.02.14.
 */
public abstract class GracefulLoader<N> extends AsyncTaskLoader<N> {
    protected Boolean success;
    protected AbstractServerResponse serverResponse;
    protected boolean updateData = true;
    protected Context context;

    protected Log log = new Log();

    public GracefulLoader(Context context) {
        super(context);
        this.context = context;
        init();
    }

    protected void init() {
        success = null;
        serverResponse = null;
    }

    protected void setServerResponse(Exception e) {
        setServerResponse(e.getLocalizedMessage());
    }

    protected void setServerResponse(String message) {
        this.serverResponse = new SimpleServerResponse(false, message);
    }

    public void setUpdateData(boolean updateData) {
        this.updateData = updateData;
    }

    /**
     * Узнать, успешно ли закончился лоад
     *
     * @return null если не закончился, true если успех false если не успех
     */

    public Boolean getSuccess() {
        return success;
    }

    public AbstractServerResponse getServerResponse() {
        return serverResponse;
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }
}
