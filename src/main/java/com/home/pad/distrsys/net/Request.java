package com.home.pad.distrsys.net;


public class Request {
    private String orderBy = "";
    private String groupBy = "";
    private String filterBy = "";

    public Request() {
    }

    public Request(String orderBy, String groupBy, String filterBy) {
        this.orderBy = orderBy;
        this.groupBy = groupBy;
        this.filterBy = filterBy;
    }

    private Request(Builder builder) {
        this.orderBy = builder.orderBy;
        this.groupBy = builder.groupBy;
        this.filterBy = builder.filterBy;
    }

    public static Builder newRequest() {
        return new Builder();
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public String getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(String filterBy) {
        this.filterBy = filterBy;
    }

    @Override
    public String toString() {
        return "Request{" +
                "orderBy='" + orderBy + '\'' +
                ", groupBy='" + groupBy + '\'' +
                ", filterBy='" + filterBy + '\'' +
                '}';
    }


    public static final class Builder {
        private String orderBy;
        private String groupBy;
        private String filterBy;

        private Builder() {
        }

        public Request build() {
            return new Request(this);
        }

        public Builder orderBy(String orderBy) {
            this.orderBy = orderBy;
            return this;
        }

        public Builder groupBy(String groupBy) {
            this.groupBy = groupBy;
            return this;
        }

        public Builder filterBy(String filterBy) {
            this.filterBy = filterBy;
            return this;
        }
    }
}
