# Pruebas de Integración - Registro de Votantes

## Descripción

Este proyecto implementa un sistema de registro de votantes desarrollado en Java utilizando Maven. El objetivo principal es validar reglas de negocio relacionadas con la inscripción de ciudadanos y demostrar diferentes estrategias de pruebas automatizadas:

* Pruebas unitarias.
* Pruebas con dobles de prueba (Mockito).
* Pruebas con Fake Repository.
* Pruebas de integración con base de datos H2.
* Integración Continua (CI) con GitHub Actions.

---

# Arquitectura del Proyecto

El proyecto está organizado siguiendo principios de Arquitectura Hexagonal (Ports & Adapters).

```text
src/main/java
│
├── application
│   ├── port
│   └── usecase
│
├── domain
│   └── model
│
├── infrastructure
│   └── persistence
│
└── delivery
    └── rest
```

## Componentes principales

### Domain

Contiene las entidades y reglas del negocio:

* Person
* Gender
* RegisterResult

### Application

Contiene los casos de uso:

* Registry

Y los puertos:

* RegistryRepositoryPort

### Infrastructure

Implementa la persistencia:

* RegistryRepository

### Delivery

Expone funcionalidades mediante REST:

* RegistryController
* PersonRequest

---

# Flujo de Registro

El proceso de registro de votantes sigue la siguiente secuencia:

```text
Persona
   │
   ▼
Registry.registerVoter()
   │
   ├─ Validar documento
   ├─ Validar edad
   ├─ Validar estado de vida
   ├─ Verificar duplicados
   │
   ▼
RegistryRepository
   │
   ▼
Resultado de Registro
```

---

# Tecnologías Utilizadas

* Java 17
* Maven
* JUnit 4
* JUnit 5
* Mockito 5
* Spring Boot 3.5
* H2 Database
* GitHub Actions

---

# Estrategia de Pruebas

El proyecto implementa tres niveles principales de pruebas.

## 1. Pruebas Unitarias con Mockito

Archivo:

```text
RegistryWithMockTest.java
```

Estas pruebas aíslan completamente el caso de uso `Registry` mediante un mock del repositorio.

### Casos cubiertos

#### Registro duplicado

Verifica que:

* El repositorio indique que el ID ya existe.
* El resultado sea `DUPLICATED`.
* No se invoque el método `save()`.

#### Error del repositorio

Verifica que:

* El repositorio genere una excepción.
* La excepción sea propagada correctamente.

#### Registro válido

Verifica que:

* El votante sea registrado exitosamente.
* Se invoque una única vez el método `save()`.

---

## 2. Pruebas con Fake Repository

Archivo:

```text
FakeRepositoryTest.java
```

Estas pruebas utilizan un repositorio falso en memoria para validar el comportamiento del caso de uso sin depender de una base de datos real.

### Casos cubiertos

#### Registro exitoso

Valida que un ciudadano válido sea registrado.

#### Registro duplicado

Valida la detección de registros repetidos.

#### Eliminación de registros

Valida el funcionamiento de `deleteAll()`.

---

## 3. Pruebas de Integración

Archivo:

```text
RegistryTest.java
```

Estas pruebas utilizan:

```text
H2 Database
```

para verificar la integración entre:

* Caso de uso.
* Repositorio.
* Persistencia.

### Casos cubiertos

#### Registro válido

Resultado esperado:

```text
VALID
```

#### Registro duplicado

Resultado esperado:

```text
DUPLICATED
```

#### Menor de edad

Resultado esperado:

```text
UNDERAGE
```

#### Persona fallecida

Resultado esperado:

```text
DEAD
```

#### Documento inválido

Resultado esperado:

```text
INVALID
```

#### Error de conexión

Se verifica el comportamiento cuando la conexión a la base de datos es inválida.

---

# Resultados de las Pruebas

Al ejecutar:

```bash
mvn clean test
```

se obtienen:

```text
Tests run: 13
Failures: 0
Errors: 0
Skipped: 0
```

Lo que confirma que todas las pruebas implementadas son exitosas.

---

# Integración Continua (CI)

El proyecto incorpora automatización mediante GitHub Actions.

Archivo:

```text
.github/workflows/ci.yml
```

## Disparadores

El pipeline se ejecuta automáticamente en:

* Push a:

  * main
  * master
  * develop

* Pull Request hacia:

  * main
  * master
  * develop

---

## Flujo de Ejecución

```text
Push / Pull Request
          │
          ▼
GitHub Actions
          │
          ▼
Checkout Repository
          │
          ▼
Configurar JDK 17
          │
          ▼
mvn verify
          │
          ▼
Pruebas Unitarias
          │
          ▼
Pruebas de Integración
          │
          ▼
Resultado Final
```

---

## Validación de Calidad

El pipeline ejecuta:

```bash
mvn verify
```

Si alguna prueba falla:

* El job falla.
* El pipeline se marca como fallido.
* El cambio no cumple los criterios de calidad establecidos.

---

# Ejecución Local

## Compilar el proyecto

```bash
mvn clean compile
```

## Ejecutar pruebas

```bash
mvn clean test
```

## Ejecutar validación completa

```bash
mvn verify
```

---

# Estructura de Pruebas

```text
src/test/java

├── AppTest
│
└── registry
    └── application
        └── usecase

            ├── RegistryTest
            ├── RegistryWithMockTest
            ├── FakeRepositoryTest
            └── FakeRepository
```

---

# Dependencias Principales

* JUnit Jupiter 5.11
* JUnit 4.13.2
* Mockito Core 5.12
* Spring Boot Starter Web 3.5
* Spring Boot Starter Test 3.5
* H2 Database 2.2.224
* Lombok 1.18.34

---

# Conclusiones

Este proyecto demuestra la aplicación práctica de diferentes estrategias de aseguramiento de calidad de software:

* Pruebas unitarias mediante Mockito.
* Uso de dobles de prueba tipo Fake.
* Pruebas de integración con base de datos H2.
* Automatización de validaciones mediante GitHub Actions.
* Ejecución continua de pruebas con Maven.

La combinación de estas técnicas permite detectar defectos tempranamente y garantizar la estabilidad del proceso de registro de votantes.


# Autor

**Sergio Cruz** /
sergiocrtr@unisabana.edu.co