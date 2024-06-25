package com.danilo.zeparceiros.exceptions;

public class PartnerNotFoundException extends RuntimeException {
    public PartnerNotFoundException(Long id) {
        super("Partner with id " + id + " not found");
    }
}
