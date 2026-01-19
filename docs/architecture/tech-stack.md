# HoneyAI Technology Stack

**Version:** 1.0
**Date:** 2026-01-18
**Source:** Extracted from Architecture Document v1.0
**Status:** Complete - Single Source of Truth

---

## Overview

This is the **single source of truth** for all technology decisions in HoneyAI. All development must use these exact technologies and versions. Any deviations require architecture review and PRD update.

---

## Technology Stack Table

| Category | Technology | Version | Purpose | Rationale |
|----------|------------|---------|---------|-----------|
| **Runtime** | Java JRE | 21 LTS | Runtime environment for Spring Boot application | Long-term support until 2029; performance improvements over Java 11; required for Spring Boot 3.x; mature and stable for production |
| **Framework** | Spring Boot | 3.5.x | Core application framework and dependency injection | Industry standard; developer's primary expertise; embedded Tomcat eliminates server setup; auto-configuration reduces boilerplate; comprehensive ecosystem |
| **Web MVC** | Spring Web MVC | 6.1.x (included) | HTTP request handling and REST endpoints | Standard Spring Boot web stack; familiar MVC pattern; integrates seamlessly with Thymeleaf; supports PDF binary responses |
| **Template Engine** | Thymeleaf | 3.1.x (included) | Server-side HTML template rendering | Natural templating (valid HTML); Spring Boot integration; fragment reusability; no JavaScript framework learning curve |
| **ORM** | Spring Data JPA | 3.5.x (included) | Data access abstraction layer | Eliminates repository boilerplate; query derivation from method names; reduces SQL writing by 80%; pagination support |
| **JPA Provider** | Hibernate ORM | 6.4.x (included) | JPA implementation and database abstraction | Industry standard JPA provider; Spring Boot default; mature SQLite integration via community dialects |
| **Database** | SQLite | 3.45+ | Embedded relational database | Zero-configuration; single file backup; ACID compliant; no server process; 1MB footprint; perfect for desktop apps |
| **JDBC Driver** | xerial sqlite-jdbc | 3.45.0.0 | SQLite JDBC connectivity | Official SQLite JDBC driver; Maven Central availability; actively maintained; handles SQLite-specific features |
| **Hibernate Dialect** | hibernate-community-dialects | 6.4.x | SQLite dialect for Hibernate | Bridges Hibernate ORM to SQLite; handles SQLite SQL syntax differences; enables JPA annotations on SQLite |
| **Validation** | Spring Boot Starter Validation | 3.5.x (included) | Bean validation with Hibernate Validator | JSR-380 annotations (@NotBlank, @Min, @Positive); automatic controller validation; localized error messages |
| **PDF Generation** | Apache PDFBox | 3.0.1 | PDF document creation for honey labels | Pure Java; no external dependencies; embeddable fonts; precise coordinate control; open-source with active community |
| **Frontend CSS** | Bootstrap | 5.3.2 (CDN) | Responsive UI component library | Modern design; mobile-first responsive grid; large button components; accessibility features; no custom CSS framework needed |
| **Frontend JS** | Vanilla JavaScript | ES6+ | Dynamic form interactions | No build step; fast page loads; progressive enhancement; adequate for add/remove product lines; modern browser support |
| **Icons** | Font Awesome | 6.4.2 (CDN) | Icon library for UI elements | Comprehensive icon set; web font delivery; recognizable standard icons; no SVG management needed |
| **Build Tool** | Maven | 3.8+ | Dependency management and build automation | Spring Boot standard; familiar to developer; mature ecosystem; Maven Central repository access; POM-based configuration |
| **Bundler** | Spring Boot Maven Plugin | 3.5.x | Executable JAR packaging | Creates fat JAR with embedded Tomcat; single deployable artifact; manifest configuration for main class |
| **Packaging** | launch4j | 3.50 | Windows .exe wrapper generation | Cross-platform build support; splash screen; custom icon; JRE bundling; version info embedding; no installer complexity |
| **CI/CD** | Git + Manual Build | N/A | Version control and release process | GitHub for source control; manual Maven build for releases; no automated CI needed for family project; simple deployment |
| **Development Tools** | Spring Boot DevTools | 3.2.x (included) | Hot reload and development utilities | Automatic restart on code changes; LiveReload integration; faster development cycle; disabled in production |
| **Testing Framework** | JUnit 5 (Jupiter) | 5.10.x (included) | Unit test framework | Modern test framework; parameterized tests; Spring Boot integration; IDE support |
| **Mocking** | Mockito | 5.x (included) | Mock objects for unit tests | Service layer testing with mocked repositories; Spring Boot Test includes; familiar API |
| **Integration Testing** | Spring Boot Test | 3.2.x (included) | Integration test support | @WebMvcTest for controllers; @DataJpaTest for repositories; embedded database testing; application context loading |
| **Monitoring** | Spring Boot Actuator | 3.5.x (included) | Application health and metrics | /actuator/health endpoint; basic monitoring; graceful shutdown support; minimal overhead |
| **Logging Framework** | Logback | 1.4.x (included) | Logging implementation | SLF4J binding; Spring Boot default; file appenders; rolling policies; pattern configuration |
| **Utility Library** | Lombok | 1.18.30 | Boilerplate code reduction | @Data for getters/setters; @Builder for entities; @Slf4j for logging; reduces code by ~30% |

---

## Maven Dependencies (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.1</version>
    </parent>

    <groupId>com.honeyai</groupId>
    <artifactId>honeyai</artifactId>
    <version>1.0.0</version>
    <name>HoneyAI</name>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Spring Boot Core -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.45.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-community-dialects</artifactId>
        </dependency>

        <!-- PDF Generation -->
        <dependency>
            <groupId>org.apache.pdfbox</groupId>
            <artifactId>pdfbox</artifactId>
            <version>3.0.1</version>
        </dependency>

        <!-- Utilities -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Development Tools -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Application Configuration (application.yml)

```yaml
server:
  port: 8080
  shutdown: graceful

spring:
  application:
    name: HoneyAI

  profiles:
    active: dev

  datasource:
    url: jdbc:sqlite:./data/honeyai.db
    driver-class-name: org.sqlite.JDBC

  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.community.dialect.SQLiteDialect
        format_sql: true
        show_sql: false

  thymeleaf:
    cache: false  # Dev: false, Prod: true

honeyai:
  etiquettes:
    siret: "12345678901234"
    nom-apiculteur: "Exploitation Apicole Familiale"
    adresse: "123 Rue de la Ruche, 12345 Village, France"
    telephone: "06 12 34 56 78"
    dluo-duree-jours: 730
    label-width-mm: 60.0
    label-height-mm: 40.0
    labels-per-row: 3
    labels-per-column: 7

  backup:
    enabled: true
    directory: ./backups
    retention-days: 30
    schedule: "0 0 2 * * ?"

logging:
  level:
    root: INFO
    com.honeyai: DEBUG
  file:
    name: ./logs/honeyai.log
  logback:
    rollingpolicy:
      max-file-size: 10MB
      max-history: 7
```

---

## Frontend Technologies

### CDN Links

**Bootstrap 5.3.2:**
```html
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
```

**Font Awesome 6.4.2:**
```html
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
```

### Custom Assets

- **CSS:** `src/main/resources/static/css/custom.css`
- **JavaScript:** `src/main/resources/static/js/forms.js`
- **Images:** `src/main/resources/static/images/`

---

## Key Architectural Decisions

### Why Spring Boot?
- Developer's primary expertise
- Rapid development (40-60 hours target)
- Embedded Tomcat (no separate server setup)
- Comprehensive ecosystem

### Why SQLite?
- Zero configuration
- Single file database (easy backup)
- No server process
- Perfect for desktop applications

### Why Thymeleaf (not React/Vue)?
- Server-side rendering (simpler, faster initial load)
- No JavaScript build step
- Developer familiar with Spring MVC
- No need for complex state management

### Why Apache PDFBox?
- Pure Java library
- No external service dependency
- Offline capability
- Fine-grained control over PDF layout

### Why Bootstrap CDN (not local)?
- Browser caching benefits
- Always up-to-date
- No local asset management
- Application is offline-first but Bootstrap CDN is loaded once

---

## Version Constraints

**Java:** Must be 21 LTS (not 11)
**Spring Boot:** Must be 3.5.x (not 2.x)
**SQLite:** Must be 3.45+ (for better JSON support)
**PDFBox:** Must be 3.0.x (not 2.x - API changed)

---

## References

- Spring Boot Documentation: https://docs.spring.io/spring-boot/docs/3.2.x/reference/htmlsingle/
- SQLite Documentation: https://www.sqlite.org/docs.html
- Apache PDFBox Guide: https://pdfbox.apache.org/
- Bootstrap 5 Documentation: https://getbootstrap.com/docs/5.3/

---

**Document Owner:** Architect Agent (Winston)
**Last Updated:** 2026-01-18
**Change Control:** Any tech stack changes require architecture review
