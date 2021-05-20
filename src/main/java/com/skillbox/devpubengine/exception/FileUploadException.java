package com.skillbox.devpubengine.exception;

import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileUploadException extends RuntimeException {

    private final GenericResultResponse response;
    private final String reason;

    private final static String UPLOAD_FAILED = "Не удалось загрузить файл";
    private final static String BAD_EXTENSION = "Неизвестный формат файла. Поддерживаются форматы JPEG и PNG.";
    private final static String SIZE_TOO_LARGE = "Размер файла превышает допустимый размер (10 МБ).";

    @Override
    public String toString() {
        switch (reason) {
            case "image":
                return SIZE_TOO_LARGE;
            case "extension":
                return BAD_EXTENSION;
            case "upload":
            default:
                return UPLOAD_FAILED;
        }
    }

    public FileUploadException() {
        super(UPLOAD_FAILED);
        this.reason = "upload";
        this.response = new GenericResultResponse(false, Map.of(reason, UPLOAD_FAILED));
    }

    public FileUploadException(String reason) {
        this.reason = reason;
        Map<String, String> errors = new HashMap<>();
        switch (reason) {
            case "image":
                errors.put(reason, SIZE_TOO_LARGE);
                break;
            case "extension":
                errors.put(reason, BAD_EXTENSION);
                break;
            case "upload":
            default:
                errors.put(reason, UPLOAD_FAILED);
                break;
        }
        this.response = new GenericResultResponse(false, errors);
    }

    public GenericResultResponse getResponse() {
        return response;
    }

    public String getReason() {
        return reason;
    }
}
