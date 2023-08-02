package demo.dto;

import java.util.List;

public class SourceDto<T> {

    private T data;

    private List<T> datas;

    private List<String> actualDatas;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public List<String> getActualDatas() {
        return actualDatas;
    }

    public void setActualDatas(List<String> actualDatas) {
        this.actualDatas = actualDatas;
    }
}
