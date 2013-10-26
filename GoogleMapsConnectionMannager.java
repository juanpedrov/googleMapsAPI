package googlemaps;

import java.util.List;

import org.h2.util.StringUtils;

import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author juanpedrov
 *
 */
public class GoogleMapsConnectionMannager {

	private static final String HOST	= "http://maps.googleapis.com";
	private static final String GEOCODE = "/maps/api/geocode/json";
	private List<GeocodeResponse> geocodes;
	private String latlng;
	
	public GoogleMapsConnectionMannager() {
	}	
	
	public List<GeocodeResponse> getGeocode(String address, String components, String latlng) {
				
		if (!StringUtils.isNullOrEmpty(this.latlng) && latlng.equals(this.latlng))
			return geocodes;
		
		this.latlng = latlng;
		return getGeocodeRequest(address, components, latlng);
	}

	public String getLocalityFromTheFirstGeocode() {
		if (geocodes == null || geocodes.size() == 0)
			return "";
		
		for (GeocodeResponse currentGeocode : geocodes) {
			
			for (AddressComponentResponse currentAddressComponent : currentGeocode.getAddress_components()) {
				
				for(String type : currentAddressComponent.getTypes()) {

					if ("locality".equals(type))
						
						return currentAddressComponent.getShort_name();
				}
			}
		}
		
		return "";
	}

	public String getCountryFromTheFirstGeocode() {
		if (geocodes == null || geocodes.size() == 0)
			return "";
		
		for (GeocodeResponse currentGeocode : geocodes) {
			
			for (AddressComponentResponse currentAddressComponent : currentGeocode.getAddress_components()) {
				
				for(String type : currentAddressComponent.getTypes()) {

					if ("country".equals(type))
						
						return currentAddressComponent.getLong_name();
				}
			}
		}
		
		return "";
	}
	
	private List<GeocodeResponse> getGeocodeRequest(String address, String components, String latlng) {
		HttpResponse res;
		WSRequest request;
		
		request = WS.url(HOST + GEOCODE);
		if (address != null && address.length() > 0)
			request.setParameter("address", address);
		if (components != null && components.length() > 0)
			request.setParameter("components", components);
		if (latlng != null && latlng.length() > 0)
			request.setParameter("latlng", latlng);
		request.setParameter("sensor", false);
		res = request.get();
		
		Gson gson = new GsonBuilder().create();
		GoogleMapsApiResponse apiResponse = gson.fromJson(res.getString(), GoogleMapsApiResponse.class);
		
		this.geocodes = apiResponse.getResults();
		return this.geocodes;
	}
}
