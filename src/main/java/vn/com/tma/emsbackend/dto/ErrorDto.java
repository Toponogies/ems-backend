package vn.com.tma.emsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDto {
    private Date timestamp;
    private String message;
    private String details;
}