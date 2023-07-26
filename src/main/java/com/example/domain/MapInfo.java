package com.example.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 緯度経度を管理するdomain.
 * 
 * @author yoshida_yuuta
 */
@Getter
@Setter
@ToString
public class MapInfo {
	private String Name;
	private Double Longitude;
	private Double Latitude;
}
