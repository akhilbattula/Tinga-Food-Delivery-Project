package com.sytiqhub.tinga.beans;

import java.util.List;

/**
 * Created by Malvika Sharma on 13-04-2019.
 */

public class CategoryBean {
    public String mCategory;
    List<FoodBean> sCategory;
    public CategoryBean(String a, List<FoodBean> b){
        mCategory=a;
        sCategory=b;
    }
    public String getm(){
        return mCategory;
    }
    public List<FoodBean> gets(){
        return sCategory;

    }
}
