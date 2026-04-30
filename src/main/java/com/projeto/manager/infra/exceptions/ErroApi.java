package com.projeto.manager.infra.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErroApi {

    private String code;

    private String message;

    private int status;

    private String path;

    OffsetDateTime timeStamp;

}
