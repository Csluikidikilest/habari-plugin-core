package com.qazima.habari.plugin.core;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

public class Content {
    @Getter
    @Setter
    private int statusCode = HttpStatus.SC_OK;
    @Getter
    @Setter
    private String type = "text/plain";
    @Getter
    @Setter
    private byte[] body;
}
