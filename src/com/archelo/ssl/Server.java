package com.archelo.ssl;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Server {
    public static final String PATH_TO_CERTIFICATE = "C:\\Users\\rtl1e\\IdeaProjects\\SSLServer\\certificate\\cellular_mpos.crt";
    public static final String KEYSTORE_PASSWORD = "p0s2AD#v";
    public static final String PATH_TO_KEYSTORE = "C:\\Users\\rtl1e\\IdeaProjects\\SSLServer\\certificate\\CellularMpos.keystore";

    public static void main(String args[]) throws Exception {
        try {
            System.out.println("Running server");
//            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocketFactory factory = createSSLContextFromKeystore().getServerSocketFactory();
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

    public static SSLContext createSSLContextFromKeystore() throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(PATH_TO_KEYSTORE), KEYSTORE_PASSWORD.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(ks, KEYSTORE_PASSWORD.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

        return sc;
    }

}
