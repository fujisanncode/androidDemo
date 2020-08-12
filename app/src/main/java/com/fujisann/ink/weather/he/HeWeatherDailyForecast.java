package com.fujisann.ink.weather.he;

import com.google.gson.annotations.SerializedName;

public class HeWeatherDailyForecast {
    private String date;
    private Cond cond;
    private Tmp tmp;
    public class Cond {
        @SerializedName("txt_d")
        private String txtD;

        public String getTxtD() {
            return txtD;
        }

        public void setTxtD(String txtD) {
            this.txtD = txtD;
        }
    }
    public class Tmp {
        private String max;
        private String min;

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Cond getCond() {
        return cond;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public Tmp getTmp() {
        return tmp;
    }

    public void setTmp(Tmp tmp) {
        this.tmp = tmp;
    }
}
