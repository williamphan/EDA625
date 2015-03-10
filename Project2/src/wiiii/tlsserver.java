package wiiii;

//==========================================================================
//Sample tlsserver using sslsockets
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.security.KeyStore;

public class tlsserver {
    // likely this port number is ok to use
    private static final int PORT = 8043;

    public static void main(String[] args) throws Exception {

        //set necessary truststore properties - using JKS
        //                System.setProperty("javax.net.ssl.trustStore", "truststore.jks");
        //                System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
        // set up key manager to do server authentication
        SSLContext context;
        KeyManagerFactory kmf;
        KeyStore ks;
        // First we need to load a keystore
        char[] passphrase = "yeehee".toCharArray();
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("serverKeyStore-expire.jks"), passphrase);
        // Initialize a KeyManagerFactory with the KeyStore
        kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, passphrase);
        // Create an SSLContext to run TLS and initialize it with KeyManagers from the KeyManagerFactory
        context = SSLContext.getInstance("TLS");
        KeyManager[] keyManagers = kmf.getKeyManagers();
        context.init(keyManagers, null, null);
        // First we need a SocketFactory that will create SSL server sockets.
        SSLServerSocketFactory ssf = context.getServerSocketFactory();
        // Create socket and Wait for a connection
        ServerSocket ss = ssf.createServerSocket(PORT);
        //             Socket s = ss.accept();
        // alterternative: needed to set SSL/TLS behaviour
        SSLSocket s = (SSLSocket) ss.accept();
        //        String[] enabledCiphers = new String[] {"TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA",
        //            "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256"};
        s.setEnabledCipherSuites(new String[] {"TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA"});
        //        s.setEnabledCipherSuites(new String[] {"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA"});
        //        s.setEnabledCipherSuites(new String[] {"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256"});
        //s.setEnabledCipherSuites(enabledCiphers);
        // Get the input stream. En/Decryption happens transparently.
        //        System.out.println(s.getSession().getCipherSuite());
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        //                String[] enabled = s.getEnabledCipherSuites();
        //                for(String cipher : enabled){
        //                    System.out.println(cipher);
        //                }
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
