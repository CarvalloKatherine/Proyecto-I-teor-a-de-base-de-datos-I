--01
SELECT c.name, count(*) from campaigns c 
inner join campaign_runs cr on c.campaign_id = cr.campaign_id
group by c.name;


--02 //campaing solo porque se requiere el nombre, si no, no es necesario usarla
with last_run as(
select 
	c.campaign_id,
	c.name,
	cr.campaign_run_id,
	cr.start_at,
	cr.end_at,
	row_number () over (partition by cr.campaign_id order by cr.start_at desc) as n
from campaigns c
	inner join campaign_runs cr
		on c.campaign_id = cr.campaign_id
)
select 
	lr.name,
	lr.campaign_run_id,
	lr.start_at,
	lr.end_at
	from last_run lr
	where lr.n = 1;
	
--3

with last_messages as (
SELECT
	c.name,
	ct.name,
	cc.campaign_contact_id,
	crm.campaign_run_id,
	crm.created_at,
	ROW_NUMBER() OVER(PARTITION BY crm.campaign_run_id, crm.campaign_contact_id 
	ORDER BY crm.created_at)
FROM campaign_run_messages crm
INNER JOIN campaign_runs cr ON crm.campaign_run_id=cr.campaign_run_id
INNER JOIN campaign_contacts cc ON crm.campaign_contact_id=cc.campaign_contact_id
INNER JOIN campaigns c ON c.campaign_id=cc.campaign_id
INNER JOIN contacts ct ON ct.contact_id=cc.contact_id
LIMIT 1000
) 
select 
name, 
name,
campaign_contact_id,
campaign_run_id,
created_at
from  last message t1
where n = 1
