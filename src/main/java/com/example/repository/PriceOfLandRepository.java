package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.domain.PriceOfLand;

/**
 * 地価テーブルへの処理を行うリポジトリ.
 * 
 * @author yoshimatsushouta
 *
 */
@Repository
public class PriceOfLandRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<PriceOfLand> PRICE_ROW_MAPPER = (rs, i) -> {
		PriceOfLand price = new PriceOfLand();
		price.setId(rs.getInt("id"));
		price.setMunicipalityId(rs.getInt("municipality_id"));
		price.setAddressId(rs.getInt("address_id"));
		price.setAddressNum(rs.getString("address_num"));
		price.setCurrentPrice(rs.getInt("current_price"));
		price.setPreviosPrice(rs.getInt("previos_price"));
		price.setChangePreviosPrice(rs.getInt("change_previos_price"));
		price.setAcreage(rs.getInt("acreage"));
		price.setNearestStationId(rs.getInt("nearest_station_id"));
		price.setUsageId(rs.getInt("usage_id"));
		return price;
	};

	/**
	 * 複数の住所IDから地価の情報を引き出すメソッド.
	 * 
	 * @param addressList 住所情報のリスト
	 * @return 地価情報のリスト
	 */
	@SuppressWarnings("null")
	public List<PriceOfLand> getPriceOfLandListByAddressId(int id, String type) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(
				"id, municipality_id, address_id, address_num, current_price, previos_price, change_previos_price, acreage, nearest_station_id, usage_id, neighborhood_usage, info_year ");
		sql.append("FROM price_of_lands ");
		if (type.equals("address")) {
			sql.append("WHERE address_id = :id;");
		} else if (type.equals("station")) {
			sql.append("WHERE nearest_station_id = :id;");
		}
		List<PriceOfLand> priceOfLandList = template.query(sql.toString(), new MapSqlParameterSource("id", id),
				PRICE_ROW_MAPPER);
		return priceOfLandList;
	}
}
