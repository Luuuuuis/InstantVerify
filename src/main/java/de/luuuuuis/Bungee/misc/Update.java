/*
 * Developed by Luuuuuis on 07.04.19 20:55.
 * Last modified 07.04.19 20:07.
 * Copyright (c) 2019.
 */

package de.luuuuuis.Bungee.misc;

import de.luuuuuis.Bungee.InstantVerify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Update {

    private InstantVerify instantVerify;
    private String version;

    public Update(InstantVerify instantVerify) {
        this.instantVerify = instantVerify;
    }

    public void searchForUpdate() {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL("https://raw.githubusercontent.com/Luuuuuis/InstantVerify/master/version");
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        assert connection != null;
        InputStreamReader inputStreamReader;
        try (BufferedReader in = new BufferedReader(inputStreamReader = new InputStreamReader(connection.getInputStream()))) {
            String[] versionCode = in.readLine().split("&&");

            in.close();
            inputStreamReader.close();

            if (!(versionCode[0].equalsIgnoreCase(instantVerify.getDescription().getVersion()))) {

                /*
                 * Download from URL given in version file
                 */

                if (versionCode[1] != null && !versionCode[1].equalsIgnoreCase("null")) {

                    Thread thread = new Thread(() -> {

                        try {

                            URL downloadURL = new URL(versionCode[1]);

                            HttpURLConnection urlConnection = (HttpURLConnection) downloadURL.openConnection();
                            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

                            InputStream inputStream = urlConnection.getInputStream();

                            Files.copy(inputStream, instantVerify.getFile().toPath(), StandardCopyOption.REPLACE_EXISTING);

                            inputStream.close();

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        System.out.println("InstantVerify >> A new update is available(" + versionCode[0] + ") please restart your server soon.");
                        System.out.println("InstantVerify >> Changelog can be viewed at GitHub: https://github.com/Luuuuuis/InstantVerify/releases");

                        setVersion(instantVerify.getDescription().getVersion() + " (outdated)");

                    });
                    thread.start();

                }
            } else {
                setVersion(instantVerify.getDescription().getVersion() + " (latest)");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}