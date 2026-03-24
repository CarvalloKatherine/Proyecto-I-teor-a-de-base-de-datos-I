#  Autoevaluación del Proyecto

##  Reflexión sobre el proceso de desarrollo

Durante el desarrollo de este proyecto adquirí múltiples conocimientos tanto en el área de bases de datos como en el desarrollo de aplicaciones. Uno de los aprendizajes más importantes fue comprender que no siempre es necesario tener relaciones directas entre todas las tablas, ya que mediante el uso adecuado de `JOINs` se pueden estructurar y relacionar los datos de manera eficiente.

Asimismo, aprendí sobre el diseño y creación de bases de datos, incluyendo la elaboración del **DDL**, el modelado en **DBML**, y la implementación de **procedimientos almacenados, funciones y triggers**, entendiendo en qué situaciones es más conveniente utilizar cada uno.

También desarrollé habilidades en la generación de reportes y gráficos, así como en la interpretación de información financiera, incluyendo conceptos como balances, ingresos, gastos y ahorros.

Para el desarrollo del sistema, utilicé **Java** como lenguaje principal y **SQL Anywhere** como gestor de base de datos.

Además, realicé:
- Todas las tablas de la base de datos
- El DDL completo
- El modelo en DBML
- Todos los procedimientos almacenados y funciones (incluyendo algunas adicionales que consideré necesarias)
- Un trigger para generar automáticamente la subcategoría general
- Los 5 reportes solicitados

Cabe mencionar que no utilicé todas las funciones creadas, ya que en algunos casos opté por implementar alternativas más prácticas, como listar todas las subcategorías en lugar de filtrarlas únicamente por ID.

También incluí datos de prueba utilizando `SELECT` para obtener los IDs, ya que desarrollé todo en un solo documento, lo cual me permitió mantener un control exacto de la información.

Finalmente, para el frontend reutilicé un proyecto propio y lo adapté a las necesidades del sistema.

---

##  Desafíos enfrentados y soluciones

Uno de los principales desafíos fue la compatibilidad de tipos de datos entre **JDBC** y **SQL Anywhere**, lo cual generó errores durante la ejecución, especialmente al trabajar con parámetros `OUT` en procedimientos almacenados.

Para solucionarlo, modifiqué algunos procedimientos para que devolvieran los resultados mediante `SELECT`, lo cual facilitó su manejo desde Java. Además, implementé validaciones adicionales para prevenir errores en tiempo de ejecución.

Otro reto importante fue la función de creación de presupuesto completo, la cual originalmente utilizaba **JSON**. Debido a que SQL Anywhere no soporta este tipo de dato de la misma manera, opté por implementar la creación del presupuesto mediante múltiples llamadas al detalle de presupuesto.

También enfrenté dificultades al aprender conceptos financieros que no dominaba completamente y al implementar gráficos dentro de la aplicación, lo cual requirió investigación adicional sobre herramientas y formas de visualización.

---

##  Aprendizajes clave

Este proyecto me permitió desarrollar una comprensión más sólida de:

- Modelado y diseño de bases de datos
- Uso de procedimientos almacenados, funciones y triggers
- Manejo de datos mediante `JOINs`
- Integración entre Java y bases de datos
- Generación de reportes y visualización de datos
- Conceptos financieros básicos aplicados a sistemas

Además, aprendí que los proyectos pueden ser largos, pero no necesariamente complicados si se abordan de forma ordenada. Me llevo un aprendizaje significativo que considero muy importante para mi formación.

---

##  Sugerencias de mejora del proyecto

Considero que el proyecto podría mejorarse en los siguientes aspectos:

- Aprender desde etapas tempranas el uso de funciones y procedimientos almacenados para optimizar el desarrollo.
- Mejorar la experiencia del usuario, permitiendo mayor personalización, como la creación de categorías propias sin depender de un administrador.
- Contar con más tiempo para perfeccionar el frontend y la interacción del sistema.

---

##  Uso de herramientas e investigación

Durante el desarrollo, utilicé herramientas de inteligencia artificial como apoyo en diferentes aspectos, tales como:

- Conversión del DDL de PostgreSQL a SQL Anywhere ya que no se podía exportar directamente. 
- Identificación y corrección de errores en el código cuando ya no encontraba el problema que usualmente era errores sintaxis
- Realización en el desarrollo del frontend
- Investigación de conceptos y formulas 

Además, complementé este aprendizaje con otras fuentes como **YouTube, foros y búsquedas en Google**, lo cual me permitió resolver dudas y avanzar de manera más eficiente.

---

##  Reflexión final

A lo largo del proceso surgieron múltiples dudas, pero fui resolviéndolas progresivamente, lo cual fortaleció mi aprendizaje. Este proyecto me gustó mucho, ya que me permitió aplicar y consolidar conocimientos importantes en bases de datos.

Aunque fue un proyecto extenso, resultó menos complicado de lo que esperaba (aun siempre con sus retos) y me alegra haber adquirido un aprendizaje real y significativo.

Agradezco al ingeniero por su apoyo y paciencia durante todo el proceso.