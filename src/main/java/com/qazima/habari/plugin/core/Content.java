package com.qazima.habari.plugin.core;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

/**
 * <p>
 * The content used to pass result within all the plugins (in cascade mode) and send the final result to client.
 * </p>
 * @author Clément BONET
 * @since 2022-10-01
 * @version 0.1
 */
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
