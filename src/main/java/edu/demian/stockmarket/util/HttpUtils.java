package edu.demian.stockmarket.util;

import edu.demian.stockmarket.exception.WrongStatusCodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpUtils {

    public static <T> T checkStatusCodeAndReturnResponseBody(ResponseEntity<T> responseEntity, HttpStatus expected) {
        HttpStatus actual = responseEntity.getStatusCode();
        if (actual == expected) {
            return responseEntity.getBody();
        }
        throw new WrongStatusCodeException("Status code = " + actual + ", " + expected + " is expected");
    }

}
