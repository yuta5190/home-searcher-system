package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Hittakuri;
import com.example.repository.HittakuriRepository;

/**
 * ひったくり情報の業務処理を行うサービスクラス.
 * 
 * @author yoshimatsushouta
 *
 */
@Service
@Transactional
public class HittakuriService {

	@Autowired
	public HittakuriRepository hittakuriRepository;

	/**
	 * 経度と緯度からひったくり情報のリストを返すメソッド.
	 * 
	 * @param mapInfo 経度と緯度の情報
	 * @return ひったくり情報リスト
	 */
	public List<Hittakuri> selectHittakuriOfRepositoryByMapInfo(int id, String type) {
		List<Hittakuri> hittakuriList = hittakuriRepository.getHittakuriByAddressId(id, type);
		return hittakuriList;
	}

}
