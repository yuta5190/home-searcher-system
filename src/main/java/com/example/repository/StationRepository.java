package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Station;
import com.example.domain.TabItem;

/**
 * stationsテーブルを操作するリポジトリ.
 * 
 * @author yoshimatsushouta
 *
 */
@Repository
public class StationRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * Stationオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Station> STATION_ROW_MAPPER = new BeanPropertyRowMapper<>(Station.class);

	/**
	 * TabItemオブジェクトを生成するローマッパー
	 */
	private static final RowMapper<TabItem> TabItem_ROW_MAPPER = (rs, i) -> {
		TabItem tabItem = new TabItem();
		tabItem.setName(rs.getString("station_name"));
		tabItem.setId(rs.getInt("id"));
		return tabItem;
	};

	/**
	 * stationsテーブルを全県検索する.
	 * 
	 * @return 駅情報のリスト
	 */
	public List<Station> findAll() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT");
		sql.append(" id, station_name, line_id, post, address, longitude, latitude");
		sql.append(" FROM");
		sql.append(" stations");
		sql.append(" ORDER BY id");

		List<Station> stationList = template.query(sql.toString(), STATION_ROW_MAPPER);
		return stationList;
	}

	/**
	 * lineIdから検索を行う.
	 * 
	 * @param lineId 沿線ID
	 * @return 駅情報のリスト
	 */
	public List<Station> findByLineId(Integer lineId) {
		SqlParameterSource param = new MapSqlParameterSource().addValue("lineId", lineId);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT");
		sql.append(" id, station_name, line_id, post, address, longitude, latitude");
		sql.append(" FROM");
		sql.append(" stations");
		sql.append(" WHERE");
		sql.append(" line_id = :lineId");
		sql.append(" ORDER BY id");

		List<Station> stationList = template.query(sql.toString(), param, STATION_ROW_MAPPER);
		return stationList;
	}

	/**
	 * 主IDから駅情報を呼び出す.
	 * 
	 * @param stationId 駅ID
	 * @return 駅情報を返します 返せるものがなければnullを返します
	 */
	public Station load(Integer stationId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT");
		sql.append(" id, station_name, line_id, post, address, longitude, latitude");
		sql.append(" FROM");
		sql.append(" stations");
		sql.append(" WHERE");
		sql.append(" id = :stationId");
		SqlParameterSource param = new MapSqlParameterSource().addValue("stationId", stationId);
		List<Station> getStation = template.query(sql.toString(), param, STATION_ROW_MAPPER);
		if (getStation.size() == 0) {
			return null;
		}
		return getStation.get(0);
	}

	/**
	 * 緯度経度から近隣の駅情報を５件取得するメソッド
	 * 
	 * @param longitude 経度
	 * @param latitude  緯度
	 * @return 駅情報
	 */
	public List<TabItem> getStationByLonLat(double longitude, double latitude) {
		SqlParameterSource param = new MapSqlParameterSource().addValue("targetLongitude", longitude)
				.addValue("targetLatitude", latitude);
		String sql = "SELECT id, station_name FROM stations WHERE longitude != :targetLongitude AND latitude != :targetLatitude ORDER BY POWER((longitude - :targetLongitude), 2) + POWER((latitude - :targetLatitude), 2) LIMIT 5;";
		List<TabItem> stationOptions = template.query(sql, param, TabItem_ROW_MAPPER);
		return stationOptions;
	}

}
