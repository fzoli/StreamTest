import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;

public class HttpExecutor {

    private final String URL, ENCODE;
    private final HttpClient HTTP_CLIENT;
    
    public HttpExecutor(String url, String encode) {        
        URL = url;
        ENCODE = encode;
        HTTP_CLIENT = getThreadSafeClient();
        HTTP_CLIENT.getParams().setParameter("http.connection.timeout", 5000);
    }
    
    public String getResponse(Map<String, String> map) {
        InputStream stream = execute(map);
        DataInputStream dis = new DataInputStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte data;
        try {
            while ((data = dis.readByte()) != -1) {
                baos.write(data);
            }
        } catch(Exception ex) {}
        byte[] response = baos.toByteArray();
        try {
            return new String(response, ENCODE);
        }
        catch(UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    private DefaultHttpClient getThreadSafeClient() {
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
        cm.setDefaultMaxPerRoute(15);
        cm.setMaxTotal(15);
        return new DefaultHttpClient(cm);
    }
    
    private UrlEncodedFormEntity createFormEntity(Map<String, String> map) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Set<String> keys = map.keySet();
        for (String k : keys) {
            nameValuePairs.add(new BasicNameValuePair(k, map.get(k)));
        }
        try {
            return new UrlEncodedFormEntity(nameValuePairs, ENCODE);
        }
        catch (UnsupportedEncodingException ex) {
            //ex.printStackTrace();
            return null;
        }
    }
    
    private InputStream getResponseStream(HttpResponse httpResponse) {
        HttpEntity httpEntity = httpResponse.getEntity();
        try {
            return httpEntity.getContent();
        }
        catch(IOException ex) {
            //ex.printStackTrace();
            return null;
        }
    }
    
    private InputStream execute(Map<String, String> map) {
        UrlEncodedFormEntity entity = createFormEntity(map);
        HttpPost post = new HttpPost(URL);
        post.setEntity(entity);
        try {
            HttpResponse response = HTTP_CLIENT.execute(post);
            return getResponseStream(response);
        }
        catch(IOException ex) {
            //ex.printStackTrace();
            return null;
        }
    }
    
}