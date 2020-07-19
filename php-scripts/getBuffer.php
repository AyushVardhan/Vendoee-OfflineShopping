<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');


        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                 $array = array();

               $sth = $conn->prepare("SELECT seller_id,lat,lng FROM seller_addresses");
               
		if($sth->execute()) {
     
			 if($sth->rowCount() > 0) {
  
 				 while($result = $sth->fetchObject()) {
    						  $sid = $result->seller_id; $lat = $result->lat; $lng = $result->lng;
    						  
    						        $sth1 = $conn->prepare("SELECT owner_name FROM sellers WHERE id =$sid");
							      if($sth1->execute())
      								{
								      	while($result1 = $sth1->fetchObject())
      									{
      											$name = $result1->owner_name;
      									}
      								}
    						array_push($array,array('name'=>$name,'lat'=>$lat,'lng'=>$lng));	  
      					    }
  				      }
  				      header('Content-type: application/json');
  				      echo json_encode($array);
    			} 
		$conn->commit();
            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>