package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.PriceOfLand;
import com.example.repository.AddressRepository;
import com.example.repository.PriceOfLandRepository;

/**
 * 地価情報の業務処理を行うサービスクラス.
 * 
 * @author yoshimatsushouta
 *
 */
@Service
@Transactional
public class PriceOfLandService {

	@Autowired
	public AddressRepository addressRepository;

	@Autowired
	public PriceOfLandRepository priceOfLandRepository;

	/**
	 * 経度と緯度から地価情報のリストを返すメソッド.
	 * 
	 * @param mapInfo 経度と緯度の情報
	 * @return 地価リスト
	 */
	public List<PriceOfLand> selectPriceOfRepositoryByMapInfo(int id,String type) {

		List<PriceOfLand> priceOfLandList = priceOfLandRepository.getPriceOfLandListByAddressId(id,type);

		return priceOfLandList;
	}

}
