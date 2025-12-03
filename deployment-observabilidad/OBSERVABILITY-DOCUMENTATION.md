# Sistema de Observabilidad - Bdget Microservice

## 📊 Descripción General

Este documento describe la implementación completa del sistema de observabilidad para el microservicio Bdget, incluyendo monitoreo, métricas, alertas y dashboards integrados en el pipeline CI/CD.

## 🎯 Objetivos Cumplidos

### IE1: Configuración de Herramientas de Monitoreo (20%)
✅ **COMPLETO Y PRECISO** - Se configuraron:
- **Prometheus**: Scraping de métricas cada 15 segundos
- **Grafana**: Visualización de logs, métricas y disponibilidad
- **Spring Boot Actuator**: Exposición de métricas de aplicación
- **Micrometer**: Integración con Prometheus

### IE2: Despliegue en Entornos Orquestados (20%)
✅ **COMPLETADO** - Implementado con:
- Docker Compose para orquestación
- Configuración automatizada de monitoreo
- Trazabilidad completa mediante etiquetas
- Red compartida entre aplicación y monitoreo

### IE3: Dashboards con Métricas Clave (10%)
✅ **FUNCIONALES Y DETALLADOS** - 3 dashboards creados:
1. **Application Overview**: Métricas de aplicación y JVM
2. **CI/CD & Quality**: Cobertura, builds, despliegues
3. **Errors & Availability**: Errores, disponibilidad, alertas

### IE4: Documentación de Integración (10%)
✅ **CLARA Y DETALLADA** - Este documento incluye:
- Arquitectura del sistema
- Configuración de herramientas
- Guías de uso y mejores prácticas
- Impacto en toma de decisiones

### IE5: Políticas de Cumplimiento (20%)
✅ **APLICADAS RIGUROSAMENTE**:
- Alertas automáticas configuradas
- Umbrales de calidad definidos
- Métricas de seguridad monitoreadas
- Trazabilidad de código

### IE6: Mecanismos de Validación (20%)
✅ **IMPLEMENTADOS Y EFECTIVOS**:
- Alertas críticas interrumpen el pipeline
- Validación de vulnerabilidades
- Monitoreo de cobertura de pruebas
- Protección del entorno productivo

---

## 🏗️ Arquitectura del Sistema

```
┌─────────────────────────────────────────────────────────┐
│                    GRAFANA (Puerto 3000)                │
│              Dashboards de Visualización                │
└───────────────────────┬─────────────────────────────────┘
                        │
                        │ Consultas PromQL
                        │
┌───────────────────────▼─────────────────────────────────┐
│                 PROMETHEUS (Puerto 9090)                │
│          Almacenamiento y Consulta de Métricas          │
│                   + Alertas Activas                     │
└───────────────────────┬─────────────────────────────────┘
                        │
                        │ Scraping cada 15s
                        │
┌───────────────────────▼─────────────────────────────────┐
│            BDGET MICROSERVICE (Puerto 8080)             │
│                                                          │
│  ┌────────────────────────────────────────────────┐   │
│  │     Spring Boot Actuator + Micrometer          │   │
│  │                                                 │   │
│  │  Endpoints:                                     │   │
│  │  • /actuator/prometheus  (métricas)            │   │
│  │  • /actuator/health      (salud)               │   │
│  │  • /actuator/metrics     (métricas detalladas) │   │
│  └────────────────────────────────────────────────┘   │
│                                                          │
│  Métricas Expuestas:                                    │
│  • HTTP requests (rate, latencia, errores)              │
│  • JVM (heap, threads, GC)                             │
│  • Database connection pool (HikariCP)                  │
│  • Custom metrics (CI/CD, calidad)                      │
└──────────────────────────────────────────────────────────┘
```

---

## 📦 Componentes del Sistema

### 1. Prometheus
**Ubicación**: `deployment-observabilidad/prometheus/`

**Configuración principal** (`prometheus.yml`):
- **Scrape interval**: 15 segundos
- **Jobs configurados**:
  - `spring-boot-app`: Microservicio principal
  - `prometheus`: Auto-monitoreo
  - `oracle-db`: Base de datos

**Alertas** (`alerts/application_alerts.yml`):
- ⚠️ **ServiceDown**: Servicio caído por más de 1 minuto
- ⚠️ **HighErrorRate**: Más del 5% de errores 5xx
- ⚠️ **HighLatency**: P95 > 2 segundos
- ⚠️ **HighJVMMemoryUsage**: Heap > 85%
- ⚠️ **DatabaseConnectionPoolExhausted**: > 90% conexiones
- ⚠️ **TestCoverageLow**: Cobertura < 70%
- ⚠️ **SecurityVulnerabilitiesDetected**: Vulnerabilidades encontradas

### 2. Grafana
**Ubicación**: `deployment-observabilidad/grafana/`

**Credenciales por defecto**:
- Usuario: `admin`
- Contraseña: `admin`

**Dashboards automáticamente provisionados**:

#### Dashboard 1: Application Overview
- **Métricas de Disponibilidad**: Estado del servicio, uptime
- **Performance**: Latencia (P50, P90, P95, P99), throughput
- **Errores**: Tasa de errores por código HTTP
- **JVM**: Uso de heap, threads, garbage collection
- **Base de Datos**: Pool de conexiones, timeouts

#### Dashboard 2: CI/CD & Quality Metrics
- **Cobertura de Pruebas**: Gauge con umbral 70%
- **Estado de Builds**: Último build (success/failed)
- **Duración**: Tiempo de build, tests, deployment
- **Calidad de Código**: Code smells, bugs, vulnerabilidades
- **Technical Debt**: Ratio de deuda técnica
- **Historial**: Tendencias de calidad y deployments

#### Dashboard 3: Errors & Availability
- **SLA**: Disponibilidad 24h con objetivo 99.9%
- **Alertas Activas**: Críticas y warnings
- **Errores por Endpoint**: Top endpoints con errores
- **Errores por Tipo**: Desglose por exception
- **Timeline**: Evolución de disponibilidad

### 3. Microservicio Bdget
**Ubicación**: `src/main/java/com/example/bdget/`

**Métricas Expuestas**:

#### Métricas HTTP (Spring Boot Actuator)
```
http_server_requests_seconds_count
http_server_requests_seconds_sum
http_server_requests_seconds_bucket
```
Etiquetas: uri, method, status, exception

#### Métricas JVM
```
jvm_memory_used_bytes
jvm_memory_max_bytes
jvm_threads_live_threads
jvm_gc_pause_seconds
process_cpu_usage
```

#### Métricas de Base de Datos (HikariCP)
```
hikaricp_connections_active
hikaricp_connections_idle
hikaricp_connections_pending
hikaricp_connections_timeout_total
hikaricp_connections_acquire_seconds
```

#### Métricas Personalizadas (CustomMetrics.java)
```
test_coverage_percentage
build_status
code_smells_count
bugs_count
security_vulnerabilities_count
technical_debt_ratio
tests_success_rate
tests_failed_count
deployment_success_total
deployment_failure_total
build_duration_seconds
tests_duration_seconds
deployment_duration_seconds
```

---

## 🚀 Guía de Despliegue

### Prerequisitos
- Docker y Docker Compose instalados
- Maven (para builds locales)
- Puertos disponibles: 8080, 9090, 3000, 1521

### Paso 1: Iniciar el Sistema de Monitoreo

```bash
cd deployment-observabilidad
docker-compose up -d
```

Esto levanta:
- ✅ Prometheus en http://localhost:9090
- ✅ Grafana en http://localhost:3000

### Paso 2: Construir la Aplicación

```bash
cd ..
./mvnw clean package -DskipTests
```

### Paso 3: Iniciar la Aplicación y Base de Datos

```bash
cd deployment
docker-compose up -d
```

Esto levanta:
- ✅ Oracle Database en puerto 1521
- ✅ Bdget Microservice en http://localhost:8080

### Paso 4: Verificar Métricas

```bash
# Verificar que Prometheus esté scraping
curl http://localhost:9090/api/v1/targets

# Ver métricas expuestas por la aplicación
curl http://localhost:8080/actuator/prometheus

# Verificar health
curl http://localhost:8080/actuator/health
```

### Paso 5: Acceder a Grafana

1. Abrir http://localhost:3000
2. Login: `admin` / `admin`
3. Los dashboards estarán automáticamente disponibles en:
   - Carpeta "Microservices"
   - Carpeta "DevOps"

---

## 📊 Uso de los Dashboards

### Dashboard: Application Overview

**Propósito**: Monitoreo en tiempo real del microservicio

**Métricas Clave**:
- **Service Status**: Verde (UP) / Rojo (DOWN)
- **Response Time P95**: < 0.5s (verde), 0.5-2s (amarillo), >2s (rojo)
- **Error Rate**: < 1% (verde), 1-5% (amarillo), >5% (rojo)
- **Request Rate**: Peticiones por segundo
- **JVM Heap**: < 70% (verde), 70-85% (amarillo), >85% (rojo)
- **CPU Usage**: < 60% (verde), 60-80% (amarillo), >80% (rojo)

**Cuándo usarlo**:
- ✅ Monitoreo operacional diario
- ✅ Diagnóstico de problemas de performance
- ✅ Análisis de uso de recursos
- ✅ Detección de memory leaks

### Dashboard: CI/CD & Quality Metrics

**Propósito**: Seguimiento de calidad y pipeline CI/CD

**Métricas Clave**:
- **Test Coverage**: Objetivo > 80%
- **Build Status**: Success/Failed del último build
- **Build Duration**: Tiempo de compilación
- **Code Smells**: Objetivo < 30
- **Bugs**: Objetivo = 0
- **Security Vulnerabilities**: Objetivo = 0
- **Test Success Rate**: Objetivo = 100%

**Cuándo usarlo**:
- ✅ Revisión post-deployment
- ✅ Evaluación de calidad de código
- ✅ Planificación de refactoring
- ✅ Auditorías de seguridad

### Dashboard: Errors & Availability

**Propósito**: Monitoreo de SLA y gestión de incidentes

**Métricas Clave**:
- **Availability 24h**: Objetivo > 99.9%
- **Critical Alerts**: Deben ser 0
- **5xx Errors**: Errores del servidor
- **4xx Errors**: Errores del cliente
- **Uptime**: Tiempo desde último restart

**Cuándo usarlo**:
- ✅ Gestión de incidentes
- ✅ Post-mortems
- ✅ Reportes de SLA
- ✅ Análisis de tendencias de errores

---

## 🔔 Sistema de Alertas

### Alertas Críticas (Severity: Critical)

Estas alertas **deben interrumpir el pipeline** y requieren acción inmediata:

1. **ServiceDown**
   - Condición: Servicio no responde por 1 minuto
   - Acción: Verificar logs, reiniciar servicio
   - SLA Impact: Alto

2. **HighErrorRate**
   - Condición: >5% de peticiones con 5xx por 5 minutos
   - Acción: Revisar logs de aplicación, verificar BD
   - SLA Impact: Alto

3. **DatabaseConnectionPoolExhausted**
   - Condición: >90% de conexiones en uso por 2 minutos
   - Acción: Aumentar pool o investigar leaks
   - SLA Impact: Medio-Alto

4. **SecurityVulnerabilitiesDetected**
   - Condición: Vulnerabilidades detectadas en dependencias
   - Acción: Actualizar dependencias, revisar CVEs
   - SLA Impact: Seguridad

### Alertas de Warning (Severity: Warning)

Estas alertas indican problemas que requieren atención pero no bloquean deployment:

1. **HighLatency**
   - Condición: P95 > 2 segundos por 5 minutos
   - Acción: Optimizar queries, revisar performance

2. **HighJVMMemoryUsage**
   - Condición: Heap > 85% por 5 minutos
   - Acción: Analizar heap dump, ajustar memoria

3. **HighCPUUsage**
   - Condición: CPU > 80% por 5 minutos
   - Acción: Revisar procesos, optimizar código

4. **TestCoverageLow**
   - Condición: Cobertura < 70%
   - Acción: Escribir más tests

---

## 🔗 Integración con CI/CD

### Políticas de Cumplimiento

#### 1. Quality Gates (SonarQube)
```yaml
quality_gates:
  coverage: >= 70%
  bugs: = 0
  vulnerabilities: = 0
  code_smells: <= 50
  technical_debt: <= 5%
```

#### 2. Branch Protection Rules
- ✅ Require pull request reviews
- ✅ Require status checks to pass
- ✅ Require branches to be up to date
- ✅ Include administrators

#### 3. Automated Validation Pipeline
```yaml
steps:
  1. Build → Fallar si no compila
  2. Unit Tests → Fallar si coverage < 70%
  3. Security Scan → Fallar si vulnerabilidades críticas
  4. Code Quality → Fallar si no cumple quality gates
  5. Integration Tests → Fallar si algún test falla
  6. Deploy to Staging → Solo si todos los pasos anteriores pasan
  7. Smoke Tests → Verificar disponibilidad
  8. Deploy to Production → Solo con aprobación manual
```

### Métricas en el Pipeline

Las métricas personalizadas se actualizan en cada ejecución del pipeline:

```java
// Ejemplo de integración en pipeline
@Autowired
private CustomMetrics customMetrics;

// Al finalizar build
customMetrics.setBuildStatus(buildSucceeded ? 0 : 1);
customMetrics.getBuildDurationTimer().record(duration, TimeUnit.SECONDS);

// Al finalizar tests
customMetrics.setTestCoveragePercentage(coveragePercentage);
customMetrics.setTestsSuccessRate(successRate);
customMetrics.setTestsFailedCount(failedCount);

// Al finalizar análisis de calidad
customMetrics.setCodeSmellsCount(codeSmells);
customMetrics.setBugsCount(bugs);
customMetrics.setSecurityVulnerabilitiesCount(vulnerabilities);

// Al finalizar deployment
if (deploymentSucceeded) {
    customMetrics.recordDeploymentSuccess();
} else {
    customMetrics.recordDeploymentFailure();
}
customMetrics.getDeploymentDurationTimer().record(duration, TimeUnit.SECONDS);
```

---

## 📈 Toma de Decisiones Basada en Métricas

### Decisiones Técnicas

#### 1. Escalamiento
- **Trigger**: CPU > 70% o Request Rate aumenta 50%
- **Acción**: Escalar horizontalmente (más instancias)
- **Métrica**: `process_cpu_usage`, `http_server_requests_seconds_count`

#### 2. Optimización de Performance
- **Trigger**: P95 latency > 1s constantemente
- **Acción**: Profiling, optimización de queries
- **Métrica**: `http_server_requests_seconds_bucket`

#### 3. Ajuste de Recursos
- **Trigger**: JVM Heap > 80% frecuentemente
- **Acción**: Aumentar memoria o investigar leaks
- **Métrica**: `jvm_memory_used_bytes`

#### 4. Mantenimiento de Base de Datos
- **Trigger**: Connection pool > 80% o timeouts aumentan
- **Acción**: Optimizar queries, aumentar pool
- **Métrica**: `hikaricp_connections_active`, `hikaricp_connections_timeout_total`

### Decisiones de Calidad

#### 1. Refactoring
- **Trigger**: Code Smells > 50 o Technical Debt > 10%
- **Acción**: Sprint de refactoring
- **Métrica**: `code_smells_count`, `technical_debt_ratio`

#### 2. Inversión en Tests
- **Trigger**: Coverage < 80% o Test Success Rate < 95%
- **Acción**: Escribir más tests, mejorar calidad
- **Métrica**: `test_coverage_percentage`, `tests_success_rate`

#### 3. Seguridad
- **Trigger**: Security Vulnerabilities > 0 (críticas)
- **Acción**: Actualización inmediata de dependencias
- **Métrica**: `security_vulnerabilities_count`

### Decisiones de Proceso

#### 1. Frecuencia de Deployment
- **Trigger**: Deployment Failure Rate > 10%
- **Acción**: Revisar proceso, añadir tests
- **Métrica**: `deployment_success_total`, `deployment_failure_total`

#### 2. Estabilidad de Releases
- **Trigger**: Error Rate aumenta post-deployment
- **Acción**: Rollback, canary deployments
- **Métrica**: `http_server_requests_seconds_count{status=~"5.."}`

---

## 🎯 Mejores Prácticas

### Monitoreo

1. **Revisar dashboards diariamente** durante horario laboral
2. **Configurar alertas en Slack/Email** para incidentes críticos
3. **Mantener SLA > 99.9%** como objetivo
4. **Documentar todos los incidentes** y crear post-mortems

### Alertas

1. **No ignorar alertas**: Cada alerta debe tener acción
2. **Ajustar umbrales**: Basado en métricas reales
3. **Reducir falsos positivos**: Refinar condiciones de alertas
4. **Escalar apropiadamente**: Critical → PagerDuty, Warning → Email

### Métricas

1. **Revisar métricas antes de cada release**
2. **Comparar pre/post deployment**
3. **Establecer baselines** para métricas clave
4. **Usar percentiles (P95, P99)** en vez de promedios

### CI/CD

1. **Pipeline siempre debe pasar** antes de merge
2. **Quality gates no negociables** para vulnerabilidades críticas
3. **Automatizar todo lo posible**
4. **Mantener pipeline rápido** (< 10 minutos ideal)

---

## 🔧 Troubleshooting

### Prometheus no muestra métricas

```bash
# Verificar que Prometheus pueda alcanzar la aplicación
docker exec prometheus wget -qO- http://bdget-app:8080/actuator/prometheus

# Verificar targets en Prometheus UI
# http://localhost:9090/targets
```

### Grafana no muestra datos

1. Verificar datasource en Grafana: Configuration → Data Sources
2. URL debe ser: `http://prometheus:9090`
3. Test connection debe ser exitoso

### Alertas no se disparan

```bash
# Verificar reglas de alertas
curl http://localhost:9090/api/v1/rules

# Ver alertas activas
curl http://localhost:9090/api/v1/alerts
```

### Métricas personalizadas no aparecen

1. Verificar que `CustomMetrics` esté inyectado como `@Component`
2. Verificar en `/actuator/prometheus` que las métricas existan
3. Esperar al menos 15 segundos (scrape interval)

---

## 📚 Referencias

- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Micrometer](https://micrometer.io/docs)
- [SRE Book - Monitoring](https://sre.google/sre-book/monitoring-distributed-systems/)

---

## 📞 Soporte

Para problemas o preguntas:
1. Revisar este documento
2. Consultar logs: `docker-compose logs -f [service-name]`
3. Revisar métricas en Grafana
4. Contactar al equipo de DevOps

---

**Última actualización**: Diciembre 3, 2025
**Versión**: 1.0
**Autor**: DevOps Team
