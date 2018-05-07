-- DB FIXING SCRIPT FOR WHEN JPA SCREWS UP VIEWS
drop table if exists `v_case_summary`;

create view `v_case_summary` as
select
	c.id, c.case_number, c.title,
	c.cre_dat, c.mod_dat, u.id as user_id, a.auth_codes
from (cases c join users u
	on c.firm_id = u.firm_id)
left join case_auth_codes a
	on  a.user_id = u.id
	and a.case_id = c.id;
