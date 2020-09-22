package cn.itcast.jingdong.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 *
 */
@Component
public class HttpUtils {

    private PoolingHttpClientConnectionManager cm;

    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();
        this.cm.setMaxTotal(100);
        this.cm.setDefaultMaxPerRoute(10);
    }

    /**
     * 下载html页面数据
     *
     * @param url 请求路径
     * @return 页面数据的字符串表示形式
     */
    public String doGetHtml(String url) {
        //从HttpClient连接池获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //创建请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.Config());
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18362");
        //发送请求，接收响应,并处理
        CloseableHttpResponse response = null;
        String content = "";//html数据的字符串表示形式
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.getEntity() != null) {
                    content = EntityUtils.toString(response.getEntity(),"utf8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 下载图片
     *
     * @param url 图片路径
     * @return 图片的名称
     */
    public String doGetImage(String url) {
        //从HttpClient连接池获取HttpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        //创建请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.Config());
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36 Edge/18.18362");
        //发送请求，接收响应,并处理
        CloseableHttpResponse response = null;
        String imageName = "";
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                if (response.getEntity() != null) {
                    //将图片写进D:\IdeaProjects\high_level\12webcrawler\images文件夹
                    imageName = UUID.randomUUID() + url.substring(url.lastIndexOf("."));
                    response.getEntity().writeTo(new FileOutputStream(new File("D:\\IdeaProjects\\high_level\\12webcrawler\\images\\" + imageName)));
                    //返回图片名
                    return imageName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imageName;
    }

    /**
     * 构造请求参数
     *
     * @return
     */
    private RequestConfig Config() {
        return RequestConfig.custom().setConnectTimeout(10000)//建立连接最大时长
                .setConnectionRequestTimeout(10000)//获取连接最大时长
                .setSocketTimeout(10 * 1000)//传输最大时长
                .build();
    }

}
