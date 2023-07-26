package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.Address;
import com.example.domain.Hittakuri;
import com.example.domain.MapInfo;
import com.example.domain.PriceOfLand;
import com.example.domain.Station;
import com.example.domain.TabItem;
import com.example.service.HittakuriService;
import com.example.service.PriceOfLandService;
import com.example.service.SearchByAddressService;
import com.example.service.StationService;

/**
 * map表示コントローラー.
 * 
 * @author yoshida_yuuta
 */
@RequestMapping("/map")
@Controller
public class ShowMapController {
	@Autowired
	private StationService stationService;
	@Autowired
	private SearchByAddressService searchByAddressService;
	@Autowired
	private PriceOfLandService priceOfLandService;
	@Autowired
	private HittakuriService hittakuriService;

	/**
	 * map表示のメソッド
	 * 
	 * @param model          モデル
	 * @param id             DBのid
	 * @param infomationType idの種類(addressかstationの物か)
	 * @return 地図表示画面
	 */
	@PostMapping("/")
	public String postIndex(Model model, Integer id, String infomationType) {
		MapInfo mapInfo = new MapInfo();
		if (infomationType.equals("station")) {
			Station station = stationService.load(id);
			mapInfo.setLatitude(station.getLatitude());
			mapInfo.setLongitude(station.getLongitude());
		}
		// 再度実行する際に必要になります。
		if (infomationType.equals("address")) {
			Address address = searchByAddressService.load(id);
			mapInfo.setLatitude(address.getLatitude());
			mapInfo.setLongitude(address.getLongitude());
		}
		model.addAttribute("id", id);
		model.addAttribute("infomationType", infomationType);
		model.addAttribute("mapInfo", mapInfo);

		return "map";
	}

	/**
	 * 地下情報の取得.
	 *
	 * @param requestData Vueで送信したデータ(緯度、経度、地図倍率)
	 * @return createMapTag.vue fetchData
	 */
	@PostMapping("/getprice")
	@ResponseBody
	public Map<String, List<PriceOfLand>> getPrice(@RequestBody Map<String, String> requestData) {
		int id = Integer.parseInt(requestData.get("id"));
		String type = requestData.get("type");
		List<PriceOfLand> priceList = priceOfLandService.selectPriceOfRepositoryByMapInfo(id, type);
		// 表示する地域の座標を元にひったくり情報を送る
		Map<String, List<PriceOfLand>> data = new HashMap<>();
		data.put("priceList", priceList);
		return data;
	}

	/**
	 * 犯罪情報の取得.
	 * 
	 * @param requestData Vueで送信したデータ(緯度、経度、地図倍率)
	 * @return createMapTag.vue fetchData
	 */

	@PostMapping("/getcrime")
	@ResponseBody
	public Map<String, List<Hittakuri>> getCrime(@RequestBody Map<String, String> requestData) {
		List<Hittakuri> hittakuriList = new ArrayList<>();
		int id = Integer.parseInt(requestData.get("id"));
		String type = requestData.get("type");
		if (type.equals("station")) {
		} else {
			// 表示する地域の座標を元にひったくり情報を送る
			hittakuriList = hittakuriService.selectHittakuriOfRepositoryByMapInfo(id, type);
		}
		// 上と同じようにListをつくり、mapに詰めればhtmlで使用可能
		Map<String, List<Hittakuri>> data = new HashMap<>();
		data.put("hittakuriList", hittakuriList);
		return data;
	}

	/**
	 * 近隣駅、地区検索.
	 * 
	 * @param requestData 表示地区の緯度経度
	 * @return menuTab.vue
	 */
	@PostMapping("/getOptions")
	@ResponseBody
	public Map<String, List<TabItem>> getOptions(@RequestBody Map<String, String> requestData) {
		Double longitude = Double.parseDouble(requestData.get("longitude"));
		Double latitude = Double.parseDouble(requestData.get("latitude"));

		List<TabItem> addressOptions = searchByAddressService.getAddressByLonLat(longitude, latitude);
		List<TabItem> stationOptions = stationService.getStationByLonLat(longitude, latitude);
		Map<String, List<TabItem>> data = new HashMap<>();
		data.put("addressOptions", addressOptions);
		data.put("stationOptions", stationOptions);
		return data;
	}
}
