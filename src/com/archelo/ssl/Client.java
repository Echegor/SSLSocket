package com.archelo.ssl;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


public class Client {
    public static final String PATH_TO_CERTIFICATE = "C:\\Users\\rtl1e\\IdeaProjects\\SSLServer\\certificate\\cellular_mpos.crt";
    public static void main(String args[]) {
        try {
            System.out.println("Running client");
//            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocketFactory factory = SSLUtils.createSSLSocketFactory(new FileInputStream(PATH_TO_CERTIFICATE)).getSocketFactory();
            SSLSocket sslsocket = (SSLSocket) factory.createSocket("localhost", 1234);
            sslsocket.startHandshake();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sslsocket.getOutputStream()));

            while(true){
                String str = "Hello World\n";
                bufferedWriter.write(str);
                bufferedWriter.flush();
                String responseStr = bufferedReader.readLine();
                System.out.println("Received: " + responseStr);
            }

        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException | KeyManagementException e) {
            System.out.println(e.getMessage());
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

}
