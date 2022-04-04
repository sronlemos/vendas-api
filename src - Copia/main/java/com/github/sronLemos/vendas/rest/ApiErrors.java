package com.github.sronLemos.vendas.rest;

import java.util.Arrays;
import java.util.List;

public class ApiErrors {

    private List<String> errors;

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiErrors(String mensagem) {
        this.errors = Arrays.asList(mensagem);
    }

    public List<String> getErrors() {
        return errors;
    }

}
