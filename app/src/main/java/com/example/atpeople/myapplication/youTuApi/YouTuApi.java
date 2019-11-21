package com.example.atpeople.myapplication.youTuApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Create by peng on 2019/8/1
 * 从2019年8月1日起，此体验接口增加调用限制，每日调用次数限制：500 次/天，并发数限制：1个/秒
 */
public class YouTuApi {
    private static final String TAG = "YouTuApi";

    public final static String API_URL = "https://api.youtu.qq.com/youtu/ocrapi/";
    private static int EXPIRED_SECONDS = 2592000;
    private String m_appid;
    private String m_secret_id;
    private String m_secret_key;

    private OnRequestListener mListener;



    public YouTuApi(String m_appid, String m_secret_id, String m_secret_key) {
        this.m_appid = m_appid;
        this.m_secret_id = m_secret_id;
        this.m_secret_key = m_secret_key;
    }
    /**
     * @Author Peng
     * @Date 2019/8/1 17:30
     * @Describe 接口
     */
    public interface OnRequestListener {
        void onSuccess(int statusCode, String responseBody);

        void onFailure(int statusCode);
    }

    public void setRequestListener(OnRequestListener listener) {
        mListener = listener;
    }


    /**
     * @Author Peng
     * @Date 2019/8/1 17:19
     * @Describe 封装请求参数
     * @param ~string  base64字符串
     */
    public void nameCardOcr(String fileData) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("image", fileData);
        // data.put("app_id", m_appid);
        sendHttpsRequest(data, "bcocr");
    }


    /**
     * @Author Peng
     * @Date 2019/8/1 17:22
     * @Describe 发送请求
     */
    private JSONObject sendHttpsRequest(JSONObject postData, String mothod) {
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom());

            StringBuffer mySign = new StringBuffer("");
            YoutuSign.appSign(m_appid, m_secret_id, m_secret_key,
                    System.currentTimeMillis() / 1000 + EXPIRED_SECONDS,
                    "", mySign);

            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
            System.setProperty("sun.net.client.defaultReadTimeout", "30000");

            URL url = new URL(API_URL + mothod);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
            // set header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("user-agent", "youtu-android-sdk");
            connection.setRequestProperty("Authorization", mySign.toString());

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "text/json");
            connection.connect();

            // POST请求
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());

            postData.put("app_id", m_appid);
            out.write(postData.toString().getBytes("utf-8"));
            // 刷新、关闭
            out.flush();
            out.close();

            final int responseCode = connection.getResponseCode();
            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String lines;
            StringBuffer resposeBuffer = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes(), "utf-8");
                resposeBuffer.append(lines);
            }
            reader.close();
            // 断开连接
            connection.disconnect();
            // 响应之后，通过接口进行回调
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                if (mListener != null) {
                    mListener.onSuccess(responseCode, resposeBuffer.toString());
                }

            } else {
                if (mListener != null) {
                    mListener.onFailure(responseCode);
                }
            }
            //JSONObject respose = new JSONObject(resposeBuffer.toString());
            //return respose;
        } catch (FileNotFoundException e) {
            if (mListener != null) {
                // 没配置app_key
                mListener.onFailure(HttpsURLConnection.HTTP_NOT_FOUND);
            }
        } catch (Exception e) {
            if (mListener != null) {
                // 断网
                mListener.onFailure(HttpsURLConnection.HTTP_BAD_GATEWAY);
            }
        }
        return null;
    }


    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
