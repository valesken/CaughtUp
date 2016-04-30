package news.caughtup.caughtup.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;

public class StringRetriever {


    private static StringRetriever instance;
    private static Resources resources;
    private static boolean isLocal;

    private StringRetriever() { }

    @NonNull
    public static StringRetriever getInstance() {
        if(instance == null) {
            instance = new StringRetriever();
        }
        return instance;
    }

    public static void initializeStringRetriever(Resources resources, boolean isLocal) {
        if(instance == null) {
            StringRetriever.resources = resources;
            StringRetriever.isLocal = isLocal;
        }
    }

    public String getStringById(int stringId) {
        return resources.getString(stringId);
    }

    public String getServerConnectionString() {
        if(isLocal) {
            String connString = resources.getString(Constants.LOCAL_CONNECTION_STRING);
            return connString.replace("localhost", resources.getString(Constants.LOCAL_IP_ADDRESS));
        }
        return resources.getString(Constants.CONNECTION_STRING);
    }
}
