package com.example.mju_sns.util.config.app;

import android.support.v4.util.Pools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

    public String starter(JSONObject param, boolean isSynchronized, final boolean isFile){
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
                connection(isFile);
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
	
	public void connection(boolean isFile){
        System.out.println("connection");

        try {
            CodeConfig codeConfig = new CodeConfig();
            JSONObject responseJSON = null;

            // URL 객체 생성
            URL url = new URL(codeConfig.SERVER_URL);

            String response = "";

            // URLConnection 생성
            HttpURLConnection httpUrlConnection = (HttpURLConnection)url.openConnection();
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setRequestMethod("POST");
            int HttpResult;
            if(isFile){
                System.out.println("FILE!!!!!!!!!!!!!!!!!!!!!");
                String path = param.get("path").toString();
                File sourceFile = new File(path);
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****");
                httpUrlConnection.setRequestProperty("uploaded_file", path);
                DataOutputStream dataOutputStream = new DataOutputStream(httpUrlConnection.getOutputStream());
                dataOutputStream.writeBytes("\r\n--" + "*****" + "\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"no\"\r\n\r\n" + 1);
                dataOutputStream.writeBytes("\r\n--" + "*****" + "\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"table_name\"\r\n\r\n" + "test");
                dataOutputStream.writeBytes("\r\n--" + "*****" + "\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"image.jpg\"\r\n");
                dataOutputStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0)
                {
                    // Upload file part(s)
                    DataOutputStream dataWrite = new DataOutputStream(httpUrlConnection.getOutputStream());
                    dataWrite.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                fileInputStream.close();

                dataOutputStream.writeBytes("\r\n--" + "*****" + "--\r\n");
                dataOutputStream.flush();

                // Responses from the server (code and message)
                HttpResult = httpUrlConnection.getResponseCode();

                //close the streams //
                dataOutputStream.flush();
                dataOutputStream.close();
            }else {
                httpUrlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                httpUrlConnection.setRequestProperty("Accept", "*/*");
                httpUrlConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpUrlConnection.getOutputStream());
                outputStreamWriter.write(param.toString());
                outputStreamWriter.flush();
                outputStreamWriter.close();
            }

            HttpResult = httpUrlConnection.getResponseCode();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}