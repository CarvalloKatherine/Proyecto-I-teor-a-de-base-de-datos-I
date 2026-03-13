select 
  c.name as category,
  COUNT (CASE WHEN rating = 'G' THEN 1 END) as G,
  COUNT (CASE WHEN rating = 'PG' THEN 1 END) as PG,
  COUNT (CASE WHEN rating = 'PG-13' THEN 1 END) as PG_13,
COUNT (CASE WHEN rating NOT IN ('G','PG','PG-13') THEN 1 END) as OTROS
FROM category c
INNER JOIN film_category fc ON c.category_id = fc.category_id
INNER JOIN film f ON fc.film_id = f.film_id
GROUP BY category

------------
select 
  TO_CHAR(fecha, 'YYYY') AS ANIO,
sum(case when to_char(fecha,'MM') IN ('01','02','03') then total else null end)as Q1,
sum(case when to_char(fecha,'MM') IN ('04','05','06') then total else null end)as Q2,
sum(case when to_char(fecha,'MM') IN ('07','08','09') then total else null end)as Q3,
sum(case when to_char(fecha,'MM') IN ('10','11','12') then total else null end)as Q4
from ventas
GROUP BY to_char(fecha, 'YYYY')