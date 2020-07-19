<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                
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
                
                     $id = 0; $subid=0;
               $main_cat = $_POST['cat'];		$city= $_POST['city'];
               $sub_cat = $_POST['subcat'];		$state= $_POST['state'];
               $clat = $_POST['latitude'];
               $clng = $_POST['longitude'];
                                        
                                      /*     
               $main_cat = 'Electronics';
               $sub_cat = 'Mobiles & Accessories';
               $clat = "30.4138632";
               $clng = "77.9715005";  */
               
               $sth = $conn->prepare("SELECT id FROM seller_categories WHERE name = '$main_cat'");
               
		if($sth->execute()) {
		
     
		 if($sth->rowCount() > 0) {

			  while($result = $sth->fetchObject()) {
			      $id = $result->id;  
          		  }
	            }
	       }
	       
               if($sub_cat!="0"){
               $sth = $conn->prepare("SELECT id FROM product_categories WHERE seller_category_id = '$id' AND name = '$sub_cat'");
               
		if($sth->execute()) {
		
     
		 if($sth->rowCount() > 0) {

			  while($result = $sth->fetchObject()) {
			      $subid = $result->id;  
          		  }
	            }
	       }
               }else{
               		$subid = $sub_cat;
               }	       
	       
			        
		
	       $data = ""; $count =  0;
	       $array = array();
	       
		$sth3 = $conn->prepare("SELECT DISTINCT(seller_id) FROM seller_intermediate_subcategory WHERE seller_category_id ='$id' AND seller_subcategory_id = '$subid'");
		if($sth3->execute()) {
 	 	   
  
		  while($result3 = $sth3->fetchObject()) {
      				$sid = $result3->seller_id;
      				
      					//echo $sid;
      					
      					$sth2 = $conn->prepare("SELECT sellers.name,sellers.image,seller_addresses.lat,seller_addresses.lng FROM sellers JOIN seller_addresses ON sellers.id=seller_addresses.seller_id WHERE sellers.id = $sid AND seller_addresses.city='$city' AND seller_addresses.state='$state' AND sellers.verified='1'");
      					
				      	if($sth2->execute())
				      	{	
				      		while($result2 = $sth2->fetchObject())
				      		{	
				      			$tempArray = array();
				      			$name = $result2->name;
							$image = $result2->image;
							$km = distanceCalculation($clat,$clng,$result2->lat,$result2->lng);
							//echo $name.",".$km.",".$sid.";";
							
							$tempArray['id'] = $sid;
							$tempArray['name'] = $name;
							$tempArray['distance'] = $km;
							//$tempArray['image'] = $image;
							
							array_push($array, $tempArray);
							
							/*
							$array["$count"]['name'] =  $name";
							$array["$count"]['distance'] =  $km";
							$array["$count"]['id'] =  $sid";
							$array["$count"]['image'] =  $image";
							*/
				      		}
				      		
				      	}    
				      				
      		
          	  }
    		}
	$data1 = "";
								
	$price = array();
	foreach ($array as $key => $row)
	{
    		$price[$key] = $row['distance'];
	}
	
	array_multisort($price, SORT_ASC, $array);
	
	
	foreach($array as $key => $row){
		$name1 = $row['name']." (Dist: ".$row['distance']." km)(".$row['id'].")";
		$data1 = $data1.",".$name1;
	}
	
	echo $data1;

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }
            	

	  mysqli_close($con);
?>