package sn.examen.designpattern.paymentservice.controller;

import sn.examen.designpattern.paymentservice.domain.ServiceName;
import sn.examen.designpattern.paymentservice.dto.FactureDto;
import sn.examen.designpattern.paymentservice.dto.InitFacturesRequest;
import sn.examen.designpattern.paymentservice.dto.PaymentReceiptDto;
import sn.examen.designpattern.paymentservice.dto.PaymentRequestDto;
import sn.examen.designpattern.paymentservice.service.FactureInitializationService;
import sn.examen.designpattern.paymentservice.service.FactureService;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
public class FactureController {

    private final FactureService factureService;
    private final FactureInitializationService initializationService;

    public FactureController(FactureService factureService, FactureInitializationService initializationService) {
        this.factureService = factureService;
        this.initializationService = initializationService;
    }

    @PostMapping("/init")
    public ResponseEntity<List<FactureDto>> init(@Valid @RequestBody InitFacturesRequest request) {
        List<FactureDto> created = factureService.toDtos(initializationService.initializeFor(request.getWalletCode()));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{code}/current")
    public List<FactureDto> current(@PathVariable String code,
                                     @RequestParam(required = false) ServiceName unite) {
        return factureService.getCurrentMonthFactures(code, unite);
    }

    @GetMapping("/{code}/periode")
    public List<FactureDto> periode(@PathVariable String code,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return factureService.getFacturesByPeriod(code, debut, fin);
    }

    @PostMapping("/pay")
    public ResponseEntity<PaymentReceiptDto> pay(@Valid @RequestBody PaymentRequestDto request) {
        PaymentReceiptDto receipt = (request.getFactureReferences() != null && !request.getFactureReferences().isEmpty())
                ? factureService.payByReferences(request.getWalletCode(), request.getServiceName(), request.getFactureReferences())
                : factureService.payCurrentMonth(request.getWalletCode(), request.getServiceName(), request.getAmount());
        return ResponseEntity.status(HttpStatus.OK).body(receipt);
    }
}
