package mobi.tarantio.qr_codeauth.app;

import android.text.TextUtils;

/**
 * User: artem
 * Надстрйока над стандартным логером. Отключаемый
 */
public final class Log {

    private static boolean DEFAULT_ON = true;
    //    private  Logger log = Logger.getLogger(Log.class);
    private String TAG = "mytoot";
    private boolean on = true;
    private boolean useAndroidLog = true;
    private static boolean writeToFile = false;

    public static void setDEFAULT_ON(boolean DEFAULT_ON) {
        Log.DEFAULT_ON = DEFAULT_ON;
    }

    public Log() {
        on = DEFAULT_ON;
    }



    public Log(boolean on) {
        this.on = on;
    }

    public Log(String TAG, boolean on, boolean useAndroidLog) {
        this.TAG = TAG;
        this.on = on;
        this.useAndroidLog = useAndroidLog;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void v(String msg) {
        if (on) {
//            if (useAndroidLog) {
//                log.info(msg);
//            } else {
            android.util.Log.i(getLocation(), msg);
            if (writeToFile) {
                writeLog(msg);
            }
//            }
        }
    }

    public void d(String msg) {
        if (on) {
            if (useAndroidLog) {
                android.util.Log.d(getLocation(), msg);
            }
            if (writeToFile) {
                writeLog(msg);
            }
        }
    }

    public void d(String tag, String msg) {
        if (on) {
            android.util.Log.d(tag, msg);
        }
    }

    private String getLocation() {
        final String className = Log.class.getName();
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        boolean found = false;

        for (StackTraceElement trace : traces) {
            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":" + trace.getMethodName() + ":" + trace.getLineNumber() + "]: ";
                    }
                } else if (trace.getClassName().startsWith(className)) {
                    found = true;
//                    continue;
                }
            } catch (ClassNotFoundException e) {
                e.getException();
            }
        }

        return "[]: ";
    }

    private String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }

            return getClassName(clazz.getEnclosingClass());
        }

        return "";
    }

    public void printStackTrace(Exception ex) {
//        if (on) {
//            if (writeToFile) {
//                PrintWriter pw = null;
//                try {
////                    pw = new PrintWriter(fileCache.getErrorLogFile());
//                } catch (FileNotFoundException ignored) {
//                }
//                ex.printStackTrace(pw);
//                if (pw != null) {
//                    pw.close();
//                }
//            } else {
//
//                ex.printStackTrace();
//            }
//        }
    }

    private void writeLog(String text) {
//        File myFile = fileCache.getLogFile();
//        try {
//            if (myFile.exists() || myFile.createNewFile()) {
//                PrintStream out = null;
//                try {
//                    out = new PrintStream(new BufferedOutputStream(new FileOutputStream(myFile, true)));
//                    out.println(text);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (out != null) {
//                        out.close();
//                    }
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
}
