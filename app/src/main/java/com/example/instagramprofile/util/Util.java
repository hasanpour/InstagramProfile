package com.example.instagramprofile.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

public class Util {

    /**
     * Get string from connection
     *
     * @param httpsURLConnection Connection
     * @return a JSON string
     * @throws IOException
     */
    public String getString(HttpsURLConnection httpsURLConnection) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
        while ((line = bufferedReader.readLine()) != null)
            stringBuilder.append(line).append("\n");
        bufferedReader.close();

        return stringBuilder.toString();
    }
}
