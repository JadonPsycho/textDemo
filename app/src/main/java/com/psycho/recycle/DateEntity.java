package com.psycho.recycle;

import java.util.List;

/**
 * Created by mr.psycho on 2017/1/13.
 */

public class DateEntity {

    /**
     * Status : 1
     * Result : [{"coID":"e34f5269-c6ab-e611-8902-c8f81052c51a","isBidded":false,"createTime":"2016-11-16","title":"急急急","regionInfo":"浙江省宁波市","coTypeName":"调档","content":"积极\n","viewCount":"已被查看18次","isMyBidSubmited":false},{"coID":"24e727cf-12ab-e611-8902-c8f81052c51a","isBidded":false,"createTime":"2016-11-15","title":"飞飞看这里","regionInfo":"浙江省宁波市","coTypeName":"诉讼合作","content":"雨呼呼","viewCount":"已被查看6次","isMyBidSubmited":false},{"coID":"1be02610-10ab-e611-8902-c8f81052c51a","isBidded":true,"createTime":"2016-11-15","title":"张东飞看这里","regionInfo":"浙江省宁波市","coTypeName":"调档","content":"看这里","viewCount":"已被查看12次","isMyBidSubmited":false},{"coID":"932cf74c-1ea7-e611-8902-c8f81052c51a","isBidded":true,"createTime":"2016-11-10","title":"挑战走一波","regionInfo":"浙江省宁波市","coTypeName":"诉讼合作","content":"剑三李白","viewCount":"已被查看12次","isMyBidSubmited":true},{"coID":"182e3cf7-eda6-e611-8902-c8f81052c51a","isBidded":false,"createTime":"2016-11-10","title":"很喜欢","regionInfo":"北京市北京市","coTypeName":"调档","content":"就是计算机","viewCount":"已被查看14次","isMyBidSubmited":false},{"coID":"cc9af6a0-5ea2-e611-8902-c8f81052c51a","isBidded":false,"createTime":"2016-11-04","title":"Dsadsadsadsa","regionInfo":"天津市天津市","coTypeName":"调查","content":"Dasdasdasdasdsadas","viewCount":"已被查看13次","isMyBidSubmited":false},{"coID":"21a71d12-1ca0-e611-8902-c8f81052c51a","isBidded":false,"createTime":"2016-11-01","title":"Dead as","regionInfo":"天津市天津市","coTypeName":"调查","content":"Dsadasdsadsadsadsadsa","viewCount":"已被查看13次","isMyBidSubmited":false},{"coID":"ccc879d3-f69f-e611-8902-c8f81052c51a","isBidded":false,"createTime":"2016-11-01","title":"啊啊啊","regionInfo":"浙江省宁波市","coTypeName":"调查","content":"大师的撒的","viewCount":"已被查看18次","isMyBidSubmited":false},{"coID":"7ae1c06d-ec9c-e611-8902-c8f81052c51a","isBidded":false,"createTime":"2016-10-28","title":"的范德萨发生大幅度","regionInfo":"浙江省宁波市","coTypeName":"调档","content":"是打发第三方第三方都睡","viewCount":"已被查看67次","isMyBidSubmited":false},{"coID":"4970a047-4a85-e611-a16f-00e07034112c","isBidded":false,"createTime":"2016-09-28","title":"一起去东莞","regionInfo":"浙江省宁波市","coTypeName":"调查","content":"老司机发车啦","viewCount":"已被查看34次","isMyBidSubmited":false}]
     */

    private int Status;
    private List<ResultEntity> Result;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public List<ResultEntity> getResult() {
        return Result;
    }

    public void setResult(List<ResultEntity> Result) {
        this.Result = Result;
    }

    public static class ResultEntity {
        /**
         * coID : e34f5269-c6ab-e611-8902-c8f81052c51a
         * isBidded : false
         * createTime : 2016-11-16
         * title : 急急急
         * regionInfo : 浙江省宁波市
         * coTypeName : 调档
         * content : 积极

         * viewCount : 已被查看18次
         * isMyBidSubmited : false
         */

        private String coID;
        private boolean isBidded;
        private String createTime;
        private String title;
        private String regionInfo;
        private String coTypeName;
        private String content;
        private String viewCount;
        private boolean isMyBidSubmited;

        public String getCoID() {
            return coID;
        }

        public void setCoID(String coID) {
            this.coID = coID;
        }

        public boolean isIsBidded() {
            return isBidded;
        }

        public void setIsBidded(boolean isBidded) {
            this.isBidded = isBidded;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRegionInfo() {
            return regionInfo;
        }

        public void setRegionInfo(String regionInfo) {
            this.regionInfo = regionInfo;
        }

        public String getCoTypeName() {
            return coTypeName;
        }

        public void setCoTypeName(String coTypeName) {
            this.coTypeName = coTypeName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getViewCount() {
            return viewCount;
        }

        public void setViewCount(String viewCount) {
            this.viewCount = viewCount;
        }

        public boolean isIsMyBidSubmited() {
            return isMyBidSubmited;
        }

        public void setIsMyBidSubmited(boolean isMyBidSubmited) {
            this.isMyBidSubmited = isMyBidSubmited;
        }
    }
}
