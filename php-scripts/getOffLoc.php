<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $array = array();
                     
               $oid = $_POST['id'];

               $sth = $conn->prepare("SELECT seller_id FROM offers WHERE id = $oid ");
               
		if($sth->execute()) {
     
			 if($sth->rowCount() > 0) {
  
				  while($result = $sth->fetchObject()) {
     					 $SID = $result->seller_id;

					$sth1 = $conn->prepare("SELECT lat,lng FROM seller_addresses WHERE seller_id=$SID");
               
					if($sth1->execute()) {
     
					 if($sth1->rowCount() > 0) {
  
						  while($result1 = $sth1->fetchObject()) {
							      $LAT = $result1->lat;
							      $LNG = $result1->lng;
          								}
        							}
    							} 

          				}
        			}
    			} 
		echo $LAT.",".$LNG;
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>