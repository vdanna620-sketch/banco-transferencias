package com.tdea.banktransfer.dto;

import java.time.Instant;

/**
 * Uniform error body returned by the API.
 *
 * @param timestamp moment of the error
 * @param status    HTTP status code
 * @param error     HTTP reason phrase
 * @param message   human readable explanation
 * @param path      request path
 */
public record ErrorResponse(Instant timestamp, int status, String error, String message, String path) {
}
