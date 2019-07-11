package com.feinik.csv.model;

import com.feinik.csv.annotation.CsvProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Feinik
 * @discription
 * @date 2019/7/10
 * @since 1.0.0
 */
@Data
public class CampaignModel implements Serializable {

    @CsvProperty(value = "日期", index = 0)
    private String day;

    @CsvProperty(value = "广告系列 ID", index = 1)
    private Long campaignId;

    @CsvProperty(value = "广告系列", index = 2)
    private String campaignName;

    @CsvProperty(value = "费用", index = 3, format = "{0}$")
    private Double cost;

    @CsvProperty(value = "点击次数", index = 4)
    private Long clicks;

    @CsvProperty(value = "点击率", index = 5, format = "{0}%")
    private Double ctr;

    public CampaignModel() {
    }

    public CampaignModel(String day, Long campaignId, String campaignName, Double cost, Long clicks, Double ctr) {
        this.day = day;
        this.campaignId = campaignId;
        this.campaignName = campaignName;
        this.cost = cost;
        this.clicks = clicks;
        this.ctr = ctr;
    }
}
