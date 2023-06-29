package com.qazima.habari.plugin.core;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpStatus;

/**
 * <p>
 * Class for the result of each action.
 * It is used to give from the current action to the following one.
 *
 * @author Clement BONET
 * @version 1.0.0
 */
public class Content {
    /**
     * <p>
     * The status code of the current result.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    private int statusCode = HttpStatus.SC_OK;
    /**
     * <p>
     * The type of the current result.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    private String type = "text/plain";
    /**
     * <p>
     * The content of the current result.
     *
     * @author Clement BONET
     * @version 1.0.0
     */
    @Getter
    @Setter
    private byte[] body;
}
