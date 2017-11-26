
CREATE TABLE league_series_xref_tbl (
  league_no_in int(11) NOT NULL,
  series_no_in int(11) NOT NULL,
  league_winners_vc varchar(255) DEFAULT NULL,
  league_high_score_si smallint(6) DEFAULT NULL,
  PRIMARY KEY (league_no_in, series_no_in)
);

ALTER TABLE season_series_tbl
    ADD games_completed_si smallint(6) DEFAULT 0;
