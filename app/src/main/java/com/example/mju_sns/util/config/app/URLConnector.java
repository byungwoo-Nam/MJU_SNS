package com.example.mju_sns.util.config.app;

import android.support.v4.util.Pools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

public class URLConnector{
    private JSONObject param;
    private String result;
    private boolean isSynchronized;
    private CountDownLatch latch;

    public String getData(JSONObject param, boolean isSynchronized){
        this.param = param;
        this.isSynchronized = isSynchronized;
        if(this.param == null || this.param.isNull("mode")){
            return null;
        }

        if(this.isSynchronized){
            this.latch = new CountDownLatch(1);
        }

        new Thread() {
            public void run() {
                connection();
            }
        }.start();

        if(this.isSynchronized){
            try{
                this.latch.await();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        return result;
    }
	
	public void connection(){
        System.out.println("connection");

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

            System.out.println(param.toString());
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
            System.out.println("connection:response" + response);
            this.result = response;
            if(this.isSynchronized) {
                this.latch.countDown();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}