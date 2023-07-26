package com.example.domain;

import lombok.Data;

/**
 * Mapに打つピンの情報を取得するオブジェクト.
 * 
 * @author yoshida_yuuta
 */
@Data
public class PinInfo {
private String name;
private Double latitude;
private Double longitude;
private String detail;
}
