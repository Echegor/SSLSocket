package com.archelo.ssl;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class Server {
    public static final String PATH_TO_CERTIFICATE = "C:\\Users\\rtl1e\\IdeaProjects\\SSLServer\\certificate\\CellularMPOS";
    public static final String CERTIFICATE_PASSWORD = "p0s2AD#v";

    public static void main(String args[]) throws Exception {
        try {
            System.out.println("Running server");
//            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocketFactory factory = createSSLServerSocketFactory();
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

    public static SSLServerSocketFactory createSSLServerSocketFactory() throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, UnrecoverableKeyException {
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

        return sc.getServerSocketFactory();
    }

    //android example
//    public static SSLServerSocketFactory createSSLServerContext() throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException, KeyManagementException {
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
//        return context.getServerSocketFactory();
//    }
}
