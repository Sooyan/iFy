/*
 * OS-code
 * Welcome to send e-mail ymbtandy[@][dot]sina[dot]com
 */
package com.soo.ify.http;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

/**
 * ClassName: SooHttpClient <br/>
 * Function: TODO ADD FUNCTION. <br/>
 */
public class SooHttpClient implements HttpClient {

    private static final int DEFAULT_HTTP_PORT = 80;
    private static final int DEFAULT_HTTPS_PORT = 443;

    private static final int DEFAULT_TIMEOUT = 20 * 1000;
    private static final int DEFAULT_MAX_CONN_PER_ROUT = 128;
    private static final int DEFAULT_MAX_CONN_TOTAL = 512;
    private static final boolean DEFAULT_TCP_NO_DELAY = true;
    private static final int DEFAULT_BUFFER_SIZE = 4096;

    public static final int DEFAULT_MAX_RETRIES = 5;
    public static final int DEFAULT_RETRY_SLEEP_TIME_MILLIS = 1500;

    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    private DefaultHttpClient client;
    private HttpContext httpContext;

    public SooHttpClient() {
        HttpParams params = getHttpParmas();
        ClientConnectionManager manager = getClientConnectionManager(params);
        httpContext = new BasicHttpContext();

        client = new DefaultHttpClient(manager, getHttpParmas());

        client.addRequestInterceptor(new HttpRequestInterceptor() {
            @Override
            public void process(HttpRequest request, HttpContext context) {
                if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                    request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
                }
            }
        });

        client.addResponseInterceptor(new HttpResponseInterceptor() {
            @Override
            public void process(HttpResponse response, HttpContext context) {
                final HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return;
                }
                final Header encoding = entity.getContentEncoding();
                if (encoding != null) {
                    for (HeaderElement element : encoding.getElements()) {
                        if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
                            response.setEntity(new GZIPEntityWrapper(entity));
                            break;
                        }
                    }
                }
            }
        });

        client.addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(final HttpRequest request,
                    final HttpContext context) throws HttpException,
                    IOException {
                AuthState authState = (AuthState) context
                        .getAttribute(ClientContext.TARGET_AUTH_STATE);
                CredentialsProvider credsProvider = (CredentialsProvider) context
                        .getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context
                        .getAttribute(ExecutionContext.HTTP_TARGET_HOST);

                if (authState.getAuthScheme() == null) {
                    AuthScope authScope = new AuthScope(targetHost
                            .getHostName(), targetHost.getPort());
                    Credentials creds = credsProvider.getCredentials(authScope);
                    if (creds != null) {
                        authState.setAuthScheme(new BasicScheme());
                        authState.setCredentials(creds);
                    }
                }
            }
        }, 0);

        client.setHttpRequestRetryHandler(new RetryHandler(DEFAULT_MAX_RETRIES,
                DEFAULT_RETRY_SLEEP_TIME_MILLIS));
    }

    private ClientConnectionManager getClientConnectionManager(HttpParams params) {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        SSLSocketFactory socketFactory = MySSLSocketFactory
                .getFixedSocketFactory();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), DEFAULT_HTTP_PORT));
        schemeRegistry.register(new Scheme("https", socketFactory,
                DEFAULT_HTTPS_PORT));
        return new ThreadSafeClientConnManager(params, schemeRegistry);
    }

    private BasicHttpParams getHttpParmas() {
        BasicHttpParams params = new BasicHttpParams();
        ConnManagerParams.setTimeout(params, DEFAULT_TIMEOUT);
        ConnManagerParams.setMaxConnectionsPerRoute(params,
                new ConnPerRouteBean(DEFAULT_MAX_CONN_PER_ROUT));
        ConnManagerParams
                .setMaxTotalConnections(params, DEFAULT_MAX_CONN_TOTAL);
        HttpConnectionParams.setTcpNoDelay(params, DEFAULT_TCP_NO_DELAY);
        HttpConnectionParams.setConnectionTimeout(params, DEFAULT_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, DEFAULT_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, DEFAULT_BUFFER_SIZE);
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUserAgent(params,
                String.format("Soo %s ( ymbtandy@sina.com )", "1.0"));
        return params;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    @Override
    public HttpResponse execute(HttpUriRequest request) throws IOException,
            ClientProtocolException {
        return client.execute(request, httpContext);
    }

    @Override
    public HttpResponse execute(HttpUriRequest request, HttpContext context)
            throws IOException, ClientProtocolException {
        return client.execute(request, context);
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request)
            throws IOException, ClientProtocolException {
        return client.execute(target, request, httpContext);
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request,
            HttpContext context) throws IOException, ClientProtocolException {
        return client.execute(target, request, context);
    }

    @Override
    public <T> T execute(HttpUriRequest request,
            ResponseHandler<? extends T> responseHander) throws IOException,
            ClientProtocolException {
        return client.execute(request, responseHander, httpContext);
    }

    @Override
    public <T> T execute(HttpUriRequest request,
            ResponseHandler<? extends T> responseHander, HttpContext context)
            throws IOException, ClientProtocolException {
        return client.execute(request, responseHander, context);
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request,
            ResponseHandler<? extends T> responseHander) throws IOException,
            ClientProtocolException {
        return client.execute(target, request, responseHander, httpContext);
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request,
            ResponseHandler<? extends T> responseHander, HttpContext context)
            throws IOException, ClientProtocolException {
        return client.execute(target, request, responseHander, context);
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        return client.getConnectionManager();
    }

    @Override
    public HttpParams getParams() {
        return client.getParams();
    }

}
