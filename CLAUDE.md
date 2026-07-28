# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a documentation example repository for the **ZK Framework** (versions targeting 10.1.0-Eval). It contains 700+ runnable code examples that accompany ZK documentation across multiple guides:
- ZK Developer's Reference
- ZK Component Reference
- ZK MVVM Reference
- ZK Client-side Reference
- ZK Style Customization Guide

**Philosophy**: "Runnable codes speak louder than static code snippets." Examples are meant to be executed and tested directly.

## Repository Structure

The repository is organized as a **Maven multi-module project** where each directory is an independent Maven web application (WAR packaging):

### Main Documentation Projects

- **`componentreference/`** — 751 ZUL files demonstrating ZK UI components (essential, input, container, layout, multimedia, diagram, supplementary)
- **`developersreference/developersreference/`** — Core developer reference examples (MVC, MVVM, security, performance, events, i18n, server push, testing, accessibility)
- **`mvvmreference/`** — MVVM pattern examples (data binding, command binding, validators, converters, templates)
- **`clientreference/`** — Client-side JavaScript/TypeScript examples (widget customization, client API usage)

### Integration Examples

Each is a standalone Maven project in the `developersreference/` directory:
- `integration.spring/` — Spring Framework integration
- `integration.spring.security/` — Spring Security authentication
- `integration.hibernate/` — Hibernate ORM examples
- `integration.jpa/` — JPA persistence examples
- `integration.ejb/` — EJB integration
- `integration.cdi/` — CDI dependency injection

### Specialized Projects

- **`mymodule/`** — Example of creating a reusable ZK module
- **`styleguide/`** — Style customization and theming examples
- **`csp-filter/`** — Content Security Policy security examples
- **`testjmeter/`** — JMeter performance testing setup (deprecated but maintained)

## Build and Run Commands

### Run a Single Project

Each project uses Maven with Jetty plugin for development:

```bash
# Navigate to a project directory, then:
mvn jetty:run

# Access at: http://localhost:8080/{project-artifact-id}
```

Example artifact IDs for URL access:
- `componentreference/` → `http://localhost:8080/component`
- `developersreference/developersreference/` → `http://localhost:8080/developers`
- `mvvmreference/` → `http://localhost:8080/mvvm`
- `clientreference/` → `http://localhost:8080/client`

### TypeScript Compilation (clientreference only)

```bash
cd clientreference
npm i -D typescript zk-types @types/jquery
npx tsc
```

### Maven Clean Build

```bash
mvn clean package
```

## Architecture and Key Patterns

### MVC Pattern (Model-View-Controller)

Located in: `developersreference/developersreference/src/main/webapp/mvc/`

- **Controller** (Java classes): Business logic, event handlers
- **View** (ZUL files): UI definition in XML-based markup
- **Model** (Java POJOs): Data representation

Event flow: User interactions in ZUL → Controller handles → Model updates → View refreshes

### MVVM Pattern (Model-View-ViewModel)

Located in: `mvvmreference/`

- **ViewModel** (Java classes with annotations): `@Command` for user actions, `@NotifyChange` for property changes
- **View** (ZUL files): Declares data bindings with `@bind`, `@load`, `@save`
- **Model** (POJOs): Business objects

Two-way binding: UI changes trigger ViewModel commands → ViewModel modifies model → UI automatically updates via notification

Example:
```java
@Command @NotifyChange("name")
public void updateName() {
    name = "Hello World";
}
```

```xml
<label value="@load(vm.name)"/>
<button label="Click" onClick="@command('updateName')"/>
```

### Component Architecture

- **ZK Components**: Pre-built UI elements (Button, Label, Textbox, etc.)
- **Custom Components**: Defined via `lang-addon.xml` in `src/main/resources/metainfo/zk/`
- **Widget Customization**: Client-side JavaScript/TypeScript overrides in `clientreference/`
- **Module System**: Reusable component packages (see `mymodule/`)

## Technology Stack

### Core

- **Java 11** (source and target)
- **Apache Maven 3.x** — Build automation
- **ZK Framework 10.1.0-Eval** — Primary UI framework
  - zkmax, zuti, zhtml, za11y, zkplus, client-bind modules

### Backend Frameworks

- **Spring Framework 5.3.39** — IoC and integration
- **Spring Security** — Authentication/authorization
- **Hibernate 5.x** — ORM
- **JPA/Persistence** — Database abstraction
- **CDI** — Dependency injection
- **EJB** — Enterprise components

### Frontend

- **TypeScript 5.3.3** — Type-safe JavaScript
- **zk-types 10.0.0** — ZK TypeScript definitions
- **jQuery** — DOM manipulation (ZK dependency)

### Additional

- **Jetty 9.4.54** — Embedded web server (development)
- **SLF4J + Log4j** — Logging
- **Hibernate Validator** — Bean validation
- **MySQL Connector** — Database driver (when needed)
- **CKEditor (ckez)**, **Google Maps (gmapsz)** — Integrated libraries

## File Organization and Naming Conventions

### Folder Naming

Folders correspond to documentation chapters and sections:
- Example: `eventHandling/` → [Event Handling](https://www.zkoss.org/wiki/ZK%20Developer's%20Reference/Event%20Handling) documentation
- Example: `mvc/controller/` → MVC Controller subsection

### File Naming

ZUL files correspond to documentation pages:
- Example: `eventQueue.zul` → [Use Event Queues](https://www.zkoss.org/wiki/ZK%20Developer's%20Reference/UI%20Patterns/Long%20Operations/Use%20Event%20Queues) page
- Example: `mvc/controller/abc.zul` → Specific MVC controller example

This mapping allows tracing example code back to documentation.

### Java Package Organization

- Controllers/ViewModels: `org.zkoss.reference.{section}`
- Models/POJOs: `org.zkoss.reference.{section}.model`
- Utilities: `org.zkoss.reference.util`

## Key Configuration Files

### ZK Application Configuration

**Location**: `src/main/webapp/WEB-INF/zk.xml`

Controls ZK runtime behavior:
```xml
<client-config>
    <debug-js>true</debug-js>  <!-- Enable JavaScript debugging -->
</client-config>
```

### Custom Component Definition

**Location**: `src/main/resources/metainfo/zk/lang-addon.xml`

Registers custom components so they're available in ZUL files:
```xml
<component>
    <name>myComponent</name>
    <class-name>org.zkoss.reference.MyComponent</class-name>
</component>
```

### Logging Configuration

- `src/main/resources/log4j.properties` or `logging.properties`
- Controls log levels for ZK and application classes

### Integration Configurations

- **Spring**: `applicationContext.xml` in `src/main/resources/`
- **JPA**: `persistence.xml` in `src/main/resources/META-INF/`
- **Hibernate**: `hibernate.cfg.xml` or annotations
- **CDI**: `beans.xml` in `src/main/webapp/WEB-INF/`

## Working with Examples

### Adding a New Example

1. **Determine category**: Decide which documentation section the example belongs to
2. **Create ZUL file**: `src/main/webapp/{section}/{page-name}.zul`
3. **Create supporting Java class** (if needed): `src/main/java/org/zkoss/reference/{section}/{ClassName}.java`
4. **Update lang-addon.xml** (for custom components)
5. **Run with**: `mvn jetty:run` and visit the URL
6. **Test manually**: Verify component behavior matches documentation

### Modifying Existing Examples

- Runnable examples are the source of truth — keep them working
- If documentation changes, update examples first
- Test all changes with `mvn jetty:run` before committing

## Testing Approach

This repository prioritizes **runnable, demonstrable code** over automated testing:

- **Manual verification**: Run projects locally with `mvn jetty:run` and test in browser
- **Visual testing**: Verify UI renders correctly and responds to interactions
- **Integration testing examples**: Some projects have test patterns in `testing/` subdirectory (examples, not automated suites)
- **JMeter** (`testjmeter/`): For performance/load testing (reference examples)

No comprehensive unit test suite — focus is on working example code.

## Important Development Notes

### Client-Side Customization

The `clientreference/` project demonstrates ZK widget customization:
- Modify JavaScript behavior via widget overrides
- Use TypeScript for type safety
- Compile TypeScript to JavaScript before running
- Widget customization files: `src/main/webapp/widgetCustomization/`

### Database Integration

Projects requiring database (Hibernate, JPA, Spring):
- Configure connection in `src/main/resources/` config files
- Default: MySQL connectivity available via dependencies
- Modify JDBC URL, username, password in configuration files

### Module Creation and Reuse

See `mymodule/` for pattern of creating reusable ZK modules:
- Package components as independent JAR/WAR
- Use `lang-addon.xml` to define exported components
- Reference in other projects via Maven dependency
- Share common UI patterns across documentation projects

### ZK Version

All projects target **ZK 10.1.0-Eval**:
- Update `<version>` tags in all `pom.xml` files to upgrade versions
- ZK repositories configured in each `pom.xml`
- Evaluation version includes enterprise features (zkmax, etc.)

## Documentation References

- Main Documentation: https://books.zkoss.org/wiki/ZK_Developer%27s_Reference
- Component Reference: https://books.zkoss.org/wiki/ZK_Component_Reference
- MVVM Guide: https://books.zkoss.org/zk-mvvm-book/9.5/index.html
- Client API: https://books.zkoss.org/wiki/ZK_Client-side_Reference
- Styling Guide: https://books.zkoss.org/wiki/ZK_Style_Customization_Guide

## Branch Information

- **master** — Latest ZK version (10.1.0-Eval)
- Other branches may target older ZK versions