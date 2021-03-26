
--
-- Structure for table poll_form
--

DROP TABLE IF EXISTS poll_form;
CREATE TABLE poll_form (
id_poll_form int AUTO_INCREMENT,
id_form int default '0' NOT NULL,
is_visible SMALLINT NOT NULL,
title long varchar,
btn_title long varchar,
btn_url long varchar,
btn_is_visible SMALLINT NOT NULL,
PRIMARY KEY (id_poll_form)
);

--
-- Structure for table poll_form_question
--

DROP TABLE IF EXISTS poll_form_question;
CREATE TABLE poll_form_question (
id_poll_form_question int AUTO_INCREMENT,
id_poll_form int default '0' NOT NULL,
id_form int default '0' NOT NULL,
id_question int default '0' NOT NULL,
chart_type long varchar,
chart_is_toolbox SMALLINT NOT NULL,
chart_is_checked SMALLINT NOT NULL,
PRIMARY KEY (id_poll_form_question)
);