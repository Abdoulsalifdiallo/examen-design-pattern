package sn.examen.designpattern.badwalletapi.controller;

import sn.examen.designpattern.badwalletapi.proxy.FactureProvider;
import sn.examen.designpattern.badwalletapi.proxy.FactureView;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * PARTIE 2 du sujet : "Proxy API dans badwallet-api". Ces endpoints
 * exposent localement les factures de payment-service via FactureServiceProxy.
 */
@RestController
@RequestMapping("/api/external/factures")
public class FactureProxyController {

    private final FactureProvider factureProvider;

    public FactureProxyController(FactureProvider factureProvider) {
        this.factureProvider = factureProvider;
    }

    @GetMapping("/{code}/current")
    public List<FactureView> current(@PathVariable String code,
                                      @RequestParam(required = false) String unite) {
        return factureProvider.getCurrentMonthFactures(code, unite);
    }

    @GetMapping("/{code}/periode")
    public List<FactureView> periode(@PathVariable String code,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return factureProvider.getFacturesByPeriod(code, debut, fin);
    }
}
