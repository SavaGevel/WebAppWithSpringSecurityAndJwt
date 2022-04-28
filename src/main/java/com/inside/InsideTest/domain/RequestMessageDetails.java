package com.inside.InsideTest.domain;

import lombok.Data;

/**
 * Auxiliary class for processing messages in JSON format from users
 */

@Data
public class RequestMessageDetails {
    private final String username;
    private final String message;
}
