package com.example.atpeople.myapplication.youTuApi.model;

import java.util.List;

/**
 * Create by peng on 2019/8/1
 */
public class BusinessCardBean extends BaseBean{
    public int angle;
    public String session_id;
    public List<ItemsBean> items;

    public static class ItemsBean {
        public String itemstring;
        public String item;
        public ItemcoordBean itemcoord;
        public List<?> coords;
        public List<?> words;
        public List<?> candword;
        public List<?> wordcoordpoint;


        public static class ItemcoordBean {
            public int x;
            public int y;
            public int width;
            public int height;
        }
    }
}
