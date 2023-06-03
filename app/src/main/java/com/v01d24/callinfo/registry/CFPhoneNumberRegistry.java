package com.v01d24.callinfo.registry;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.v01d24.callinfo.TLSSocketFactory;
import com.v01d24.callinfo.PhoneNumberScore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

public class CFPhoneNumberRegistry implements PhoneNumberRegistry {
    public @NonNull Uri getWebPageUri(@NonNull String phoneNumber) {
        return Uri.parse("https://callfilter.app/" + phoneNumber.substring(1));
    }

    public @NonNull PhoneNumberInfo lookup(@NonNull String phoneNumber) throws RegistryException {
        try {
            Uri webPageUri = getWebPageUri(phoneNumber);
            URL url = new URL(webPageUri.toString());
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(new TLSSocketFactory());
            InputStream stream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String infoHtml = readInfoHtml(reader);
            reader.close();
            CFPhoneNumberInfoParser parser = new CFPhoneNumberInfoParser(infoHtml);
            PhoneNumberScore score = parser.parseScore();
            String summary = parser.parseSummary();
            if (score == null || summary == null) {
                throw new RegistryException("Score or summary data not found");
            }
            return new PhoneNumberInfo(score, summary, webPageUri);
        } catch (MalformedURLException e) {
            throw new RegistryException("Malformed lookup URL", e);
        } catch (IOException e) {
            throw new RegistryException("Unable to contact server", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RegistryException("TLS is not supported", e);
        } catch (KeyManagementException e) {
            throw new RegistryException("TLS is not supported", e);
        }
    }

    private @NonNull String readInfoHtml(@NonNull BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        String line = findFirstInfoLine(reader);
        while (line != null && !line.contains("class=\"aheader\"")) {
            builder.append(line);
            line = reader.readLine();
        }
        return builder.toString();
    }

    private @Nullable String findFirstInfoLine(@NonNull BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            if (line.contains("class=\"mainInfoHeader\"")) {
                return line;
            }
        }
    }
}
