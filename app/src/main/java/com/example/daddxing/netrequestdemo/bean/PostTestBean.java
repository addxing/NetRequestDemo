package com.example.daddxing.netrequestdemo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class PostTestBean implements Serializable {

    /**
     * status : 00
     * msg : 查询成功
     * data : [{"id":"491","imgPath":"2018/03/25/5ab78b7a5a90b.png","introduction":"","eventType":2,"eventValue":"http://mp.weixin.qq.com/s?__biz=MjM5MjQ3MDgwMQ==&mid=2650848287&idx=1&sn=b3e8f69e7de513880f7604d04fb3013e&chksm=bd51de618a2657773c2ea6a71402b504ad61ce33c15a72efeabf8df5d2bdad8dfb8bdb8c0359&mpshare=1&scene=23&srcid=0324zJ1J5v4beXoSuFYNpj7G#rd"}]
     */

    private String status;
    private String msg;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 491
         * imgPath : 2018/03/25/5ab78b7a5a90b.png
         * introduction :
         * eventType : 2
         * eventValue : http://mp.weixin.qq.com/s?__biz=MjM5MjQ3MDgwMQ==&mid=2650848287&idx=1&sn=b3e8f69e7de513880f7604d04fb3013e&chksm=bd51de618a2657773c2ea6a71402b504ad61ce33c15a72efeabf8df5d2bdad8dfb8bdb8c0359&mpshare=1&scene=23&srcid=0324zJ1J5v4beXoSuFYNpj7G#rd
         */

        private String id;
        private String imgPath;
        private String introduction;
        private int eventType;
        private String eventValue;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getEventType() {
            return eventType;
        }

        public void setEventType(int eventType) {
            this.eventType = eventType;
        }

        public String getEventValue() {
            return eventValue;
        }

        public void setEventValue(String eventValue) {
            this.eventValue = eventValue;
        }
    }
}
