package com.example.mju_sns.util.config.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

public class URLConnector{
    private JSONObject param;
    private String result;

    public String getData(JSONObject param){
        this.param = param;
        if(this.param == null || this.param.isNull("mode")){
            return null;
        }

        Thread networkThread = new Thread() {
            public void run() {
                connection();
            }
        };

        networkThread.start();

        try{
            networkThread.join();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
	
	public void connection(){
        try {
            CodeConfig codeConfig = new CodeConfig();
            JSONObject responseJSON = null;

            // URL 객체 생성
            URL url = new URL(codeConfig.server_url);

            // URLConnection 생성
            HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            httpUrlConnection.setRequestProperty("Accept", "*/*");
            httpUrlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            httpUrlConnection.setRequestMethod("POST");

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpUrlConnection.getOutputStream());
            outputStreamWriter.write(param.toString());
            outputStreamWriter.flush();

            String response = "";
            int HttpResult = httpUrlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                br.close();
            }

            result = response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}