package googlemaps;

import java.util.Iterator;
import java.util.List;

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
	
	public GoogleMapsConnectionMannager() {
	}	
	
	public List<GeocodeResponse> getGeocode(String address, String components, String latlng) {
				
		return getGeocodeRequest(address, components, latlng);
	}

	public String getLocalityFromTheFirstGeocode() {
		if (geocodes == null || geocodes.size() == 0)
			return "";
		
		for (Iterator<GeocodeResponse> iterator = geocodes.iterator(); iterator.hasNext();) {
			GeocodeResponse currentGeocode = iterator.next();
			
			for (Iterator<AddressComponentResponse> iterator2 = currentGeocode.getAddress_components().iterator(); iterator2.hasNext();) {
				AddressComponentResponse currentAddressComponent = iterator2.next();	
				
				for(Iterator<String> iterator3 = currentAddressComponent.getTypes().iterator(); iterator3.hasNext();) {
					String type = iterator3.next();
					
					if ("locality".equals(type))
						
						return currentAddressComponent.getShort_name();
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
