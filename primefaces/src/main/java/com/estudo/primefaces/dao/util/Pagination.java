package com.estudo.primefaces.dao.util;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Pagination {
    private final Integer offset;
    private final Integer size;

    public Pagination(Integer offset, Integer size) {
        this.offset = offset;
        this.size = size;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
