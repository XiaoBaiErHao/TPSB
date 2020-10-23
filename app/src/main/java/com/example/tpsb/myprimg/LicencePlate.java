package com.example.tpsb.myprimg;

import java.util.List;

public class LicencePlate {

    private List<CodeInfoBean> codeInfo;

    public List<CodeInfoBean> getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(List<CodeInfoBean> codeInfo) {
        this.codeInfo = codeInfo;
    }

    public static class CodeInfoBean {
        /**
         * Hp : 冀A
         * city : 石家庄
         * province : 河北
         * Pcode : HB
         * AreaCode : 130100
         */

        private String Hp;
        private String city;
        private String province;
        private String Pcode;
        private String AreaCode;

        public String getHp() {
            return Hp;
        }

        public void setHp(String Hp) {
            this.Hp = Hp;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getPcode() {
            return Pcode;
        }

        public void setPcode(String Pcode) {
            this.Pcode = Pcode;
        }

        public String getAreaCode() {
            return AreaCode;
        }

        public void setAreaCode(String AreaCode) {
            this.AreaCode = AreaCode;
        }
    }
}
