package cn.itcast.jobInfo.pojo;

import java.io.Serializable;
import java.util.List;

public class ResultPage implements Serializable {

    private List<Job> rows;

    private Integer pageTotal;

    public List<Job> getRows() {
        return rows;
    }

    public void setRows(List<Job> rows) {
        this.rows = rows;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }
}
