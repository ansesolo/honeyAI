# HoneyAI Coding Standards

**Version:** 1.0
**Date:** 2026-01-18
**Source:** Extracted from Architecture Document v1.0
**Status:** Complete - Mandatory Standards

---

## Overview

These coding standards are **mandatory** for all HoneyAI development. They prevent common mistakes, ensure consistency, and maintain code quality for long-term family support.

---

## Critical Fullstack Rules

**These standards prevent common mistakes. ALL code must follow these rules.**

### 1. Transaction Boundaries
All service write methods **MUST** have `@Transactional` annotation.

**Example:**
```java
@Service
@Transactional  // Default for all methods
public class ClientService {

    @Transactional(readOnly = true)  // Optimization for read-only
    public List<Client> findAllActive() {
        return clientRepository.findByDeletedAtIsNullOrderByNomAsc();
    }

    // Write method - uses class-level @Transactional
    public Client save(Client client) {
        return clientRepository.save(client);
    }
}
```

### 2. Soft Delete Queries
All client queries **MUST** filter `deletedAt IS NULL` to exclude soft-deleted records.

**Example:**
```java
// ✅ CORRECT
List<Client> findByDeletedAtIsNullOrderByNomAsc();

Optional<Client> findByIdAndDeletedAtIsNull(Long id);

// ❌ WRONG - Will return deleted clients
List<Client> findAll();
Optional<Client> findById(Long id);
```

### 3. Price Snapshots
Always store `prixUnitaire` in `LigneCommande` at order creation time.

**Rationale:** Prices change over time. Historical orders must reflect the price at the time of purchase.

**Example:**
```java
public Commande create(Commande commande) {
    for (LigneCommande ligne : commande.getLignes()) {
        if (ligne.getPrixUnitaire() == null) {
            // Fetch current price and store it
            BigDecimal prix = produitService.getCurrentYearTarif(ligne.getProduit().getId());
            ligne.setPrixUnitaire(prix);
        }
    }
    return commandeRepository.save(commande);
}
```

### 4. Status Transitions
Validate status transitions before changing `StatutCommande`.

**Allowed transitions:**
- `COMMANDEE` → `RECUPEREE`
- `RECUPEREE` → `PAYEE`

**Forbidden transitions:**
- `PAYEE` → `COMMANDEE` (backward)
- `COMMANDEE` → `PAYEE` (skip RECUPEREE)

**Example:**
```java
public void updateStatut(Long commandeId, StatutCommande newStatut) {
    Commande commande = findById(commandeId);

    if (!commande.canTransitionTo(newStatut)) {
        throw new InvalidStatusTransitionException(
            String.format("Cannot transition from %s to %s",
                commande.getStatut(), newStatut)
        );
    }

    commande.setStatut(newStatut);
    commandeRepository.save(commande);
}
```

### 5. Enum Storage
Always use `@Enumerated(EnumType.STRING)` for enum fields.

**Rationale:** Ordinal storage (0, 1, 2) breaks when enum order changes. String storage is readable and migration-safe.

**Example:**
```java
@Entity
public class Commande {

    @Enumerated(EnumType.STRING)  // ✅ CORRECT - Stores "COMMANDEE", "RECUPEREE", "PAYEE"
    @Column(nullable = false)
    private StatutCommande statut;

    // ❌ WRONG - Stores 0, 1, 2
    // @Enumerated(EnumType.ORDINAL)
}
```

### 6. XSS Prevention
Use `th:text` in Thymeleaf, never `th:utext` for user input.

**Example:**
```html
<!-- ✅ CORRECT - Auto-escapes HTML -->
<p th:text="${client.notes}">Notes</p>

<!-- ❌ DANGEROUS - Allows HTML injection -->
<p th:utext="${client.notes}">Notes</p>
```

### 7. Exception Handling
Throw domain exceptions, caught by `GlobalExceptionHandler`.

**Example:**
```java
// Service layer
public Client findById(Long id) {
    return clientRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new ClientNotFoundException("Client #" + id + " not found"));
}

// GlobalExceptionHandler
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public String handleClientNotFound(ClientNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }
}
```

### 8. Logging
Use SLF4J via Lombok `@Slf4j`, never `System.out.println()`.

**Example:**
```java
@Service
@Slf4j  // Lombok generates: private static final Logger log = ...
public class ClientService {

    public Client save(Client client) {
        log.info("Saving client: {}", client.getNom());  // ✅ CORRECT
        // System.out.println("Saving client");  // ❌ WRONG
        return clientRepository.save(client);
    }
}
```

### 9. BigDecimal for Money
Never use `float` or `double` for currency. Always use `BigDecimal`.

**Rationale:** Floating-point arithmetic is imprecise. `12.50 - 12.49` may not equal `0.01` in float.

**Example:**
```java
@Entity
public class LigneCommande {

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;  // ✅ CORRECT

    // private double prixUnitaire;  // ❌ WRONG - Precision loss
}

// Arithmetic
BigDecimal total = ligne.getPrixUnitaire()
    .multiply(BigDecimal.valueOf(ligne.getQuantite()))
    .setScale(2, RoundingMode.HALF_UP);
```

### 10. Constructor Injection
Use Lombok `@RequiredArgsConstructor` for dependency injection.

**Rationale:** Constructor injection is immutable, testable, and prevents null dependencies.

**Example:**
```java
@Service
@RequiredArgsConstructor  // ✅ CORRECT - Generates constructor
public class ClientService {
    private final ClientRepository clientRepository;  // final = required

    // Lombok generates:
    // public ClientService(ClientRepository clientRepository) {
    //     this.clientRepository = clientRepository;
    // }
}

// ❌ WRONG - Field injection
// @Autowired
// private ClientRepository clientRepository;
```

---

## Naming Conventions

### Java Classes

| Element | Convention | Example |
|---------|------------|---------|
| Entity | Singular noun, PascalCase | `Client`, `Commande`, `LigneCommande` |
| Repository | `{Entity}Repository` | `ClientRepository` |
| Service | `{Entity}Service` | `ClientService` |
| Controller | `{Entity}Controller` | `ClientController` |
| DTO | Purpose + `Dto` or `Request` | `TopProduitDto`, `EtiquetteRequest` |
| Exception | Descriptive + `Exception` | `ClientNotFoundException` |
| Enum | Singular noun | `StatutCommande`, `TypeMiel` |

### Java Methods

**Convention:** camelCase, verb-first

**Examples:**
```java
// CRUD operations
public Client save(Client client)
public Optional<Client> findById(Long id)
public List<Client> findAllActive()
public void delete(Long id)

// Business operations
public void softDelete(Long id)
public BigDecimal calculateTotal(Long commandeId)
public void updateStatut(Long id, StatutCommande newStatut)

// Boolean queries
public boolean isDeleted()
public boolean canTransitionTo(StatutCommande newStatut)
```

### Database Tables & Columns

**Convention:** snake_case, lowercase

| Java | Database |
|------|----------|
| `Client` entity | `clients` table |
| `dateCommande` field | `date_commande` column |
| `prixUnitaire` field | `prix_unitaire` column |

**Example:**
```java
@Entity
@Table(name = "clients")  // Explicit table name
public class Client {

    @Column(name = "date_commande")  // Explicit column name
    private LocalDate dateCommande;
}
```

### URL Paths

**Convention:** kebab-case, lowercase, RESTful

**Examples:**
```
GET  /clients                  # List
GET  /clients/nouveau          # New form
POST /clients                  # Create
GET  /clients/{id}             # Detail
GET  /clients/{id}/edit        # Edit form
POST /clients/{id}/delete      # Delete
```

### CSS Classes

**Convention:** kebab-case, BEM-inspired

**Examples:**
```css
.btn-primary
.form-control
.client-list
.order-status-badge
.error-message
```

---

## Code Organization Standards

### File Header Comments

**Not required** - Code should be self-documenting via clear naming.

**Exception:** Complex algorithms or business logic.

```java
/**
 * Calculates DLUO (Best Before Date) for honey labels.
 * Formula: Harvest Date + 730 days (2 years per French regulation).
 * Display format: MM/YYYY (month and year only).
 */
public LocalDate calculateDluo(LocalDate dateRecolte) {
    return dateRecolte.plusDays(730);
}
```

### Method Length

**Guideline:** Maximum 30 lines per method.

If longer, extract helper methods.

**Example:**
```java
// ❌ TOO LONG
public Commande createCommande(CommandeDto dto) {
    // 50+ lines of validation, price lookup, calculation...
}

// ✅ BETTER - Extracted helpers
public Commande createCommande(CommandeDto dto) {
    validateCommande(dto);
    Commande commande = buildCommande(dto);
    enrichWithPrices(commande);
    return commandeRepository.save(commande);
}

private void validateCommande(CommandeDto dto) { /* ... */ }
private Commande buildCommande(CommandeDto dto) { /* ... */ }
private void enrichWithPrices(Commande commande) { /* ... */ }
```

### Class Length

**Guideline:** Maximum 300 lines per class.

If larger, consider splitting:
- Extract utility methods to separate `*Utils` class
- Split complex service into multiple specialized services

---

## Spring Boot Annotations

### Controller Layer

```java
@Controller                           // Spring MVC controller
@RequestMapping("/clients")          // Base path
@RequiredArgsConstructor             // Constructor injection
public class ClientController {

    @GetMapping                       // GET /clients
    @GetMapping("/{id}")              // GET /clients/{id}
    @PostMapping                      // POST /clients

    @ModelAttribute                   // Bind form data
    @Valid                            // Trigger validation
    @PathVariable                     // URL parameter
    @RequestParam                     // Query parameter
}
```

### Service Layer

```java
@Service                              // Spring service bean
@Transactional                        // Transaction management
@RequiredArgsConstructor             // Constructor injection
@Slf4j                               // Logging
public class ClientService {

    @Transactional(readOnly = true)  // Read-only optimization
}
```

### Repository Layer

```java
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Spring Data JPA - no implementation needed

    @Query("SELECT c FROM Client c WHERE...")  // Custom JPQL
    @Param("search")                            // Named parameter
}
```

### Entity Layer

```java
@Entity                               // JPA entity
@Table(name = "clients")             // Table name
@Data                                // Lombok: getters, setters, toString, equals, hashCode
@NoArgsConstructor                   // Lombok: default constructor
@AllArgsConstructor                  // Lombok: all-args constructor
@Builder                             // Lombok: builder pattern
public class Client {

    @Id                               // Primary key
    @GeneratedValue(strategy = IDENTITY)

    @Column(nullable = false)        // Column constraints
    @NotBlank                        // Bean validation
    @Email
    @Min(1)
    @Positive

    @OneToMany                       // Relationship
    @ManyToOne
    @JoinColumn

    @Enumerated(EnumType.STRING)    // Enum storage

    @CreatedDate                     // Audit: creation timestamp
    @LastModifiedDate                // Audit: update timestamp
}
```

---

## Thymeleaf Standards

### Template Structure

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org"
      th:replace="~{fragments/layout :: layout(~{::title}, ~{::content})}">
<head>
    <title>Page Title</title>
</head>
<body>
    <div th:fragment="content">
        <!-- Page-specific content -->
    </div>
</body>
</html>
```

### Common Expressions

```html
<!-- Variable expression -->
<p th:text="${client.nom}">Name</p>

<!-- Message expression (i18n) -->
<p th:text="#{welcome.message}">Welcome</p>

<!-- URL expression -->
<a th:href="@{/clients/{id}(id=${client.id})}">Link</a>

<!-- Fragment expression -->
<div th:replace="~{fragments/header :: header}"></div>

<!-- Conditional rendering -->
<div th:if="${client.isDeleted()}">Deleted</div>
<div th:unless="${client.isDeleted()}">Active</div>

<!-- Iteration -->
<tr th:each="client : ${clients}">
    <td th:text="${client.nom}">Name</td>
</tr>

<!-- Form binding -->
<form th:action="@{/clients}" th:object="${client}" method="post">
    <input type="text" th:field="*{nom}" />
    <span th:errors="*{nom}" class="error"></span>
</form>
```

### XSS Prevention

```html
<!-- ✅ CORRECT - Escapes HTML -->
<p th:text="${userInput}"></p>

<!-- ❌ DANGEROUS - Allows script injection -->
<p th:utext="${userInput}"></p>
```

---

## Testing Standards

### Unit Test Naming

**Convention:** `methodName_shouldExpectedBehavior_whenCondition`

**Examples:**
```java
@Test
void findAllActive_shouldReturnOnlyNonDeletedClients()

@Test
void softDelete_shouldSetDeletedAtTimestamp()

@Test
void updateStatut_shouldThrowException_whenTransitionInvalid()

@Test
void calculateTotal_shouldSumAllLignes()
```

### Test Structure (Given-When-Then)

```java
@Test
void save_shouldSetTimestamps_whenNewClient() {
    // Given
    Client client = Client.builder()
        .nom("Test Client")
        .build();

    // When
    Client saved = clientService.save(client);

    // Then
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getUpdatedAt()).isNotNull();
}
```

### Mocking

```java
@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void findById_shouldReturnClient_whenExists() {
        // Given
        Client client = Client.builder().id(1L).nom("Test").build();
        when(clientRepository.findByIdAndDeletedAtIsNull(1L))
            .thenReturn(Optional.of(client));

        // When
        Optional<Client> result = clientService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNom()).isEqualTo("Test");
        verify(clientRepository).findByIdAndDeletedAtIsNull(1L);
    }
}
```

---

## Error Handling Patterns

### Service Layer Exceptions

```java
// Throw domain exceptions
public Client findById(Long id) {
    return clientRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new ClientNotFoundException("Client #" + id));
}

public void updateStatut(Long id, StatutCommande newStatut) {
    Commande commande = findById(id);
    if (!commande.canTransitionTo(newStatut)) {
        throw new InvalidStatusTransitionException("Invalid transition");
    }
    // ...
}
```

### Controller Layer Error Handling

```java
@Controller
public class ClientController {

    @PostMapping
    public String save(@Valid @ModelAttribute Client client, BindingResult result) {
        if (result.hasErrors()) {
            // Validation errors - return form with errors
            return "clients/form";
        }

        try {
            clientService.save(client);
            redirectAttributes.addFlashAttribute("success", "Client enregistré");
            return "redirect:/clients";
        } catch (Exception e) {
            // Business exceptions - caught by GlobalExceptionHandler
            throw e;
        }
    }
}
```

### Global Exception Handler

```java
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public String handleNotFound(ClientNotFoundException ex, Model model) {
        log.error("Client not found: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericError(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("error", "Une erreur inattendue s'est produite");
        return "error/500";
    }
}
```

---

## Database Query Standards

### Repository Query Methods

**Simple queries:** Use Spring Data JPA method name derivation

```java
// Derived queries - no implementation needed
List<Client> findByDeletedAtIsNullOrderByNomAsc();
Optional<Client> findByIdAndDeletedAtIsNull(Long id);
List<Commande> findByStatut(StatutCommande statut);
List<Achat> findByDateAchatBetween(LocalDate start, LocalDate end);
```

**Complex queries:** Use `@Query` with JPQL

```java
@Query("SELECT c FROM Client c WHERE c.deletedAt IS NULL " +
       "AND (LOWER(c.nom) LIKE LOWER(CONCAT('%', :search, '%')) " +
       "OR c.telephone LIKE CONCAT('%', :search, '%'))")
List<Client> searchClients(@Param("search") String search);

@Query("SELECT new com.honeyai.dto.TopProduitDto(p.nom, SUM(lc.quantite)) " +
       "FROM LigneCommande lc JOIN lc.produit p " +
       "JOIN lc.commande cmd " +
       "WHERE cmd.statut = 'PAYEE' " +
       "GROUP BY p.id ORDER BY SUM(lc.quantite) DESC")
List<TopProduitDto> findTopProducts(@Param("limit") int limit);
```

### Performance Optimization

```java
// Read-only transactions
@Transactional(readOnly = true)
public List<Client> findAllActive() {
    return clientRepository.findByDeletedAtIsNullOrderByNomAsc();
}

// Lazy loading (default)
@ManyToOne(fetch = FetchType.LAZY)
private Client client;

// Eager loading (when always needed)
@ManyToOne(fetch = FetchType.EAGER)
private Produit produit;
```

---

## Configuration Standards

### Property Naming

**Convention:** kebab-case in YAML

```yaml
# ✅ CORRECT
honeyai:
  etiquettes:
    nom-apiculteur: "Exploitation"
    dluo-duree-jours: 730

# ❌ WRONG
honeyai:
  etiquettes:
    nomApiculteur: "Exploitation"
    dluoDureeJours: 730
```

### Configuration Class

```java
@ConfigurationProperties(prefix = "honeyai.etiquettes")
@Validated
@Data
public class EtiquetteConfig {

    @NotBlank
    private String siret;

    @NotBlank
    private String nomApiculteur;

    @Min(1)
    private Integer dluoDureeJours = 730;
}
```

---

## Logging Standards

### Log Levels

| Level | Usage | Example |
|-------|-------|---------|
| ERROR | Exceptions, system failures | `log.error("Failed to generate PDF", ex)` |
| WARN | Recoverable issues, deprecations | `log.warn("Using default price for missing tarif")` |
| INFO | State changes, important events | `log.info("Client {} created", client.getNom())` |
| DEBUG | Detailed flow information | `log.debug("Searching clients with term: {}", search)` |

### Structured Logging

```java
// ✅ CORRECT - Parameterized
log.info("Client {} saved with id {}", client.getNom(), client.getId());

// ❌ WRONG - String concatenation
log.info("Client " + client.getNom() + " saved with id " + client.getId());
```

### Exception Logging

```java
// Log exception with stack trace
try {
    // ...
} catch (Exception e) {
    log.error("Failed to process order {}", orderId, e);  // e goes last
    throw new OrderProcessingException("Order failed", e);
}
```

---

## Version Control Standards

### Commit Messages

**Convention:** `<type>: <short description>`

**Types:**
- `feat:` New feature
- `fix:` Bug fix
- `refactor:` Code restructure (no behavior change)
- `test:` Add or update tests
- `docs:` Documentation changes
- `chore:` Build, dependencies, config

**Examples:**
```
feat: add client soft delete functionality
fix: correct DLUO calculation for 2-year expiry
refactor: extract price lookup to separate method
test: add unit tests for CommandeService status transitions
docs: update README with installation instructions
chore: upgrade Spring Boot to 3.2.2
```

### Branch Strategy

**Main branches:**
- `main` - Production-ready code
- `develop` - Integration branch

**Feature branches:**
- `feature/epic-1-client-management`
- `fix/pdf-accent-rendering`

---

## Performance Guidelines

### Database Optimization

1. **Index frequently queried columns**
   ```java
   @Table(indexes = {
       @Index(name = "idx_client_nom", columnList = "nom"),
       @Index(name = "idx_commande_date", columnList = "date_commande")
   })
   ```

2. **Use read-only transactions**
   ```java
   @Transactional(readOnly = true)
   ```

3. **Avoid N+1 queries**
   ```java
   // Use JOIN FETCH or pagination
   @Query("SELECT c FROM Commande c JOIN FETCH c.lignes")
   ```

### Frontend Optimization

1. **Bootstrap/FontAwesome via CDN** (browser caching)
2. **Thymeleaf cache enabled in production**
3. **Minimal custom CSS/JS**

---

## Security Guidelines

### Input Validation

```java
@Entity
public class Client {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Email(message = "Email invalide")
    private String email;
}

@PostMapping
public String save(@Valid @ModelAttribute Client client, BindingResult result) {
    if (result.hasErrors()) {
        return "clients/form";
    }
    // ...
}
```

### XSS Prevention

- Always use `th:text` (not `th:utext`)
- Spring Data JPA uses parameterized queries (SQL injection protection)

### No Authentication Required

- Single-user family application
- Physical security (parents' PC)
- No network exposure

---

## References

- Spring Boot Best Practices: https://docs.spring.io/spring-boot/docs/current/reference/html/
- Java Code Conventions: https://google.github.io/styleguide/javaguide.html
- Thymeleaf Documentation: https://www.thymeleaf.org/documentation.html

---

**Document Owner:** Architect Agent (Winston)
**Last Updated:** 2026-01-18
**Enforcement:** Code reviews must check compliance with these standards
