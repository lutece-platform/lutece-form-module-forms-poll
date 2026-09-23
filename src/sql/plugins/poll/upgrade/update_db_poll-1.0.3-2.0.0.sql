-- liquibase formatted sql
-- changeset poll:update_db_poll-1.0.3-2.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
UPDATE core_admin_right SET icon_url = 'ti ti-chart-pie', id_feature_group = 'APPLICATIONS' WHERE id_right = 'POLL_MANAGEMENT';
