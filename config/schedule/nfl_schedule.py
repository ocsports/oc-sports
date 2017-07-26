#!/usr/bin/python

import csv
import datetime
import os

INPUT_FILE = "nfl_schedule_2017.csv"
OUTPUT_FILE = "nfl_schedule_2017.sql"
SEASON_KEY = 14
LEAGUE_KEY = 14
FIRST_SERIES_KEY = 225
FIRST_GAME_KEY = 3130
FIRST_SERIES_START = datetime.datetime.strptime("9/6/2017", "%m/%d/%Y")

DATE_IDX = 0
TIME_IDX = 1
AWAY_TEAM_IDX = 2
HOME_TEAM_IDX = 3

TEAM_KEYS = {
    "ARIZONACARDINALS": 1,
    "ATLANTAFALCONS": 2,
    "BALTIMORERAVENS": 3,
    "BUFFALOBILLS": 4,
    "CAROLINAPANTHERS": 5,
    "CHICAGOBEARS": 6,
    "CINCINNATIBENGALS": 7,
    "CLEVELANDBROWNS": 8,
    "DALLASCOWBOYS": 9,
    "DENVERBRONCOS": 10,
    "DETROITLIONS": 11,
    "GREENBAYPACKERS": 12,
    "HOUSTONTEXANS": 13,
    "INDIANAPOLISCOLTS": 14,
    "JACKSONVILLEJAGUARS": 15,
    "KANSASCITYCHIEFS": 16,
    "MIAMIDOLPHINS": 17,
    "MINNESOTAVIKINGS": 18,
    "NEWENGLANDPATRIOTS": 19,
    "NEWORLEANSSAINTS": 20,
    "NEWYORKGIANTS": 21,
    "NEWYORKJETS": 22,
    "OAKLANDRAIDERS": 23,
    "PHILADELPHIAEAGLES": 24,
    "PITTSBURGHSTEELERS": 25,
    "LOSANGELESCHARGERS": 26,
    "SANFRANCISCO49ERS": 27,
    "SEATTLESEAHAWKS": 28,
    "LOSANGELESRAMS": 29,
    "TAMPABAYBUCCANEERS": 30,
    "TENNESSEETITANS": 31,
    "WASHINGTONREDSKINS": 32,
    "ARIZONA": 1,
    "ATLANTA": 2,
    "BALTIMORE": 3,
    "BUFFALO": 4,
    "CAROLINA": 5,
    "CHICAGO": 6,
    "CINCINNATI": 7,
    "CLEVELAND": 8,
    "DALLAS": 9,
    "DENVER": 10,
    "DETROIT": 11,
    "GREENBAY": 12,
    "HOUSTON": 13,
    "INDIANAPOLIS": 14,
    "JACKSONVILLE": 15,
    "KANSASCITY": 16,
    "MIAMI": 17,
    "MINNESOTA": 18,
    "NEWENGLAND": 19,
    "NEWORLEANS": 20,
    "NYGIANTS": 21,
    "NYJETS": 22,
    "OAKLAND": 23,
    "PHILADELPHIA": 24,
    "PITTSBURGH": 25,
    "SANDIEGO": 26,
    "SANFRANCISCO": 27,
    "SEATTLE": 28,
    "LOSANGELES": 29,
    "TAMPABAY": 30,
    "TENNESSEE": 31,
    "WASHINGTON": 32,
}

series_key = 0
series_start = None
series_end = None


def main():
    if os.path.exists(OUTPUT_FILE):
        os.remove(OUTPUT_FILE)

    try:
        printHeader()

        game_key = FIRST_GAME_KEY
        last_series_key = -1
        with open(INPUT_FILE, 'rU') as csvfile:
            csvreader = csv.reader(csvfile, delimiter=',', quotechar='\"')
            for row in csvreader:
                # print ', '.join(row)

                away_team = row[AWAY_TEAM_IDX]
                away_team_key = get_team_id(away_team)
                if away_team_key == 0:
                    raise Exception("invalid away team {}".format(away_team))

                home_team = row[HOME_TEAM_IDX]
                home_team_key = get_team_id(home_team)
                if home_team_key == 0:
                    raise Exception("invalid home team {}".format(home_team))

                kickoff = CDate(row[DATE_IDX] + " " + row[TIME_IDX], '%m/%d/%Y %H:%M')
                # adjust for PT (from ET)
                kickoff = kickoff - datetime.timedelta(hours=3)

                populate_series(kickoff)
                if not series_key == last_series_key:
                    if not last_series_key == -1:
                        write_sql("commit;\n")
                    sql = "insert into season_series_tbl(season_no_in, series_no_in, series_start_dt, series_end_dt, spread_published_si, series_cleanup_si, series_reminder_si) values({}, {}, '{}', '{}', {}, {}, {});"
                    write_sql(sql.format(SEASON_KEY, series_key, series_start, series_end, 0, 0, 0))
                    last_series_key = series_key

                sql = "insert into game_tbl(game_no_in, series_no_in, game_start_dt, away_team_in, home_team_in, game_spread_fl) values({}, {}, '{}', {}, {}, {});"
                write_sql(sql.format(game_key, series_key, kickoff, away_team_key, home_team_key, 0))
                game_key = game_key + 1

            write_sql("commit;\n")
            print "complete!"
    except Exception as e:
        print e


def write_sql(sql):
    with open(OUTPUT_FILE, 'a') as outfile:
        outfile.write(sql + "\n")


def printHeader():
    write_sql("delete from user_league_xref_tbl where league_no_in = {};".format(LEAGUE_KEY))
    write_sql("delete from user_tbl where league_no_in = {};".format(LEAGUE_KEY))
    write_sql("delete from league_tbl where league_no_in = {};".format(LEAGUE_KEY))
    write_sql("delete from user_game_xref_tbl where game_no_in >= {};".format(FIRST_GAME_KEY))
    write_sql("delete from game_tbl where game_no_in >= {};".format(FIRST_GAME_KEY))
    write_sql("delete from user_series_xref_tbl where series_no_in >= {};".format(FIRST_SERIES_KEY))
    write_sql("delete from season_series_tbl where series_no_in >= {};".format(FIRST_SERIES_KEY))
    write_sql("delete from season_tbl where season_no_in = {};".format(SEASON_KEY))
    write_sql("commit;\n")

    write_sql("update season_tbl set season_active_si = 0;")
    sql = "insert into season_tbl(season_no_in, season_name_vc, sport_type_si, series_prefix_vc, season_active_si)\nvalues({}, '{}', {}, '{}', {});"
    write_sql(sql.format(SEASON_KEY, 'NFL ' + Format(FIRST_SERIES_START, "%Y"), 1, 'Week', 1))

    sql = "insert into league_tbl(league_no_in, season_no_in, league_name_vc, league_public_ti, league_pwd_vc, league_admin_in)\nvalues({}, {}, '{}', {}, {}, {});"
    write_sql(sql.format(LEAGUE_KEY, SEASON_KEY, 'NFL ' + Format(FIRST_SERIES_START, "%Y") + ' - Public League', 1, 'NULL', -1))
    write_sql("commit;\n\n")


def populate_series(kickoff):
    for i in range(0, 17, 1):
        start = FIRST_SERIES_START + datetime.timedelta(7 * i)
        end = start + datetime.timedelta(days=7) - datetime.timedelta(seconds=1)
        # print "get_series({}, {}, {})".format(kickoff, start, end)
        if kickoff >= start and kickoff <= end:
            global series_key, series_start, series_end
            series_key = FIRST_SERIES_KEY + i
            series_start = start
            series_end = end
            return

    raise Exception("kickoff not within any series range: " + Format(kickoff, "%m/%d/%Y %H:%M"))


def get_team_id(team_city):
    team_city = team_city.replace(" ", "").upper()
    # print team_city
    if TEAM_KEYS.get(team_city):
        return TEAM_KEYS[team_city]

    raise Exception("Invalid team city: " + str(team_city))


def CDate(s, f):
    return datetime.datetime.strptime(s, f)


def Format(dt, f):
    return dt.strftime(f)


if __name__ == "__main__":
    main()
