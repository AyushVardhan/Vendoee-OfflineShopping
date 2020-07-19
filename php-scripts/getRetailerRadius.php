<?php
define('HOST','localhost');
define('USER','psVishalPs');
define('PASS','vendoe1232');
define('DB','vendoe');

                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 
                 date_default_timezone_set('Asia/Kolkata');
		    $date = date('Y-m-d');
		    
		    	
	$radius = $_POST['rad'];
	$lat = $_POST['latit'];
	$longitude = $_POST['longit'];
	$catid = $_POST['cid'];
	
	/*
	$radius = $_POST['rad'];
	$lat = $_POST['latit'];
	$longitude = $_POST['longit'];
	
	$radius = "50";
	$lat = "30.4138715";
	$longitude = "77.9715897";
	
	
	$radius = "0";
	$lat = "30.4138632";
	$longitude = "77.9715005";	
	$catid = 0;
	*/

function distanceCalculation($point1_lat, $point1_long, $point2_lat, $point2_long, $unit = 'km', $decimals = 2) {
	// Calculate the distance in degrees
	$degrees = rad2deg(acos((sin(deg2rad($point1_lat))*sin(deg2rad($point2_lat))) + (cos(deg2rad($point1_lat))*cos(deg2rad($point2_lat))*cos(deg2rad($point1_long-$point2_long)))));
 
	// Convert the distance in degrees to the chosen unit (kilometres, miles or nautical miles)
	switch($unit) {
		case 'km':
			$distance = $degrees * 111.13384; // 1 degree = 111.13384 km, based on the average diameter of the Earth (12,735 km)
			break;
		case 'mi':
			$distance = $degrees * 69.05482; // 1 degree = 69.05482 miles, based on the average diameter of the Earth (7,913.1 miles)
			break;
		case 'nmi':
			$distance =  $degrees * 59.97662; // 1 degree = 59.97662 nautic miles, based on the average diameter of the Earth (6,876.3 nautical miles)
	}
	return round($distance, $decimals);
}	
	
	
	
	$radius = (float)$radius*1000;

	if($catid==0){
		$sth = $conn->prepare("SELECT seller_id,lat,lng FROM seller_addresses INNER JOIN sellers ON sellers.id = seller_addresses.seller_id WHERE sellers.verified='1'");	
	}else{
		$sth = $conn->prepare("SELECT seller_id,lat,lng FROM seller_addresses INNER JOIN sellers ON sellers.id = seller_addresses.seller_id WHERE sellers.verified='1' AND seller_id IN(SELECT seller_id FROM seller_intermediate_categories WHERE seller_category_id='$catid')");
	}
		
	if($sth->execute()) {
     
		 if($sth->rowCount() > 0) {
  
			  while($result = $sth->fetchObject()) {
			  
      				$sid = $result->seller_id; $latit = $result->lat; $longit = $result->lng;
      				
      				/*
      				    $ch = curl_init();
				    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    					curl_setopt($ch, CURLOPT_URL, 
				        "http://maps.googleapis.com/maps/api/directions/json?origin=" . "$lat" . "," . "$longitude" . "&destination=" . "$latit" . "," . "$longit" . "&sensor=false&units=metric&mode=driving");
    				    $content = curl_exec($ch);
    				    $content = json_decode($content);
    				    echo "<pre>";
    				    print_r($content);
    				    echo "</pre>";
    				    $content1 = $content->routes[0]->legs[0]->distance->text;
    				    */
    				    
    			$km = distanceCalculation($lat,$longitude,$latit,$longit);

			$cval = (float)$km*1000;
    				   
    				  
    				   
    				    if($cval<=$radius){
    						
    						$sql = "select name from sellers where id = ".$sid;
			                        $STH = $conn->prepare($sql);
			                        $STH -> execute();
			                        $result = $STH -> fetch();
						$name = $result ["name"];
						
						$sql1 = "select phone_no from seller_contacts where seller_id = ".$sid." LIMIT 1";
                        			$STH1 = $conn->prepare($sql1);
                        			$STH1 -> execute();
                        			$result1 = $STH1 -> fetch();                        
                        			$contact= $result1 ["phone_no"];	

						$sql18 = "SELECT end_date FROM offers WHERE seller_id = '$sid' and verified = '1' and end_date > '$date'";
                        			$STH18 = $conn->prepare($sql18);
                        			$STH18 -> execute();
                        			
                        			if($STH18->rowCount() > 0){
                        				$val = "live";
                        			}else{
					            $val = "end";
					        }
                        			
                        			$category = "";
					        $sth5 = $conn->prepare("select cat_name from seller_intermediate_categories where seller_id = ".$sid);
      						if($sth5->execute())
      						{
      							while($result5 = $sth5->fetchObject())
      							{
      								$tmp = $result5->cat_name;
      								$category = $tmp.";".$category;
      							}
      						}					
						
						//echo $sid." ".$cval." ".$content1."</br>";
						//echo "$name"."</br>";
						//echo  $sid."|".$latit."|".$longit."|".$name."|".$contact."|".$category."|".$radius."~";
						echo  $sid."|".$latit."|".$longit."|".$name."|".$contact."|".$category."|".$radius."|".$val."~";
    				    }
		    
          		}     
        	}else{
        	echo "";
        	}
    	}

mysqli_close($con);

?>