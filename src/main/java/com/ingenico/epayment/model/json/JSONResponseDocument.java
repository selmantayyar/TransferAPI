package com.ingenico.epayment.model.json;

import java.util.ArrayList;
import java.util.List;

public class JSONResponseDocument<T,K> {

    private T data;

    private List<K> errors = new ArrayList<>();

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<K> getErrors() {
        return errors;
    }

    public void setErrors(List<K> errors) {
        this.errors = errors;
    }
}
