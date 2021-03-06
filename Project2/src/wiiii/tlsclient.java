package wiiii;
//==========================================================================
//Sample tlsclient using sslsockets

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.SecureRandom;

public class tlsclient {
    private static final String HOST = "localhost";
    private static final int PORT = 8043;

    public static void main(String[] args) throws Exception {
        // TrustStore
        char[] passphrase_ts = "TrustPass".toCharArray();
        KeyStore ts = KeyStore.getInstance("JKS");
        ts.load(new FileInputStream("truststore.jks"), passphrase_ts);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);

        KeyManagerFactory kmf;
        KeyStore ks;
        // First we need to load a keystore
        char[] passphrase = "123456".toCharArray();
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("clientKeyStore.jks"), passphrase);
        // Initialize a KeyManagerFactory with the KeyStore
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        KeyManager[] keyManagers = kmf.getKeyManagers();

        context.init(keyManagers, trustManagers, new SecureRandom());
        SSLSocketFactory sf = context.getSocketFactory();
        Socket s = sf.createSocket(HOST, PORT);

        OutputStream toServer = s.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
//        toServer.write("\nConnection established.\n\n".getBytes());
        System.out.print("\nConnection established.\n\n");

        String line = "";
        int inCharacter = 0;
        inCharacter = System.in.read();
        while (inCharacter != '~') {
            toServer.write(inCharacter);
            if (inCharacter == '\n') {
                toServer.flush();
                line += in.readLine();
                //                System.out.println(line);
            }
            inCharacter = System.in.read();
        }
        if (line.length() > 2) {
            System.out.println("Echo: " + line.substring(0, line.length() - 2));
        }
        System.out.println("Closing down...");
        toServer.close();
        in.close();
        s.close();
    }
}
