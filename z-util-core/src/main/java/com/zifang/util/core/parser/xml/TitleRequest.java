package com.zifang.util.core.parser.xml;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "RequestOrder")
public class TitleRequest {

    private List<Item> item;

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "TitleRequest{item=" + item + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TitleRequest that = (TitleRequest) o;
        return java.util.Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(item);
    }

    @XmlType(propOrder = {"code", "province", "city", "district"})
    public static class Item {

        private String code;
        private String province;
        private String city;
        private String district;

        public Item() {
        }

        public Item(String code, String province, String city, String district) {
            this.code = code;
            this.province = province;
            this.city = city;
            this.district = district;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        @Override
        public String toString() {
            return "Item{code=" + code + ", province=" + province + ", city=" + city + ", district=" + district + "}";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return java.util.Objects.equals(code, item.code) &&
                    java.util.Objects.equals(province, item.province) &&
                    java.util.Objects.equals(city, item.city) &&
                    java.util.Objects.equals(district, item.district);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(code, province, city, district);
        }
    }
}