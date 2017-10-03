delete from user_league_xref_tbl where league_no_in = 13;
delete from user_tbl where league_no_in = 13;
delete from league_tbl where league_no_in = 13;
delete from user_game_xref_tbl where game_no_in >= 2870;
delete from game_tbl where game_no_in >= 2870;
delete from user_series_xref_tbl where series_no_in >= 205;
delete from season_series_tbl where series_no_in >= 205;
delete from season_tbl where season_no_in = 13;
commit;

update season_tbl set season_active_si = 0;
insert into season_tbl(season_no_in, season_name_vc, sport_type_si, series_prefix_vc, season_active_si)
values(13,'NFL 2016', 1, 'Week', 1);

insert into league_tbl(league_no_in, season_no_in, league_name_vc, league_public_ti, league_pwd_vc, league_admin_in)
values(13, 13, 'NFL 2016 - Public League', 1, NULL, -1);

commit;



insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 205, '2016-09-06 00:00:00', '2016-09-12 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2870,205, '2016-09-08 17:30', 5, 10, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2871,205, '2016-09-11 10:00', 12, 15, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2872,205, '2016-09-11 10:00', 4, 3, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2873,205, '2016-09-11 10:00', 6, 13, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2874,205, '2016-09-11 10:00', 8, 24, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2875,205, '2016-09-11 10:00', 30, 2, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2876,205, '2016-09-11 10:00', 18, 31, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2877,205, '2016-09-11 10:00', 7, 22, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2878,205, '2016-09-11 10:00', 23, 20, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2879,205, '2016-09-11 10:00', 26, 16, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2880,205, '2016-09-11 13:05', 17, 28, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2881,205, '2016-09-11 13:25', 11, 14, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2882,205, '2016-09-11 13:25', 21, 9, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2883,205, '2016-09-11 17:30', 19, 1, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2884,205, '2016-09-12 16:10', 25, 32, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2885,205, '2016-09-12 19:20', 29, 27, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 206, '2016-09-13 00:00:00', '2016-09-19 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2886,206, '2016-09-15 17:25', 22, 4, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2887,206, '2016-09-18 10:00', 7, 25, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2888,206, '2016-09-18 10:00', 31, 11, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2889,206, '2016-09-18 10:00', 3, 8, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2890,206, '2016-09-18 10:00', 9, 32, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2891,206, '2016-09-18 10:00', 20, 21, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2892,206, '2016-09-18 10:00', 27, 5, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2893,206, '2016-09-18 10:00', 17, 19, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2894,206, '2016-09-18 10:00', 16, 13, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2895,206, '2016-09-18 13:05', 28, 29, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2896,206, '2016-09-18 13:05', 30, 1, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2897,206, '2016-09-18 13:25', 15, 26, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2898,206, '2016-09-18 13:25', 2, 23, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2899,206, '2016-09-18 13:25', 14, 10, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2900,206, '2016-09-18 17:30', 12, 18, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2901,206, '2016-09-19 17:30', 24, 6, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 207, '2016-09-20 00:00:00', '2016-09-26 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2902,207, '2016-09-22 17:25', 13, 19, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2903,207, '2016-09-25 10:00', 1, 4, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2904,207, '2016-09-25 10:00', 23, 31, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2905,207, '2016-09-25 10:00', 8, 17, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2906,207, '2016-09-25 10:00', 3, 15, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2907,207, '2016-09-25 10:00', 11, 12, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2908,207, '2016-09-25 10:00', 10, 7, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2909,207, '2016-09-25 10:00', 18, 5, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2910,207, '2016-09-25 10:00', 32, 21, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2911,207, '2016-09-25 13:05', 29, 30, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2912,207, '2016-09-25 13:05', 27, 28, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2913,207, '2016-09-25 13:25', 22, 16, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2914,207, '2016-09-25 13:25', 26, 14, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2915,207, '2016-09-25 13:25', 25, 24, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2916,207, '2016-09-25 17:30', 6, 9, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2917,207, '2016-09-26 17:30', 2, 20, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 208, '2016-09-27 00:00:00', '2016-10-03 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2918,208, '2016-09-29 17:25', 17, 7, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2919,208, '2016-10-02 06:30', 14, 15, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2920,208, '2016-10-02 10:00', 31, 13, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2921,208, '2016-10-02 10:00', 8, 32, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2922,208, '2016-10-02 10:00', 28, 22, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2923,208, '2016-10-02 10:00', 4, 19, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2924,208, '2016-10-02 10:00', 5, 2, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2925,208, '2016-10-02 10:00', 23, 3, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2926,208, '2016-10-02 10:00', 11, 6, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2927,208, '2016-10-02 13:05', 10, 30, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2928,208, '2016-10-02 13:25', 29, 1, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2929,208, '2016-10-02 13:25', 20, 26, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2930,208, '2016-10-02 13:25', 9, 27, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2931,208, '2016-10-02 17:30', 16, 25, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2932,208, '2016-10-03 17:30', 21, 18, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 209, '2016-10-04 00:00:00', '2016-10-10 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2933,209, '2016-10-06 17:25', 1, 27, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2934,209, '2016-10-09 10:00', 19, 8, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2935,209, '2016-10-09 10:00', 24, 11, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2936,209, '2016-10-09 10:00', 6, 14, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2937,209, '2016-10-09 10:00', 31, 17, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2938,209, '2016-10-09 10:00', 32, 3, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2939,209, '2016-10-09 10:00', 13, 18, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2940,209, '2016-10-09 10:00', 22, 25, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2941,209, '2016-10-09 13:05', 2, 10, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2942,209, '2016-10-09 13:25', 7, 9, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2943,209, '2016-10-09 13:25', 4, 29, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2944,209, '2016-10-09 13:25', 26, 23, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2945,209, '2016-10-09 17:30', 21, 12, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2946,209, '2016-10-10 17:30', 30, 5, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 210, '2016-10-11 00:00:00', '2016-10-17 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2947,210, '2016-10-13 17:25', 10, 26, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2948,210, '2016-10-16 10:00', 27, 4, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2949,210, '2016-10-16 10:00', 24, 32, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2950,210, '2016-10-16 10:00', 8, 31, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2951,210, '2016-10-16 10:00', 3, 21, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2952,210, '2016-10-16 10:00', 5, 20, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2953,210, '2016-10-16 10:00', 15, 6, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2954,210, '2016-10-16 10:00', 29, 11, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2955,210, '2016-10-16 10:00', 25, 17, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2956,210, '2016-10-16 10:00', 7, 19, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2957,210, '2016-10-16 13:05', 16, 23, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2958,210, '2016-10-16 13:25', 2, 28, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2959,210, '2016-10-16 13:25', 9, 12, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2960,210, '2016-10-16 17:30', 14, 13, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2961,210, '2016-10-17 17:30', 22, 1, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 211, '2016-10-18 00:00:00', '2016-10-24 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2962,211, '2016-10-20 17:25', 6, 12, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2963,211, '2016-10-23 06:30', 21, 29, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2964,211, '2016-10-23 10:00', 20, 16, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2965,211, '2016-10-23 10:00', 14, 31, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2966,211, '2016-10-23 10:00', 18, 24, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2967,211, '2016-10-23 10:00', 8, 7, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2968,211, '2016-10-23 10:00', 32, 11, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2969,211, '2016-10-23 10:00', 23, 15, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2970,211, '2016-10-23 10:00', 4, 17, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2971,211, '2016-10-23 10:00', 3, 22, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2972,211, '2016-10-23 13:05', 30, 27, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2973,211, '2016-10-23 13:05', 26, 2, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2974,211, '2016-10-23 13:25', 19, 25, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2975,211, '2016-10-23 17:30', 28, 1, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2976,211, '2016-10-24 17:30', 13, 10, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 212, '2016-10-25 00:00:00', '2016-10-31 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2977,212, '2016-10-27 17:25', 15, 31, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2978,212, '2016-10-30 06:30', 32, 7, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2979,212, '2016-10-30 10:00', 16, 14, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2980,212, '2016-10-30 10:00', 23, 30, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2981,212, '2016-10-30 10:00', 28, 20, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2982,212, '2016-10-30 10:00', 11, 13, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2983,212, '2016-10-30 10:00', 22, 8, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2984,212, '2016-10-30 10:00', 12, 2, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2985,212, '2016-10-30 10:00', 19, 4, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2986,212, '2016-10-30 13:05', 26, 10, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2987,212, '2016-10-30 13:25', 1, 5, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2988,212, '2016-10-30 17:30', 24, 9, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2989,212, '2016-10-31 17:30', 18, 6, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 213, '2016-11-01 00:00:00', '2016-11-07 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2990,213, '2016-11-03 17:25', 2, 30, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2991,213, '2016-11-06 10:00', 25, 3, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2992,213, '2016-11-06 10:00', 9, 8, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2993,213, '2016-11-06 10:00', 15, 16, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2994,213, '2016-11-06 10:00', 22, 17, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2995,213, '2016-11-06 10:00', 24, 21, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2996,213, '2016-11-06 10:00', 11, 18, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2997,213, '2016-11-06 13:05', 5, 29, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2998,213, '2016-11-06 13:05', 20, 27, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(2999,213, '2016-11-06 13:25', 31, 26, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3000,213, '2016-11-06 13:25', 14, 12, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3001,213, '2016-11-06 17:30', 10, 23, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3002,213, '2016-11-07 17:30', 4, 28, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 214, '2016-11-08 00:00:00', '2016-11-14 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3003,214, '2016-11-10 17:25', 8, 3, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3004,214, '2016-11-13 10:00', 12, 31, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3005,214, '2016-11-13 10:00', 18, 32, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3006,214, '2016-11-13 10:00', 6, 30, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3007,214, '2016-11-13 10:00', 16, 5, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3008,214, '2016-11-13 10:00', 2, 24, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3009,214, '2016-11-13 10:00', 29, 22, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3010,214, '2016-11-13 10:00', 10, 20, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3011,214, '2016-11-13 10:00', 13, 15, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3012,214, '2016-11-13 13:05', 17, 26, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3013,214, '2016-11-13 13:25', 9, 25, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3014,214, '2016-11-13 13:25', 27, 1, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3015,214, '2016-11-13 17:30', 28, 19, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3016,214, '2016-11-14 17:30', 7, 21, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 215, '2016-11-15 00:00:00', '2016-11-21 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3017,215, '2016-11-17 17:25', 20, 5, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3018,215, '2016-11-20 10:00', 25, 8, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3019,215, '2016-11-20 10:00', 3, 9, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3020,215, '2016-11-20 10:00', 15, 11, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3021,215, '2016-11-20 10:00', 31, 14, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3022,215, '2016-11-20 10:00', 4, 7, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3023,215, '2016-11-20 10:00', 30, 16, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3024,215, '2016-11-20 10:00', 6, 21, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3025,215, '2016-11-20 10:00', 1, 18, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3026,215, '2016-11-20 13:05', 17, 29, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3027,215, '2016-11-20 13:25', 19, 27, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3028,215, '2016-11-20 13:25', 24, 28, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3029,215, '2016-11-20 17:30', 12, 32, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3030,215, '2016-11-21 17:30', 13, 23, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 216, '2016-11-22 00:00:00', '2016-11-28 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3031,216, '2016-11-24 09:30', 18, 11, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3032,216, '2016-11-24 13:30', 32, 9, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3033,216, '2016-11-24 17:30', 25, 14, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3034,216, '2016-11-27 10:00', 31, 6, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3035,216, '2016-11-27 10:00', 15, 4, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3036,216, '2016-11-27 10:00', 7, 3, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3037,216, '2016-11-27 10:00', 1, 2, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3038,216, '2016-11-27 10:00', 21, 8, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3039,216, '2016-11-27 10:00', 29, 20, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3040,216, '2016-11-27 10:00', 27, 17, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3041,216, '2016-11-27 10:00', 26, 13, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3042,216, '2016-11-27 13:05', 28, 30, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3043,216, '2016-11-27 13:25', 5, 23, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3044,216, '2016-11-27 13:25', 16, 10, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3045,216, '2016-11-27 17:30', 19, 22, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3046,216, '2016-11-28 17:30', 12, 24, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 217, '2016-11-29 00:00:00', '2016-12-05 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3047,217, '2016-12-01 17:25', 9, 18, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3048,217, '2016-12-04 10:00', 16, 2, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3049,217, '2016-12-04 10:00', 11, 20, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3050,217, '2016-12-04 10:00', 29, 19, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3051,217, '2016-12-04 10:00', 10, 15, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3052,217, '2016-12-04 10:00', 13, 12, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3053,217, '2016-12-04 10:00', 24, 7, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3054,217, '2016-12-04 10:00', 17, 3, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3055,217, '2016-12-04 10:00', 27, 6, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3056,217, '2016-12-04 13:05', 4, 23, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3057,217, '2016-12-04 13:25', 21, 25, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3058,217, '2016-12-04 13:25', 32, 1, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3059,217, '2016-12-04 13:25', 30, 26, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3060,217, '2016-12-04 17:30', 5, 28, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3061,217, '2016-12-05 17:30', 14, 22, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 218, '2016-12-06 00:00:00', '2016-12-12 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3062,218, '2016-12-08 17:25', 23, 16, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3063,218, '2016-12-11 10:00', 25, 4, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3064,218, '2016-12-11 10:00', 10, 31, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3065,218, '2016-12-11 10:00', 20, 30, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3066,218, '2016-12-11 10:00', 32, 24, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3067,218, '2016-12-11 10:00', 1, 17, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3068,218, '2016-12-11 10:00', 26, 5, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3069,218, '2016-12-11 10:00', 7, 8, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3070,218, '2016-12-11 10:00', 6, 11, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3071,218, '2016-12-11 10:00', 13, 14, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3072,218, '2016-12-11 10:00', 18, 15, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3073,218, '2016-12-11 13:05', 22, 27, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3074,218, '2016-12-11 13:25', 2, 29, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3075,218, '2016-12-11 13:25', 28, 12, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3076,218, '2016-12-11 17:30', 9, 21, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3077,218, '2016-12-12 17:30', 3, 19, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 219, '2016-12-13 00:00:00', '2016-12-19 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3078,219, '2016-12-15 17:25', 29, 28, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3079,219, '2016-12-17 17:25', 17, 22, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3080,219, '2016-12-18 10:00', 12, 6, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3081,219, '2016-12-18 10:00', 30, 9, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3082,219, '2016-12-18 10:00', 15, 13, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3083,219, '2016-12-18 10:00', 8, 4, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3084,219, '2016-12-18 10:00', 24, 3, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3085,219, '2016-12-18 10:00', 31, 16, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3086,219, '2016-12-18 10:00', 11, 21, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3087,219, '2016-12-18 10:00', 14, 18, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3088,219, '2016-12-18 13:05', 20, 1, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3089,219, '2016-12-18 13:05', 27, 2, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3090,219, '2016-12-18 13:25', 19, 10, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3091,219, '2016-12-18 13:25', 23, 26, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3092,219, '2016-12-18 17:30', 25, 7, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3093,219, '2016-12-19 17:30', 5, 32, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 220, '2016-12-20 00:00:00', '2016-12-26 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3094,220, '2016-12-22 17:25', 21, 24, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3095,220, '2016-12-24 10:00', 17, 4, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3096,220, '2016-12-24 10:00', 30, 20, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3097,220, '2016-12-24 10:00', 22, 19, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3098,220, '2016-12-24 10:00', 31, 15, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3099,220, '2016-12-24 10:00', 18, 12, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3100,220, '2016-12-24 10:00', 26, 8, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3101,220, '2016-12-24 10:00', 32, 6, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3102,220, '2016-12-24 10:00', 2, 5, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3103,220, '2016-12-24 13:05', 14, 23, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3104,220, '2016-12-24 13:25', 1, 28, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3105,220, '2016-12-24 13:25', 27, 29, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3106,220, '2016-12-24 17:25', 7, 13, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3107,220, '2016-12-25 13:30', 3, 25, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3108,220, '2016-12-25 17:30', 10, 16, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3109,220, '2016-12-26 17:30', 11, 9, 0);


insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values(13, 221, '2016-12-27 00:00:00', '2017-01-02 23:59:59', 0, 0, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3110,221, '2017-01-01 10:00', 20, 2, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3111,221, '2017-01-01 10:00', 3, 7, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3112,221, '2017-01-01 10:00', 21, 32, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3113,221, '2017-01-01 10:00', 13, 31, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3114,221, '2017-01-01 10:00', 5, 30, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3115,221, '2017-01-01 10:00', 12, 11, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3116,221, '2017-01-01 10:00', 15, 14, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3117,221, '2017-01-01 10:00', 19, 17, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3118,221, '2017-01-01 10:00', 6, 18, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3119,221, '2017-01-01 10:00', 4, 22, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3120,221, '2017-01-01 10:00', 9, 24, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3121,221, '2017-01-01 10:00', 8, 25, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3122,221, '2017-01-01 13:25', 1, 29, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3123,221, '2017-01-01 13:25', 23, 10, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3124,221, '2017-01-01 13:25', 16, 26, 0);
insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values(3125,221, '2017-01-01 13:25', 28, 27, 0);
