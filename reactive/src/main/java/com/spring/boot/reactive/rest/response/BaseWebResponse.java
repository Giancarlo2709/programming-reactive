package com.spring.boot.reactive.rest.response;

import com.spring.boot.reactive.enums.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseWebResponse<T> {

    private ErrorCode errorCode;
    private T data;

    public static BaseWebResponse successNoData() {
        return BaseWebResponse.builder()
                .build();
    }

    public static <T> BaseWebResponse<T> successWithData(T data) {
        return BaseWebResponse.<T>builder()
                .data(data)
                .build();
    }

    public static BaseWebResponse error(ErrorCode errorCode) {
        return BaseWebResponse.builder()
                .errorCode(errorCode)
                .build();
    }
}
