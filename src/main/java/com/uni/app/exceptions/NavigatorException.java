package com.uni.app.exceptions;

import java.io.Serial;

public class NavigatorException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public NavigatorException(String message) { super(message); }
}
