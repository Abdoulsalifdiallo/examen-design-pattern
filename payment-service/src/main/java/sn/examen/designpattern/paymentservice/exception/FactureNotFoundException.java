package sn.examen.designpattern.paymentservice.exception;

public class FactureNotFoundException extends RuntimeException {

    public FactureNotFoundException(String message) {
        super(message);
    }
}
