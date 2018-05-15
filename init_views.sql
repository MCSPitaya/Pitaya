-- DB FIXING SCRIPT FOR WHEN JPA SCREWS UP VIEWS
drop table if exists `v_case_summary`;
drop view  if exists `v_case_summary`;
drop table if exists `v_file_auth`;
drop view  if exists `v_file_auth`;
drop table if exists `v_case_auth`;
drop view  if exists `v_case_auth`;
drop table if exists `v_file_summary`;
drop view  if exists `v_file_summary`;
-- CASE SUMMARY VIEW INCLUDING CASE AND USER AUTHCODES
create view `v_case_summary` as
select
	c.id, c.case_number, c.title,
	c.cre_dat, c.mod_dat, u.id as user_id, a.auth_codes as case_auth, u.auth_codes as user_auth
from (cases c join users u
	on c.firm_id = u.firm_id)
left join case_auth_codes a
	on  a.user_id = u.id
	and a.case_id = c.id;

-- FILE AUTHORIZATION VIEW
create view `v_file_auth` as
select
	f.id,
	u.id as user_id,
	fa.auth_codes as file_auth,
	group_concat(ca.auth_codes) as case_auth,
	u.auth_codes  as user_auth
from (((((files f
		join file_cases fc
			on f.id = fc.file_id)
		join cases c
			on fc.cases_id = c.id)
		join users u
			on c.firm_id = u.firm_id)
		left join file_auth_codes fa
			on fa.file_id = f.id and fa.user_id = u.id)
		left join case_auth_codes ca
			on ca.case_id = c.id and ca.user_id = u.id)
group by id, user_id;

-- CASE AUTHORIZATION VIEW
create view `v_case_auth` as
select
	c.id,
	u.id as user_id,
	ca.auth_codes as case_auth,
	u.auth_codes as user_auth
from ((cases c
		join users u
			on c.firm_id = u.firm_id)
		left join case_auth_codes ca
			on ca.case_id = c.id and ca.user_id = u.id);

-- FILE SUMMARY VIEW
create view `v_file_summary` as
select
	f.id,
	f.case_id,
	f.name,
	f.cre_dat,
	f.mod_dat,
	(select count(*) from file_data where file_id = a.id) as revisions,
	a.user_id,
	a.file_auth,
	a.case_auth,
	a.user_auth
from
	files f join v_file_auth a on f.id = a.id;
