package com.archelo.ssl;

import javax.net.ssl.*;
import java.io.*;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static com.archelo.ssl.Server.CERTIFICATE_PASSWORD;
import static com.archelo.ssl.Server.PATH_TO_CERTIFICATE;

public class Client {
    public static void main(String args[]) {
        try {
            System.out.println("Running client");
//            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocketFactory factory = createSSLSocketFactory();
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


//            sslsocket.close();
        } catch (IOException | NoSuchAlgorithmException | CertificateException | KeyStoreException | KeyManagementException e) {
            System.out.println(e.getMessage());
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    public static SSLSocketFactory createSSLSocketFactory() throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(PATH_TO_CERTIFICATE), CERTIFICATE_PASSWORD.toCharArray());

//        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, CERTIFICATE_PASSWORD.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

        return sc.getSocketFactory();
    }

//    public static SSLSocketFactory createSSLSocketContext() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, KeyManagementException {
//        // Load CAs from an InputStream
//// (could be from a resource or ByteArrayInputStream or ...)
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//// From https://www.washington.edu/itconnect/security/ca/load-der.crt
//        Certificate ca;
//        try (InputStream caInput = new BufferedInputStream(new FileInputStream(PATH_TO_CERTIFICATE))) {
//            ca = cf.generateCertificate(caInput);
//            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//        }
//
//// Create a KeyStore containing our trusted CAs
//        String keyStoreType = KeyStore.getDefaultType();
//        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//        keyStore.load(null, null);
//        keyStore.setCertificateEntry("ca", ca);
//
//// Create a TrustManager that trusts the CAs in our KeyStore
//        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//        tmf.init(keyStore);
//
//// Create an SSLContext that uses our TrustManager
//        SSLContext context = SSLContext.getInstance("TLS");
//        context.init(null, tmf.getTrustManagers(), null);
//        return context.getSocketFactory();
//    }
}
