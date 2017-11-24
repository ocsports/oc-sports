#!/usr/bin/python
import csv
import datetime
import os

# constants to be adjusted for each new season
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

# static constants
ONE_WEEK = datetime.timedelta(days=7)
ONE_SECOND = datetime.timedelta(seconds=1)
TEAM_KEYS = {
    1: ["ARI", "ARIZONA", "CARDINALS"],
    2: ["ATL", "ATLANTA", "FALCONS"],
    3: ["BAL", "BALTIMORE", "RAVENS"],
    4: ["BUF", "BUFFALO", "BILLS"],
    5: ["CAR", "CAROLINA", "PANTHERS"],
    6: ["CHI", "CHICAGO", "BEARS"],
    7: ["CIN", "CINCINNATI", "BENGALS"],
    8: ["CLE", "CLEVELAND", "BROWNS"],
    9: ["DAL", "DALLAS", "COWBOYS"],
    10: ["DEN", "DENVER", "BRONCOS"],
    11: ["DET", "DETROIT", "LIONS"],
    12: ["GB", "GREEN BAY", "PACKERS"],
    13: ["HOU", "HOUSTON", "TEXANS"],
    14: ["IND", "INDIANAPOLIS", "COLTS"],
    15: ["JAX", "JACKSONVILLE", "JAGUARS"],
    16: ["KC", "KANSAS CITY", "CHIEFS"],
    17: ["MIA", "MIAMI", "DOLPHINS"],
    18: ["MIN", "MINNESOTA", "VIKINGS"],
    19: ["NE", "NEW ENGLAND", "PATRIOTS"],
    20: ["NO", "NEW ORLEANS", "SAINTS"],
    21: ["NYG", "NEW YORK", "GIANTS", "NYGIANTS"],
    22: ["NYJ", "NEW YORK", "JETS", "NYJETS"],
    23: ["OAK", "OAKLAND", "RAIDERS"],
    24: ["PHI", "PHILADELPHIA", "EAGLES"],
    25: ["PIT", "PITTSBURGH", "STEELERS"],
    26: ["LAC", "LOS ANGELES", "CHARGERS"],
    27: ["SF", "SAN FRANCISCO", "49ERS"],
    28: ["SEA", "SEATTLE", "SEAHAWKS"],
    29: ["LAR", "LOS ANGELES", "RAMS"],
    30: ["TB", "TAMPA BAY", "BUCCANEERS"],
    31: ["TEN", "TENNESSEE", "TITANS"],
    32: ["WAS", "WASHINGTON", "REDSKINS"]
}

series_key = 0
series_start = None
series_end = None


def main():
    start = datetime.datetime.now()
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

            end = datetime.datetime.now()
            ms = int(round((end - start).total_seconds()*1000, 0))
            print "completed in {}ms!".format(ms)
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
        end = start + ONE_WEEK - ONE_SECOND
        if kickoff >= start and kickoff <= end:
            global series_key, series_start, series_end
            series_key = FIRST_SERIES_KEY + i
            series_start = start
            series_end = end
            return

    raise Exception("kickoff not within any series range: {}".format(
                    Format(kickoff, "%m/%d/%Y %H:%M")))


def get_team_id(team_city):
    team_city = team_city.replace(" ", "").upper()
    for team_id, team_values in TEAM_KEYS.iteritems():
        full_name = team_values[1] + team_values[2]
        if full_name.replace(" ", "").upper() == team_city:
            return team_id
        for v in team_values:
            if v.replace(" ", "").upper() == team_city:
                return team_id

    raise Exception("Invalid team city: " + str(team_city))


def CDate(s, f):
    return datetime.datetime.strptime(s, f)


def Format(dt, f):
    return dt.strftime(f)


if __name__ == "__main__":
    main()
