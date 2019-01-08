package com.archelo.ssl;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SSLUtils {

    /**
    * Should be used on server to enable ssl
    * */
    public static SSLContext createSSLContextFromKeystore(InputStream keystore, String keystorePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(keystore, keystorePassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(ks, keystorePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

        return sc;
    }

    /**
    * Should be used on client to enable ssl
    * */
    public static SSLContext createSSLSocketFactory(InputStream certificate) throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException, UnrecoverableKeyException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate ca;
        try (InputStream caInput = new BufferedInputStream(certificate)) {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        }

// Create a KeyStore containing our trusted CAs
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null,null );
        keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        return context;
    }
}
