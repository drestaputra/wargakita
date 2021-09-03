package dresta.putra.wargakita.slider;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SliderPojoResponse {
    @SerializedName("status")
    Integer status;
    @SerializedName("msg")
    String msg;
    @SerializedName("page")
    Integer page;
    @SerializedName("totalPage")
    int totalPage;
    @SerializedName("recordsFiltered")
    Integer recordsFiltered;
    @SerializedName("totalRecords")
    Integer totalRecords;
    @SerializedName("data")
    List<SliderPojo> data;

    public SliderPojoResponse(Integer status, String msg, Integer page, int totalPage, Integer recordsFiltered, Integer totalRecords, List<SliderPojo> data) {
        this.status = status;
        this.msg = msg;
        this.page = page;
        this.totalPage = totalPage;
        this.recordsFiltered = recordsFiltered;
        this.totalRecords = totalRecords;
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<SliderPojo> getData() {
        return data;
    }

    public void setData(List<SliderPojo> data) {
        this.data = data;
    }
}
