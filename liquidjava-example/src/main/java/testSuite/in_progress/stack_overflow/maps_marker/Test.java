package testSuite.in_progress.stack_overflow.maps_marker;
public class Test {

    void createMarker(GoogleMap map) {
        MarkerOptions options = new MarkerOptions();
        // options.position(new LatLng(lat, lang)); //NEEDED
        // options.anchor()

        System.out.println("LazyMarker - Options var val: "+options.toString());
        System.out.println("LazyMarker - GoogleMap Value:"+map.toString());
        var marker = map.addMarker(options);

        System.out.println("LazyMarker - GoogleMap Marker:"+marker.toString());

    }


    /*
     * STUB CLASSES
     */
    class GoogleMap {
        public Marker addMarker(MarkerOptions options) {
            return new Marker();
        }
    }
    
    class Marker {
        public Marker(){}
    }
    
    class MarkerOptions {
    }
    
    
    
}

