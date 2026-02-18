# Sistema de Presupuesto Mensual Personal

Sistema web para gestionar finanzas personales: ingresos, gastos, ahorros y obligaciones fijas, con reportería analítica integrada.

---

##  Descripción

Aplicación web de tres capas que permite a un usuario planificar y controlar su presupuesto mensual. El sistema gestiona categorías, subcategorías, presupuestos con vigencia temporal, transacciones reales y obligaciones fijas recurrentes, brindando visibilidad financiera a través de reportes y gráficos.

---

##  Objetivos

- Aplicar conocimientos de Teoria de Bases de Datos I en un proyecto real.
- Diseñar e implementar un modelo relacional completo con entidades, relaciones y reglas de negocio.
- Implementar lógica de negocio en la base de datos mediante procedimientos almacenados, funciones y triggers.

---

## Características

- **Gestión de usuarios** con perfil y salario base de referencia.
- **Presupuestos con vigencia temporal** (mensual, trimestral, semestral o anual).
- **Categorías y subcategorías** jerárquicas para clasificar ingresos, gastos y ahorros.
- **Obligaciones fijas mensuales** con alertas de vencimiento (servicios, deudas, seguros).
- **Registro de transacciones** con imputación presupuestal flexible por mes/año.
- **6 reportes analíticos** exportables a PDF: balance mensual, distribución de gastos, cumplimiento de presupuesto, tendencias, estado de obligaciones y progreso de metas de ahorro.
- Toda la lógica de negocio encapsulada en **procedimientos almacenados y triggers**.

---

##  Tecnologías

| Capa | Tecnología |
|------|------------|
| Base de datos | SAP SQL Anywhere |
| Backend | Java |
| Frontend | Java Swing |
| Reportería | Metabase |
| Control de versiones | Git / GitHub |

---

##  Instalación de SQL Anywhere

1. Ingresar al sitio oficial de SAP SQL Anywhere:  
    [https://www.sap.com/products/data-cloud/sql-anywhere.html](https://www.sap.com/products/data-cloud/sql-anywhere.html)

2. Registrarse con tu cuenta institucional o personal para obtener la versión de prueba.

3. Descargar el instalador correspondiente a tu sistema operativo (Windows / Linux).

4. Ejecutar el instalador y seguir el asistente de instalación.

> Para más detalles consulta la [documentación oficial de SAP SQL Anywhere](https://help.sap.com/docs/sql-anywhere).


---

##  Estructura del Repositorio

```
proyecto-presupuesto-personal/
├── README.md
├── docs/
│   ├── ModeloRelacional.pdf
│   ├── DiccionarioDatos.xlsx
│   └── Reportes.pdf
├── database/
│   ├── DDL/
│   ├── procedimientos/
│   ├── funciones/
│   ├── triggers/
│   └── datos_prueba/
├── backend/
│   └── src/
├── frontend/
│   └── src/
└── metabase/
    └── metabase_backup.zip
```

---

## Autor

**Katherine Carvallo**  
Universidad Tecnológica Centroamericana (UNITEC)  
Teoria de Bases de Datos I — 2026
