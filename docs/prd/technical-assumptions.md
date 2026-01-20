# Technical Assumptions

## Repository Structure

**Type:** Monorepo

**Structure validée:**
```
honeyAI/
├── src/main/java/com/honeyai/
│   ├── HoneyAiApplication.java          # Main Spring Boot
│   ├── config/                          # Configuration classes
│   ├── controller/                      # MVC Controllers
│   ├── service/                         # Business logic layer
│   ├── repository/                      # Spring Data JPA repositories
│   ├── model/                           # JPA entities
│   ├── enums/                           # Enumerations
│   └── exception/                       # Exception handling
├── src/main/resources/
│   ├── application.yml                  # Spring configuration
│   ├── static/                          # CSS, JS, images
│   └── templates/                       # Thymeleaf HTML templates
├── src/test/java/                       # Tests unitaires
├── data/                                # SQLite database (runtime)
├── docs/                                # Documentation
├── launcher/                            # Packaging (launch4j config)
└── pom.xml                              # Maven dependencies
```

## Service Architecture

**Architecture:** Monolith (Spring Boot standalone application)

**Rationale:** Pas de microservices (complexité inutile pour application mono-poste), pattern MVC classique (Controllers → Services → Repositories → Database), server-side rendering (Thymeleaf génère HTML côté serveur).

**Technologies stack:**

| Composant | Technologie | Version | Rationale |
|-----------|-------------|---------|-----------|
| **Runtime** | Java (JRE) | 17 LTS | Support long terme, stabilité |
| **Framework** | Spring Boot | 3.2+ | Framework maîtrisé, productivité maximale |
| **Web** | Spring Web + Thymeleaf | Inclus SB | MVC server-side rendering |
| **ORM** | Spring Data JPA + Hibernate | Inclus SB | Abstraction CRUD automatique |
| **Database** | SQLite | 3.45+ | Fichier unique, zéro configuration |
| **JDBC Driver** | xerial sqlite-jdbc | 3.45.0.0 | Driver JDBC standard |
| **PDF Generation** | Apache PDFBox | 3.0+ | Open source, gratuit, API Java simple |
| **Frontend CSS** | Bootstrap | 5.3+ | Composants UI modernes, responsive |
| **Frontend JS** | JavaScript Vanilla | ES6+ | Interactions légères |
| **Icons** | Font Awesome | 6.4+ | Iconographie cohérente |
| **Build** | Maven | 3.8+ | Gestion dépendances standard |
| **Packaging** | launch4j | 1.7+ | Wrapper .exe Windows |
| **Testing** | Spring Boot Test + JUnit 5 | Inclus SB | Tests unitaires et d'intégration |

## Testing Requirements

**Stratégie:** Unit + Integration (Testing Pyramid partiel)

**Couverture cible:**
- **Unit Tests (JUnit 5 + Mockito):** Services layer avec 80%+ couverture, focus sur calculs critiques (financiers, DLUO, transitions statuts)
- **Integration Tests (Spring Boot Test):** Controllers principaux, requêtes custom repositories, contraintes database
- **Tests manuels critiques:** Génération PDF (conformité visuelle), workflow complet commandes, backup automatique, performance, UX avec parents (2-4 semaines)
- **Pas de tests E2E automatisés:** Coût/maintenance vs. bénéfice défavorable pour projet familial

## Additional Technical Assumptions and Requests

**Database & Data Management:**
- Hibernate ddl-auto: update (pas de migrations Flyway/Liquibase pour MVP)
- Indexes SQLite sur colonnes recherchées fréquemment (clients.nom, commandes.date_commande)
- Transactions @Transactional Spring pour opérations multi-tables
- Isolation niveau READ_COMMITTED (mono-utilisateur)

**Security:**
- Pas d'authentification (application mono-utilisateur familial)
- Pas de chiffrement données (locales uniquement)
- Validation inputs via Spring Validation (@NotBlank, @Min, etc.)

**Logging & Monitoring:**
- Logback niveau INFO en production, DEBUG en dev
- Logs vers ./logs/honeyai.log avec rotation quotidienne (max 7 jours)
- Simple endpoint /actuator/health pour health check

**Error Handling:**
- @ControllerAdvice global exception handler pour pages erreur conviviales
- Try-catch larges pour éviter plantages
- Exceptions custom (ClientNotFoundException, InvalidCommandeStateException, etc.)

**PDF Generation:**
- Apache PDFBox 3.x
- Dimensions étiquettes configurables dans application.yml
- Police Arial ou système (pas de fonts custom)
- Format papier A4 standard

**Backup Strategy:**
- @Scheduled(cron = "0 0 2 * * ?") pour backup quotidien 2h du matin
- Destination ./backups/honeyai-backup-YYYY-MM-DD-HHmmss.db
- Rétention 30 jours, suppression automatique backups anciens
- Export manuel via bouton UI

**Internationalization:**
- Français uniquement pour MVP
- Format dates DD/MM/YYYY
- Nombres: séparateur décimal virgule (1 234,56 €)
- Devise Euro (€) hardcodé

**Browser Compatibility:**
- Pas de support IE11 (navigateurs modernes uniquement)
- Bootstrap 5 + CSS custom minimal
- JavaScript ES6+ (pas de transpilation Babel)

**Packaging & Distribution:**
- JAR exécutable via Spring Boot Maven Plugin
- Wrapper .exe avec launch4j (icon, splash screen, console cachée)
- JRE 17 embarqué (optionnel mais recommandé) dans distribution
- Pas d'installeur Windows complexe pour MVP - copie dossier suffit
- Pas de système mise à jour auto - mise à jour manuelle

---
