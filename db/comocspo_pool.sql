
-- --------------------------------
-- audit_login_tbl
-- --------------------------------
CREATE TABLE audit_login_tbl (
  login_timestamp_dt datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  login_id_vc varchar(64) DEFAULT NULL,
  login_pwd_vc varchar(64) DEFAULT NULL,
  user_no_in int(11) DEFAULT NULL
);

-- --------------------------------
-- audit_picks_tbl
-- --------------------------------
CREATE TABLE audit_picks_tbl (
  pick_timestamp_dt datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  pick_type_vc varchar(64) NOT NULL DEFAULT '',
  user_no_in int(11) NOT NULL DEFAULT '0',
  game_no_in int(11) DEFAULT NULL,
  series_no_in int(11) DEFAULT NULL,
  team_no_in int(11) DEFAULT NULL
);

-- --------------------------------
-- forum_message_tbl
-- --------------------------------
CREATE TABLE forum_message_tbl (
  forum_msg_no_in int(11) NOT NULL DEFAULT '0',
  league_no_in int(11) NOT NULL DEFAULT '0',
  msg_text_vc text,
  msg_created_dt datetime DEFAULT NULL,
  msg_created_by_in int(11) DEFAULT NULL,
  PRIMARY KEY (forum_msg_no_in)
);

-- --------------------------------
-- game_tbl
-- --------------------------------
CREATE TABLE game_tbl (
  game_no_in int(11) NOT NULL DEFAULT '0',
  series_no_in int(11) NOT NULL DEFAULT '0',
  game_start_dt datetime DEFAULT NULL,
  away_team_in int(11) DEFAULT NULL,
  home_team_in int(11) DEFAULT NULL,
  game_spread_fl float DEFAULT NULL,
  game_notes_vc varchar(255) DEFAULT NULL,
  default_picks_si smallint(6) DEFAULT NULL,
  home_score_in int(11) DEFAULT NULL,
  away_score_in int(11) DEFAULT NULL,
  game_posted_si smallint(6) DEFAULT NULL,
  PRIMARY KEY (game_no_in)
);

-- --------------------------------
-- key_tbl
-- --------------------------------
CREATE TABLE key_tbl (
  tbl varchar(30) NOT NULL DEFAULT '',
  col varchar(30) NOT NULL DEFAULT '',
  val int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (tbl, col)
);

-- --------------------------------
-- league_tbl
-- --------------------------------
CREATE TABLE league_tbl (
  league_no_in int(11) NOT NULL DEFAULT '0',
  league_name_vc varchar(64) DEFAULT NULL,
  season_no_in int(11) NOT NULL DEFAULT '0',
  league_public_ti tinyint(4) DEFAULT NULL,
  league_pwd_vc varchar(15) DEFAULT NULL,
  league_admin_in int(11) DEFAULT NULL,
  PRIMARY KEY (league_no_in)
);

-- --------------------------------
-- league_series_xref_tbl
-- --------------------------------
CREATE TABLE league_series_xref_tbl (
  league_no_in int(11) NOT NULL,
  series_no_in int(11) NOT NULL,
  league_winners_vc varchar(255) DEFAULT NULL,
  league_high_score_si smallint(6) DEFAULT NULL,
  PRIMARY KEY (league_no_in, series_no_in)
);

-- --------------------------------
-- season_series_tbl
-- --------------------------------
CREATE TABLE season_series_tbl (
  series_no_in int(11) NOT NULL DEFAULT '0',
  season_no_in int(11) NOT NULL DEFAULT '0',
  series_start_dt datetime DEFAULT NULL,
  series_end_dt datetime DEFAULT NULL,
  spread_published_si smallint(6) DEFAULT NULL,
  series_cleanup_si smallint(6) DEFAULT NULL,
  series_reminder_si smallint(6) DEFAULT NULL,
  season_seq_no smallint(2) DEFAULT NULL,
  games_completed_si smallint(6) DEFAULT 0,
  PRIMARY KEY (series_no_in)
);

-- --------------------------------
-- season_tbl
-- --------------------------------
CREATE TABLE season_tbl (
  season_no_in int(11) NOT NULL DEFAULT '0',
  season_name_vc varchar(30) NOT NULL DEFAULT '',
  sport_type_si smallint(6) NOT NULL DEFAULT '0',
  series_prefix_vc varchar(30) DEFAULT NULL,
  season_active_si smallint(6) DEFAULT NULL,
  PRIMARY KEY (season_no_in)
);

-- --------------------------------
-- system_notice_tbl
-- --------------------------------
CREATE TABLE system_notice_tbl (
  notice_no_in int(11) NOT NULL DEFAULT '0',
  notice_created_dt datetime DEFAULT NULL,
  notice_text_vc text,
  notice_publish_si smallint(6) DEFAULT NULL,
  notice_updated_dt datetime DEFAULT NULL,
  PRIMARY KEY (notice_no_in)
);

-- --------------------------------
-- team_conference_tbl
-- --------------------------------
CREATE TABLE team_conference_tbl (
  team_conf_no_in int(11) NOT NULL DEFAULT '0',
  team_conf_name_vc varchar(30) NOT NULL DEFAULT '',
  sport_type_si smallint(6) DEFAULT NULL,
  PRIMARY KEY (team_conf_no_in)
);

-- --------------------------------
-- team_division_tbl
-- --------------------------------
CREATE TABLE team_division_tbl (
  team_div_no_in int(11) NOT NULL DEFAULT '0',
  team_conf_no_in int(11) NOT NULL DEFAULT '0',
  team_div_name_vc varchar(30) NOT NULL DEFAULT '',
  PRIMARY KEY (team_div_no_in)
);

-- --------------------------------
-- team_tbl
-- --------------------------------
CREATE TABLE team_tbl (
  team_no_in int(11) NOT NULL DEFAULT '0',
  team_div_no_in int(11) NOT NULL DEFAULT '0',
  team_city_vc varchar(30) DEFAULT NULL,
  team_name_vc varchar(30) DEFAULT NULL,
  team_abrv_vc char(3) DEFAULT NULL,
  sport_type_si smallint(6) DEFAULT NULL,
  team_dome_si smallint(6) DEFAULT NULL,
  team_turf_si smallint(6) DEFAULT NULL,
  team_weather_url_vc varchar(60) DEFAULT NULL,
  PRIMARY KEY (team_no_in)
);

-- --------------------------------
-- user_game_xref_tbl
-- --------------------------------
CREATE TABLE user_game_xref_tbl (
  user_no_in int(11) NOT NULL DEFAULT '0',
  game_no_in int(11) NOT NULL DEFAULT '0',
  team_no_in int(11) DEFAULT NULL,
  ugame_notes_vc varchar(255) DEFAULT NULL,
  ugame_status_si smallint(6) DEFAULT NULL,
  ugame_default_si smallint(6) DEFAULT NULL,
  ugame_timestamp_dt datetime DEFAULT NULL,
  PRIMARY KEY (user_no_in, game_no_in)
);

-- --------------------------------
-- user_league_xref_tbl
-- --------------------------------
CREATE TABLE user_league_xref_tbl (
  user_no_in int(11) NOT NULL DEFAULT '0',
  league_no_in int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (league_no_in, user_no_in)
);

-- --------------------------------
-- user_series_xref_tbl
-- --------------------------------
CREATE TABLE user_series_xref_tbl (
  user_no_in int(11) NOT NULL DEFAULT '0',
  series_no_in int(11) NOT NULL DEFAULT '0',
  survivor_team_in int(11) DEFAULT NULL,
  lock_team_in int(11) DEFAULT NULL,
  useries_notes_vc varchar(255) DEFAULT NULL,
  lock_status_si smallint(6) DEFAULT NULL,
  survivor_status_si smallint(6) DEFAULT NULL,
  useries_timestamp_dt datetime DEFAULT NULL,
  PRIMARY KEY (user_no_in, series_no_in)
);

-- --------------------------------
-- user_tbl
-- --------------------------------
CREATE TABLE user_tbl (
  user_no_in int(11) NOT NULL DEFAULT '0',
  league_no_in int(11) DEFAULT NULL,
  user_fname_vc varchar(40) DEFAULT NULL,
  user_mname_vc varchar(10) DEFAULT NULL,
  user_lname_vc varchar(40) DEFAULT NULL,
  user_address_vc varchar(255) DEFAULT NULL,
  user_email_vc varchar(64) DEFAULT NULL,
  user_email2_vc varchar(64) DEFAULT NULL,
  user_login_id_vc varchar(64) NOT NULL DEFAULT '',
  user_login_pwd_vc varchar(64) NOT NULL DEFAULT '',
  user_default_pick_si smallint(6) DEFAULT NULL,
  user_email_picks_si smallint(6) DEFAULT NULL,
  user_send_warning_si smallint(6) DEFAULT NULL,
  user_paid_si smallint(6) DEFAULT NULL,
  user_survivor_status_si smallint(6) DEFAULT NULL,
  user_theme_vc varchar(30) DEFAULT NULL,
  user_pref_team_in int(11) DEFAULT NULL,
  user_pref_team_vc varchar(30) DEFAULT NULL,
  user_login_disabled_si smallint(6) DEFAULT NULL,
  PRIMARY KEY (user_no_in)
);

COMMIT;
