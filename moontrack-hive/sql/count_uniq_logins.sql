BEGIN;

CREATE TABLE IF NOT EXISTS count_uniq_logins (
	date timestamp without time zone,
	game_id smallint,
	realm_id smallint,
	logins int
);
ALTER TABLE count_uniq_logins OWNER TO analytics;
GRANT SELECT ON TABLE count_uniq_logins TO dashboard;
CREATE SEQUENCE IF NOT EXISTS count_uniq_logins_seq;

CREATE OR REPLACE FUNCTION count_uniq_logins_update()
	RETURNS void
	LANGUAGE 'sql'
	COST 100
	VOLATILE 
AS $BODY$

TRUNCATE TABLE count_uniq_logins;
INSERT INTO count_uniq_logins SELECT date_trunc('day', login_date) AS day, game, realm_server, COUNT(DISTINCT r_user)
FROM logins, realm_users, d_user_registrations
WHERE logins.r_user = realm_users.id
AND realm_users.d_user = d_user_registrations.id
GROUP BY day, game, realm_server;

$BODY$;

ALTER FUNCTION count_uniq_logins_update()
	OWNER TO analytics;

COMMIT;