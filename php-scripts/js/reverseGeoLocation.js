function reverseGeoToAddress(lat,lng) {
    var geocoder = new google.maps.Geocoder();
    var latlng = new google.maps.LatLng(lat, lng);
    console.log(latlng);

    geocoder.geocode(
        {'latLng': latlng},
        function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (results[0]) {
                	address = [];
                    console.log(results[0]['address_components']);
                    //$j('#searchInput').val(results[0].formatted_address)
                    for (var i = 0; i < results[0]['address_components'].length; i++) {
                        var addressType = results[0]['address_components'][i].types[0];
                        //if (componentForm[addressType]) {
                            var val = results[0]['address_components'][i].long_name;
                            address.push(val);
                        //}
                    }
                    console.log(address);
                    window.location = "./getAddressResult.php?vishal=" + address;
                }
                else  {
                    alert("Failed to Retrive Location, please try again");
                }
            }else {
                alert("Geocoder failed due to: " + status);
            }
    });
}