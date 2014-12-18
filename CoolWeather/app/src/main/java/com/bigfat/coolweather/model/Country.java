package com.bigfat.coolweather.model;

/**
 * @author <a href="mailto:fbzhh007@gmail.com">bigfat</a>
 * @since 2014/12/18
 */
public class Country {
    private String areaId;
    private String nameCn;
    private String DistrictCn;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getDistrictCn() {
        return DistrictCn;
    }

    public void setDistrictCn(String districtCn) {
        DistrictCn = districtCn;
    }
}
