<?php 
	$lat= $_POST['lat'];
        $lng= $_POST['lng'];        
        
	//if(isset($lat) && isset($lng))){	
		$geocodeFromLatLong = file_get_contents('http://maps.googleapis.com/maps/api/geocode/json?latlng='.trim($lat).','.trim($lng).'&sensor=false&output=json'); 
	        $output = json_decode($geocodeFromLatLong);
	        $status = $output->status;
	        //Get address from json data
	        //$address = ($status=="OK")?$output->results[1]->formatted_address:'';
	        if($status == "OK"){
		        //var_dump($output);
		        $formatted_address = $output->results[0]->formatted_address;
		       
		        for($i = 0; $i < count($output->results[0]->address_components); $i++){
		        	switch($output->results[0]->address_components[$i]->types[0]){
		                	case "administrative_area_level_2" : 
		                		$address['city'] = $output->results[0]->address_components[$i]->long_name;
		                		break;
		                	case "administrative_area_level_1" : 
			                	$address['state'] = $output->results[0]->address_components[$i]->long_name;
		                		break;
		                	case "country" :  
		                		$address['country'] = $output->results[0]->address_components[$i]->long_name;
		                		break;
		                	case "postal_code" : 
			                	$address['pin'] = $output->results[0]->address_components[$i]->long_name;
		                		break;			
		                }
		        }
	        }
	        //Return address of the given latitude and longitude
	        if(!empty($address)){
		        echo $formatted_address."||".$address['city']."||".$address['state']."||".$address['pin']."||".$address['country'];
	        }else{
	            echo "loaction not found";
	        }
	//}
	
?>