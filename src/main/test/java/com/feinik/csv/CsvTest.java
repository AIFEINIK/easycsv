package com.feinik.csv;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.feinik.csv.context.CsvContext;
import com.feinik.csv.listener.CampaignDataListener;
import com.feinik.csv.model.CampaignModel;
import com.feinik.csv.util.FileUtil;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Feinik
 * @discription
 * @date 2019/7/11
 * @since 1.0.0
 */
public class CsvTest {

    private String writeFilePath = "G:/tmp/campaign.csv";

    @Test
    public void write() {
        CampaignModel m1 = new CampaignModel("2019-01-01", 10000000L, "campaign1", 12.21d, 100L, 0.12d);
        CampaignModel m2 = new CampaignModel("2019-01-02", 12000001L, "campaign2", 13.01d, 100L, 0.13d);
        CampaignModel m3 = new CampaignModel("2019-01-03", 13000002L, "campaign3", 14.11d, 100L, 0.14d);
        ArrayList<CampaignModel> campaignModels = Lists.newArrayList(m1, m2, m3);

        CsvWriter writer = null;
        try {
            writer = CsvFactory.getWriter(writeFilePath, Charset.forName("GBK"));
            CsvUtil.write(writer, campaignModels, true, null);
            CsvUtil.write(writer, campaignModels, false, null);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @Test
    public void read() throws Exception {
        final InputStream is = FileUtil.getResourcesFileInputStream("campaign.csv");

        //少数据量读取
        //List<CampaignModel> models = CsvUtil.read(is, CampaignModel.class, 1, Charset.forName("GBK"), null);
        //print(models);

        //大数据量读取
        CsvUtil.readOnListener(is, CampaignModel.class, 1, Charset.forName("GBK"), null , new CampaignDataListener());

    }

    public static class CampaignDataContext implements CsvContext {

        @Override
        public void initWriter(CsvWriter writer) {
            writer.setDelimiter('#');
        }

        @Override
        public void initReader(CsvReader reader) {
            //通过这里设置headers可灵活设置具体csv文件解析对应的head顺序，可通过将headers参数配置化（如：通过配置中心读取）
            reader.setHeaders(new String[]{"日期", "广告系列 ID", "广告系列", "费用", "点击次数", "点击率"});
        }
    }

    public void print(List<CampaignModel> objects) {
        for (CampaignModel object : objects) {
            System.out.println(object);
        }
    }
}
