package server;

import com.google.gson.Gson;

/**
 * Created by wufeiyang on 2017/8/3.
 */
public class JsonUtil {

    private static volatile Gson sGson;

    private JsonUtil() {

    }

    public static Gson getGson () {
        if (null == sGson) {
            synchronized(JsonUtil.class) {
                if (null == sGson) {
                    sGson = new Gson();
                }
            }
        }

        return sGson;
    }
}
