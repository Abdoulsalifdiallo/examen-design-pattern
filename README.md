# Examen de Design Pattern — BadWallet API & Payment Service

Deux microservices Spring Boot independants :

- **badwallet-api** (port 8080) : gestion des portefeuilles electroniques
  (creation, depot, retrait, transfert, paiement de factures, historique).
- **payment-service** (port 8081) : service externe de facturation (ISM,
  WOYAFAL), consomme par badwallet-api.

## Lancer les services

```bash
cd payment-service && mvn spring-boot:run
cd badwallet-api && mvn spring-boot:run
```

Consoles H2 : `http://localhost:8081/h2-console` et `http://localhost:8080/h2-console`
(JDBC URL `jdbc:h2:mem:paymentdb` / `jdbc:h2:mem:walletdb`, user `sa`, password vide).

Le fichier [test.http](test.http) reprend l'ensemble des requetes de test du
sujet (extension REST Client de VS Code), executables dans l'ordre.

A la creation d'un portefeuille (manuelle ou via /seed), badwallet-api appelle
automatiquement payment-service pour initialiser ses factures du mois en
cours (3 ISM + 3 WOYAFAL) : aucune etape manuelle n'est necessaire avant de
consulter ou payer ses factures.

## Design patterns appliques

| Pattern | Ou | Pourquoi |
|---|---|---|
| Builder | `Wallet.builder()` | construction de portefeuille avec generation de code |
| Strategy | `DepositStrategy` (CREDIT_CARD / WALLET_TARGET) | comportement de depot interchangeable selon le moyen de paiement |
| Decorator | `CappedFeeDecorator` sur le retrait | ajoute les frais (1%, plafonnes a 5000) sans modifier l'operation de base |
| Template Method | `AbstractWalletOperation` | squelette commun valider/executer/enregistrer pour depot, retrait, transfert, paiement |
| Adapter | `PaymentServiceAdapter` / `BillPaymentGateway` | traduit les appels vers payment-service en modele interne badwallet |
| Proxy | `FactureServiceProxy` | expose `/api/external/factures/...` en deleguant a payment-service |
| Strategy + Factory Method | `BillingRuleFactory` (cote payment-service) | regles de facturation specifiques par service (ISM / WOYAFAL) |

## Strategie Git

Feature branching : `feature/*` -> `develop` -> `main` (voir l'historique git).
