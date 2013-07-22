package googlemaps;

import java.util.List;

public class GeocodeResponse {

	private GeometryResponse geometry;
	
	private List<AddressComponentResponse> address_components;

	public GeometryResponse getGeometry() {
		return geometry;
	}	
	
	public double[] northeastBound() {
		
		return this.geometry.getBounds().getNortheast().getLocation();
	}
	
	public double[] southwestBound() {
		
		return this.geometry.getBounds().getSouthwest().getLocation();
	}

	public List<AddressComponentResponse> getAddress_components() {
		return address_components;
	}	
}
