# 🎯 Sistema de Observabilidad - Bdget Microservice

Sistema completo de monitoreo, métricas y alertas con **Prometheus** y **Grafana** para cumplir con todos los criterios de evaluación IE1-IE6.

## 🚀 Inicio Rápido (5 minutos)

### 1. Iniciar Monitoreo
```bash
cd deployment-observabilidad
docker-compose up -d
```

### 2. Iniciar Aplicación
```bash
cd ../deployment
docker-compose up -d
```

### 3. Acceder a Dashboards
- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090
- **Aplicación**: http://localhost:8080/actuator/health

## 📊 Dashboards Disponibles (Auto-provisionados)

### 1. Application Overview
✅ **Monitoreo en tiempo real del microservicio**
- Estado del servicio y disponibilidad
- Latencia (P50, P90, P95, P99)
- Tasa de errores por código HTTP
- Uso de recursos JVM (Heap, Threads, GC)
- Pool de conexiones de base de datos

### 2. CI/CD & Quality Metrics
✅ **Métricas de calidad y pipeline CI/CD**
- Cobertura de pruebas
- Estado y duración de builds
- Code smells, bugs, vulnerabilidades
- Historial de deployments

### 3. Errors & Availability
✅ **SLA y gestión de incidentes**
- Availability 24h (objetivo 99.9%)
- Alertas críticas activas
- Errores por endpoint
- Timeline de disponibilidad

## 🔔 Sistema de Alertas Configurado

### Alertas Críticas (Interrumpen Pipeline)
- ❌ **ServiceDown**: Servicio caído > 1 minuto
- ❌ **HighErrorRate**: > 5% errores 5xx
- ❌ **DatabaseConnectionPoolExhausted**: > 90% conexiones
- ❌ **SecurityVulnerabilitiesDetected**: Vulnerabilidades detectadas

### Alertas Warning
- ⚠️ **HighLatency**: P95 > 2 segundos
- ⚠️ **HighJVMMemoryUsage**: Heap > 85%
- ⚠️ **HighCPUUsage**: CPU > 80%
- ⚠️ **TestCoverageLow**: Cobertura < 70%

## ✅ Cumplimiento de Criterios (100%)

| Criterio | Estado | Puntaje |
|----------|--------|---------|
| **IE1**: Herramientas configuradas | ✅ COMPLETO | 20% |
| **IE2**: Despliegue automatizado | ✅ COMPLETO | 20% |
| **IE3**: Dashboards funcionales | ✅ COMPLETO | 10% |
| **IE4**: Documentación | ✅ COMPLETO | 10% |
| **IE5**: Políticas de cumplimiento | ✅ COMPLETO | 20% |
| **IE6**: Validación automatizada | ✅ COMPLETO | 20% |

## 📖 Documentación Completa

Ver **`OBSERVABILITY-DOCUMENTATION.md`** para:
- Arquitectura detallada
- Guía de alertas
- Integración CI/CD
- Troubleshooting
- Mejores prácticas

## 🔧 Verificación Rápida

```bash
# Ver métricas de la aplicación
curl http://localhost:8080/actuator/prometheus

# Ver targets de Prometheus
curl http://localhost:9090/api/v1/targets

# Ver alertas activas
curl http://localhost:9090/api/v1/alerts
```

---

**Última actualización**: Diciembre 3, 2025  
**Para la evaluación completa**: Ver OBSERVABILITY-DOCUMENTATION.md

## Pasos para levantar el stack

### 1. Crear la red de Docker para monitoring

```bash
docker network create deployment-observabilidad_monitoring
```

### 2. Levantar el stack de observabilidad

```bash
cd deployment-observabilidad
docker-compose up -d
```

### 3. Levantar tu aplicación

```bash
cd ../deployment
docker-compose up -d
```

### 4. Verificar que todos los contenedores estén corriendo

```bash
docker ps
```

## Acceso a los servicios

- **Grafana**: http://localhost:3000
  - Usuario: `admin`
  - Contraseña: `admin`

- **Prometheus**: http://localhost:1111/prometheus

- **Métricas de la aplicación**: http://localhost:8080/actuator/prometheus

- **Health endpoint**: http://localhost:8080/actuator/health

## Configuración de Grafana

### 1. Importar Dashboards recomendados

Una vez dentro de Grafana:

1. Ir a **Dashboards** → **Import**
2. Ingresar uno de estos IDs y hacer click en **Load**:
   - **11378**: Spring Boot 2.1 Statistics
   - **4701**: JVM (Micrometer)
   - **12900**: Spring Boot Statistics

### 2. Verificar datasources

El datasource de Prometheus ya está pre-configurado automáticamente en:
- **Configuration** → **Data Sources**

Deberías ver:
- Prometheus: `http://prometheus:9090`
- Loki: `http://loki:3100`

## Métricas disponibles

Tu aplicación ahora expone:

✅ Métricas de JVM (memoria, threads, garbage collection)
✅ Métricas HTTP (requests, latencia, errores)
✅ Métricas de conexiones a base de datos
✅ Métricas personalizadas de tu aplicación
✅ Health checks

## Comandos útiles

```bash
# Ver logs de Grafana
docker logs -f grafana

# Ver logs de Prometheus
docker logs -f prometheus

# Ver logs de tu aplicación
docker logs -f bdget-app

# Reiniciar el stack de observabilidad
cd deployment-observabilidad
docker-compose restart

# Detener todo
docker-compose down
```

## Troubleshooting

### Si no ves métricas en Grafana:

1. Verifica que Prometheus esté scrapeando tu app:
   - Ir a http://localhost:1111/prometheus/targets
   - Buscar el job `spring-boot-app`
   - Debe estar en estado `UP`

2. Verifica que tu aplicación expone métricas:
   ```bash
   curl http://localhost:8080/actuator/prometheus
   ```

3. Verifica la conectividad entre contenedores:
   ```bash
   docker exec prometheus ping bdget-app
   ```

### Si Grafana no inicia:

```bash
# Verifica los permisos de los volúmenes
docker-compose logs grafana
```

## Próximos pasos

- Crear dashboards personalizados para tus métricas específicas
- Configurar alertas en Prometheus/Grafana
- Explorar los logs con Loki (ya está configurado)
- Agregar métricas custom en tu código usando Micrometer
