# easycsv
通过easycsv组件可以方便处理csv文本内容的写入与读取功能，大数据量也不会导致系统OOM异常

# Maven包引入
```
<dependency>
    <groupId>com.github.aifeinik</groupId>
    <artifactId>easycsv</artifactId>
    <version>1.0</version>
</dependency>
```

# 具体使用
```
public class CsvTest {

    CampaignModel m1 = new CampaignModel("2019-01-01", 10000000L, "campaign1", 12.21d, 300L, 0.12d);
    CampaignModel m2 = new CampaignModel("2019-01-02", 12000001L, "campaign2", 13.01d, 80L, 0.13d);
    CampaignModel m3 = new CampaignModel("2019-01-03", 13000002L, "campaign3", 14.11d, 100L, 0.14d);
    CampaignModel m4 = new CampaignModel("2019-01-04", 15000002L, "campaign4", 10.11d, 99L, 0.16d);
    ArrayList<CampaignModel> data1 = Lists.newArrayList(m1, m2);
    ArrayList<CampaignModel> data2 = Lists.newArrayList(m3, m4);

    private String writeFilePath = "G:/tmp/campaign.csv";

    /**
     * 小数据量写入
     */
    public void writeSmallData() {
        CsvWriter writer = null;
        try {
            writer = CsvFactory.getWriter(writeFilePath, Charset.forName("GBK"));
            CsvUtil.write(writer, data1, true, null);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    /**
     * 大数据量通过分批写入，不至于导致OOM问题
     */
    @Test
    public void writeLargeData() {
        CsvWriter writer = null;
        try {
            writer = CsvFactory.getWriter(writeFilePath, Charset.forName("GBK"));
            //首次写入数据包含写入head行
            CsvUtil.write(writer, data1, true, null);

            //下一批数据写入不在写入head行
            CsvUtil.write(writer, data2, false, null);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 小数据量读取
     * @throws Exception
     */
    @Test
    public void readSmallData() throws Exception {
        final InputStream is = FileUtil.getResourcesFileInputStream("campaign.csv");

        List<CampaignModel> models = CsvUtil.read(is, CampaignModel.class, 1, Charset.forName("GBK"), null);
        print(models);
    }

    /**
     * 大数据量读取
     * @throws Exception
     */
    @Test
    public void readLargeData() throws Exception {
        final InputStream is = FileUtil.getResourcesFileInputStream("campaign.csv");
        CsvUtil.readOnListener(is, CampaignModel.class, 1, Charset.forName("GBK"), null , new CampaignDataListener());

    }

    public static class CampaignDataContext implements CsvContext {

        @Override
        public void initWriter(CsvWriter writer) {}

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
```
