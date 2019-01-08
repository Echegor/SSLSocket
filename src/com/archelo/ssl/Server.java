package com.archelo.ssl;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class Server {


    public static void main(String args[]) throws Exception {
        try {
            System.getProperties();
            String keystorePath = System.getProperty("com.wakefern.keystore.path");
            String keystorePassword = System.getProperty("com.wakefern.keystore.password");
            System.out.println("Running server");
//            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocketFactory factory = SSLUtils.createSSLContextFromKeystore(new FileInputStream(keystorePath),keystorePassword).getServerSocketFactory();
            SSLServerSocket sslserversocket = (SSLServerSocket) factory.createServerSocket(1234);
            SSLSocket sslsocket = (SSLSocket) sslserversocket.accept();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
            while (true) {
                String input = bufferedReader.readLine();
                System.out.println("Received = " + input);
                bufferedWriter.write(input + "\n");
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done");
    }



}
