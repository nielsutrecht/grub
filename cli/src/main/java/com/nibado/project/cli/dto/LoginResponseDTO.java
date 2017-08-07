package com.nibado.project.cli.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String expires;
}
