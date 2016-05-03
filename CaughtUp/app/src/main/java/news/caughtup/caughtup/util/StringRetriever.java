package news.caughtup.caughtup.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;

/**
 * This class is used to retrieve strings by id from xml files instead of hardcoding string in code.
 * This allows for flexibility and localization of the application.
 */
public class StringRetriever {
    private static StringRetriever instance;
    private static Resources resources;
    private static boolean isLocal;

    private StringRetriever() { }

    /**
     * Returns a singleton of the class.
     * @return
     */
    @NonNull
    public static StringRetriever getInstance() {
        if(instance == null) {
            instance = new StringRetriever();
        }
        return instance;
    }

    /**
     * Instantiates a share Resources instance
     * @param resources
     * @param isLocal
     */
    public static void initializeStringRetriever(Resources resources, boolean isLocal) {
        if(instance == null) {
            StringRetriever.resources = resources;
            StringRetriever.isLocal = isLocal;
        }
    }

    /**
     * Wrapper to simplify string retrieval
     * @param stringId
     * @return
     */
    public String getStringById(int stringId) {
        return resources.getString(stringId);
    }

    /**
     * Returns local connection string if isLocal == true
     * @return
     */
    public String getServerConnectionString() {
        if(isLocal) {
            String connString = resources.getString(Constants.LOCAL_CONNECTION_STRING);
            return connString.replace("localhost", resources.getString(Constants.LOCAL_IP_ADDRESS));
        }
        return resources.getString(Constants.CONNECTION_STRING);
    }
}
