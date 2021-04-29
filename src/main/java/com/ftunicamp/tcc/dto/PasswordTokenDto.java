package com.ftunicamp.tcc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordTokenDto {
    private boolean valido;
}
