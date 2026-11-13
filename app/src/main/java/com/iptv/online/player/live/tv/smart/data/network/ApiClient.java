package com.iptv.online.player.live.tv.smart.data.network;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;

/* loaded from: classes5.dex */
public class ApiClient {
    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36";
    private static OkHttpClient okHttpClient;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$getUnsafeOkHttpClient$0(String str, SSLSession sSLSession) {
        return true;
    }

    public static synchronized OkHttpClient getClient() {
        OkHttpClient okHttpClient2;
        synchronized (ApiClient.class) {
            if (okHttpClient == null) {
                okHttpClient = getUnsafeOkHttpClient();
            }
            okHttpClient2 = okHttpClient;
        }
        return okHttpClient2;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            TrustManager[] trustManagerArr = {new X509TrustManager() { // from class: com.iptv.online.player.live.tv.smart.data.network.ApiClient.1
                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
                }

                @Override // javax.net.ssl.X509TrustManager
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            SSLContext sSLContext = SSLContext.getInstance("SSL");
            sSLContext.init(null, trustManagerArr, new SecureRandom());
            SSLSocketFactory socketFactory = sSLContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(socketFactory, (X509TrustManager) trustManagerArr[0]);
            builder.hostnameVerifier(new HostnameVerifier() { // from class: com.iptv.online.player.live.tv.smart.data.network.ApiClient$$ExternalSyntheticLambda0
                @Override // javax.net.ssl.HostnameVerifier
                public final boolean verify(String str, SSLSession sSLSession) {
                    return ApiClient.lambda$getUnsafeOkHttpClient$0(str, sSLSession);
                }
            });
            builder.connectTimeout(30L, TimeUnit.SECONDS).readTimeout(45L, TimeUnit.SECONDS).writeTimeout(30L, TimeUnit.SECONDS).followRedirects(true).followSslRedirects(true);
            return builder.build();
        } catch (Exception unused) {
            return new OkHttpClient.Builder().connectTimeout(30L, TimeUnit.SECONDS).readTimeout(45L, TimeUnit.SECONDS).build();
        }
    }
}
