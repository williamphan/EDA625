package wiiii;

//==========================================================================
//Sample tlsserver using sslsockets
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.KeyStore;
import java.security.SecureRandom;

public class tlsserver {
    // likely this port number is ok to use
    private static final int PORT = 8043;

    public static void main(String[] args) throws Exception {

        // TrustStore
        char[] passphrase_ts = "TrustPass".toCharArray();
        KeyStore ts = KeyStore.getInstance("JKS");
        ts.load(new FileInputStream("truststore.jks"), passphrase_ts);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ts);

        SSLContext context;
        KeyManagerFactory kmf;
        KeyStore ks;
        // First we need to load a keystore
        char[] passphrase = "123456".toCharArray();
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("serverKeyStore.jks"), passphrase);
        // Initialize a KeyManagerFactory with the KeyStore
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        // Create an SSLContext to run TLS and initialize it with KeyManagers from the KeyManagerFactory
        context = SSLContext.getInstance("TLS");
        KeyManager[] keyManagers = kmf.getKeyManagers();
        TrustManager[] trustManagers = tmf.getTrustManagers();
        context.init(keyManagers, trustManagers, new SecureRandom());
        // First we need a SocketFactory that will create SSL server sockets.
        SSLServerSocketFactory ssf = context.getServerSocketFactory();
        // Create socket and Wait for a connection
        ServerSocket ss = ssf.createServerSocket(PORT);
        // alterternative: needed to set SSL/TLS behaviour
        SSLSocket s = (SSLSocket) ss.accept();
        s.setNeedClientAuth(true);
        // Get the input stream. En/Decryption happens transparently.
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        System.out.print("\nConnection established.\n\n");

        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        // Read through the input from the client and display it to the screen.
        String line = null;
        while (((line = in.readLine())) != null) {
            System.out.println(line);
            out.println(line + ", ");
        }
        out.close();
        in.close();
        s.close();
    }
}
