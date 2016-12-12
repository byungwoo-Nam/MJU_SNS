package com.example.mju_sns.util.config.app;

import android.provider.Settings;
import android.support.v4.util.Pools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
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

    public void test(boolean isFile, final String path) {
        new Thread() {
            public void run() {
                String fileName = path;

                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(path);

                if (!sourceFile.isFile()) {
                }
                else
                {
                    try {
                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(sourceFile);
                        CodeConfig codeConfig = new CodeConfig();
                        URL url = new URL(codeConfig.SERVER_URL);
                        int serverResponseCode = 0;

                        // Open a HTTP  connection to  the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("uploaded_file", fileName);

                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file[]\";filename=\""
                                + fileName + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of  maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        }

                        // send multipart form data necesssary after file data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        // Responses from the server (code and message)
                        serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();

                        if(serverResponseCode == 200){
                        }

                        //close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (MalformedURLException ex) {
                    } catch (Exception e) {
                    }

                } // End else block
            }
        }.start();
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