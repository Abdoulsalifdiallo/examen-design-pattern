package sn.examen.designpattern.badwalletapi.controller;

import sn.examen.designpattern.badwalletapi.domain.Wallet;
import sn.examen.designpattern.badwalletapi.dto.BalanceResponse;
import sn.examen.designpattern.badwalletapi.dto.DepositRequest;
import sn.examen.designpattern.badwalletapi.dto.TransactionResponse;
import sn.examen.designpattern.badwalletapi.dto.PayFacturesRequest;
import sn.examen.designpattern.badwalletapi.dto.PayRequest;
import sn.examen.designpattern.badwalletapi.dto.WalletCreateRequest;
import sn.examen.designpattern.badwalletapi.dto.TransferRequest;
import sn.examen.designpattern.badwalletapi.dto.WalletResponse;
import sn.examen.designpattern.badwalletapi.dto.WithdrawRequest;
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

import java.util.List;

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

    @PostMapping("/{id}/deposit")
    public List<TransactionResponse> deposit(@PathVariable Long id, @Valid @RequestBody DepositRequest request) {
        return walletService.deposit(id, request.getAmount(), request.getPaymentMethod())
                .stream().map(TransactionResponse::new).toList();
    }

    @PostMapping("/withdraw")
    public List<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        return walletService.withdraw(request.getPhoneNumber(), request.getAmount())
                .stream().map(TransactionResponse::new).toList();
    }

    @PostMapping("/transfer")
    public List<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        return walletService.transfer(request.getSenderPhone(), request.getReceiverPhone(), request.getAmount())
                .stream().map(TransactionResponse::new).toList();
    }

    @PostMapping("/pay")
    public List<TransactionResponse> pay(@Valid @RequestBody PayRequest request) {
        return walletService.payCurrentMonthBill(request.getPhoneNumber(), request.getServiceName(), request.getAmount())
                .stream().map(TransactionResponse::new).toList();
    }

    @PostMapping("/pay-factures")
    public List<TransactionResponse> payFactures(@Valid @RequestBody PayFacturesRequest request) {
        return walletService.payFactures(request.getPhoneNumber(), request.getServiceName(), request.getFactureReferences())
                .stream().map(TransactionResponse::new).toList();
    }

    @GetMapping("/{phone}/transactions")
    public List<TransactionResponse> getTransactions(@PathVariable String phone) {
        return walletService.getTransactions(phone).stream().map(TransactionResponse::new).toList();
    }
}
