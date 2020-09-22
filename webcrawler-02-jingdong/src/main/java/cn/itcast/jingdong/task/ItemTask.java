package cn.itcast.jingdong.task;

import cn.itcast.jingdong.pojo.Item;
import cn.itcast.jingdong.service.ItemService;
import cn.itcast.jingdong.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class ItemTask {

    @Autowired
    private HttpUtils httpUtils;

    @Autowired
    private ItemService itemService;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 抓取html页面数据
     */
    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void itemTask() {
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&wq=%E6%89%8B%E6%9C%BA&s=1&click=0&page=";
        for (int i = 1; i < 2; i += 2) {
            url = url + i;
            String html = httpUtils.doGetHtml(url);
            this.parse(html);
        }
        System.out.println("抓取、分析、存储完成！");
    }

    /**
     * 分析并存储
     *
     * @param html
     */
    private void parse(String html) {
        //解析html
        Document document = Jsoup.parse(html);
        //获取所有spu元素
        Elements spuElements = document.select("div#J_goodsList>ul>li");
        for (Element spuElement : spuElements) {
            //获取spu
            String spuStr = spuElement.attr("data-spu");
            long spu = Long.parseLong("".equals(spuStr) ? "0" : spuStr);
            //获取sku元素
            Elements skuElements = spuElement.select("li.ps-item");
            for (Element skuElement : skuElements) {
                //获取sku
                long sku = Long.parseLong(skuElement.select("[data-sku]").attr("data-sku"));
                //获取详情页url https://item.jd.com/100012223336.html
                String itemUrl = "https://item.jd.com/" + sku + ".html";
                //获取手机图片url  https://img10.360buyimg.com/n7/jfs/t1/114603/36/13412/44425/5f1a82f9E1621ad46/77ee2de61806b5f0.jpg
                String imageUrl = "https:" + skuElement.select("img[data-sku]").attr("data-lazy-img");
                imageUrl = imageUrl.replace("/n7/", "/n1/");
                //获取图片在本地存储的名称
                String pic = this.httpUtils.doGetImage(imageUrl);


                String detailHtml = this.httpUtils.doGetHtml(itemUrl);
                Document detailDocument = Jsoup.parse(detailHtml);
                String title = detailDocument.select(".sku-name").first().text();

                //https://p.3.cn/prices/mgets?skuIds=J_100009082466
                String priceJson = this.httpUtils.doGetHtml("https://p.3.cn/prices/mgets?skuIds=J_" + sku);
                double price = 0;
                try {
                    price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Item item = new Item();
                item.setSku(sku);
                List<Item> itemList = itemService.findAll(item);
                if (itemList == null || itemList.size() == 0) {
                    item.setSpu(spu);
                    item.setTitle(title);
                    item.setPrice(price);
                    item.setPic(pic);
                    item.setUrl(itemUrl);
                    item.setCreated(new Date());
                    item.setUpdated(new Date());
                    itemService.save(item);
                }

            }
        }
    }
}
