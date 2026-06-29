package sn.examen.designpattern.badwalletapi.controller;

import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.dto.BalanceResponse;
import sn.examen.designpattern.badwalletapi.dto.WalletCreateRequest;
import sn.examen.designpattern.badwalletapi.dto.WalletResponse;
import sn.examen.designpattern.badwalletapi.service.WalletService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletResponse> create(@Valid @RequestBody WalletCreateRequest request) {
        Wallet wallet = walletService.createWallet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new WalletResponse(wallet));
    }

    @GetMapping
    public Page<WalletResponse> list(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        return walletService.listWallets(PageRequest.of(page, size)).map(WalletResponse::new);
    }

    @GetMapping("/{phone}")
    public WalletResponse getByPhone(@PathVariable String phone) {
        return new WalletResponse(walletService.getByPhone(phone));
    }

    @GetMapping("/{phone}/balance")
    public BalanceResponse getBalance(@PathVariable String phone) {
        Wallet wallet = walletService.getByPhone(phone);
        return new BalanceResponse(wallet.getPhoneNumber(), wallet.getBalance(), wallet.getCurrency());
    }
}
