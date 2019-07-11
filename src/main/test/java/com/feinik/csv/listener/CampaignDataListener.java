package com.feinik.csv.listener;

import com.feinik.csv.event.CsvListener;
import com.feinik.csv.model.CampaignModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feinik
 * @discription
 * @date 2019/7/11
 * @since 1.0.0
 */
public class CampaignDataListener extends CsvListener<CampaignModel> {

    List<CampaignModel> dataList = new ArrayList<>();

    @Override
    public void invoke(CampaignModel object) {
        dataList.add(object);
        if (dataList.size() > 1000) {
            doSomething();
            dataList = new ArrayList<>();
        }
    }

    @Override
    public void complete() {
        doSomething();
        System.out.println("数据读取结束了");
    }

    public void doSomething() {
        for (CampaignModel model : dataList) {
            System.out.println(model);
        }
    }
}
