use electrical_data;

/* Inserts Default */

## Place ##
INSERT INTO place (place_id,name,meter) VALUES (1,'CERIn','Medidor 13');

## Electrical Data ##
INSERT INTO electrical_data (place_id,measurement_date_time,
current_phase_a,current_phase_b,current_phase_c,current_total,
factor_power,frequency,apparent_power,active_power,reactive_power,
energy_production,phase_voltage_ab,phase_voltage_bc,phase_voltage_ca)
VALUES (1, '2018-01-25 12:18:30',13.97181,13.93181,13.98181,41.116722,-99.64918,60.0169,9672.018,9671.93,41.42097,3.0919744E7,397.5435,399.11285,397.60028);

## Users ##
INSERT INTO users(user_id,name,email,password) VALUES (1,'Administrador','dadoseletricos@ifsuldeminas.edu.br','WOX5/Mwafa2R69lt3DaOoVJtQB8=');

/* Fim */

/*
SET innodb_lock_wait_timeout = 200;

INSERT INTO mysql.user (Host, User, Password, Select_priv, max_connections, max_user_connections, ssl_cipher, x509_issuer, x509_subject,
Insert_priv, Update_priv, Delete_priv, Create_priv, Drop_priv, Reload_priv, Shutdown_priv, Process_priv, File_priv, Grant_priv, 
References_priv, Index_priv, Alter_priv, Show_db_priv, Super_priv, Create_tmp_table_priv, Lock_tables_priv, Execute_priv, 
Repl_slave_priv, Repl_client_priv, Create_view_priv, Show_view_priv, Create_routine_priv, Alter_routine_priv, Create_user_priv) 
VALUES ('%', 'root', '', 'Y', 200, 200, '','','',
'Y', 'Y', 'Y', 'N', 'N', 'N', 'N', 'N', 'N', 'N',
'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 
'N', 'N', 'N', 'N', 'N', 'N', 'N');

GRANT ALL ON *.* TO 'root'@'%';

flush privileges ;
*/