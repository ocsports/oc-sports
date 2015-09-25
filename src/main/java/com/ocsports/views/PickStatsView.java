package com.ocsports.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class PickStatsView implements BaseView {

	public static final int SORT_BY_TEAM = -1;
	public static final int SORT_BY_TOTAL = 0;

	// Map<String, Map<String, String> == Map<teamName, Map<seriesId, pickCount>
	private Map picksByTeam = new LinkedHashMap();
	// the current time when this view was built
	private Date created;

	public PickStatsView() {
		created = new java.util.Date();
	}

	// @Override
	public Object clone() {
		PickStatsView view = new PickStatsView();
		view.setPicksByTeam(picksByTeam);
		view.setCreated(created);

		return view;
	}

	// @Override
	public boolean isStale() {
		long oneHourAgo = new java.util.Date().getTime() - (1000l * 60l * 60l);
		return (created.getTime() < oneHourAgo);
	}

	// @Override
	public String lastUpdated() {
		SimpleDateFormat fmt = new SimpleDateFormat();
		fmt.applyPattern("h:mm a");
		return fmt.format(created);
	}

	public void addPicks(int teamId, int seriesId, int pickCount, String teamName) {
		Map teamPicks = (Map) picksByTeam.get(String.valueOf(teamId));
		if (teamPicks == null) {
			// first time loading picks for this team
			teamPicks = new TreeMap();
			teamPicks.put("teamName", teamName);
			teamPicks.put(String.valueOf(0), String.valueOf(0));
			picksByTeam.put(String.valueOf(teamId), teamPicks);
		}
		int seasonCount = Integer.parseInt((String) teamPicks.get("0"));
		seasonCount += pickCount;

		teamPicks.put("0", String.valueOf(seasonCount));
		teamPicks.put(String.valueOf(seriesId), String.valueOf(pickCount));
	}

	/**
	 * 
	 * @param sortKey   -1=Sort By teamId ASC;  0=Sort By Total Picks DESC;  >1=Sort By Series Picks DESC
	 */
	public void sortTeams(int sortKey) {
		Map sortedTeams = new TreeMap();
		Iterator iter = picksByTeam.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			Map picks = (Map) picksByTeam.get(key);

			String newKey = "";
			switch (sortKey) {
				case SORT_BY_TEAM:
					newKey = (String) picks.get("teamName");
					break;
				default:
					String seriesPickCount = (String) picks.get(String.valueOf(sortKey));
					int pickCount = (seriesPickCount == null ? 0 : Integer.parseInt(seriesPickCount));
					newKey = String.valueOf(9999 - pickCount) + "." + (String) picks.get("teamName");
			}
			sortedTeams.put(newKey, picks);
		}

		picksByTeam = sortedTeams;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Map getPicksByTeam() {
		return picksByTeam;
	}

	public void setPicksByTeam(Map picksByTeam) {
		this.picksByTeam = picksByTeam;
	}
}
