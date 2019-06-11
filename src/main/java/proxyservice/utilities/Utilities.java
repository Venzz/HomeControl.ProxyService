package proxyservice.utilities;

import proxyservice.App;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Utilities {
    public static String substringBetween(String originalString, String open, String close) {
        int indexOfOpen = originalString.indexOf(open);
        if (indexOfOpen == -1) {
            return null;
        }
        int indexOfClose = originalString.indexOf(close, indexOfOpen + open.length());
        if (indexOfClose == -1) {
            return null;
        }
        return originalString.substring(indexOfOpen + open.length(), indexOfClose);
    }
}
