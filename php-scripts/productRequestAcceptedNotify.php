<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                 $phone = $_POST['phone'];
                 //$phone = "8941096130";

                 date_default_timezone_set('Asia/Kolkata');
		    $date = date('Y-m-d');

                        $sql = "SELECT id FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
			if($STH->rowCount()>0){
				   	$result = $STH -> fetch();

		                        $cid = $result ["id"];
					
			}

		

      $sql = "SELECT * FROM offer_requests WHERE created_at LIKE '".$date."%' and customer_id='$cid'";
      $sth5 = $conn->prepare($sql);
      if($sth5->execute())
      {
      	$count = $sth5->rowCount();      	
      	if($count>1){
	      	while($result5 = $sth5->fetchObject())
	      	{
	      		$contact = $result5->id;
	      		$offerid = $result5->offer_id;
	      		
	      		      $sql8 = "SELECT * FROM offers WHERE  verified='1' and id='$offerid'";
			      $sth8 = $conn->prepare($sql8);$sth8->execute();
			      $countit = $sth8->rowCount() ; 
			      
			      if($countit==1){
     
			      	      		$tmp = $tmp.",".$contact;
			      		      	$city = substr($tmp,1);      					      
			     	}
	      	}
      	}else{
	      	while($result5 = $sth5->fetchObject())
	      	{
	      		$contact = $result5->id;
	      		$offerid = $result5->offer_id;	      	
	      	
	      		      $sql8 = "SELECT * FROM offers WHERE  verified='1' and id='$offerid'";
			      $sth8 = $conn->prepare($sql8);$sth8->execute();
			      $countit = $sth8->rowCount() ; 	      	

			      if($countit==1){	      	
				      		$city = $contact;
			     	}
	      	}      		      	
      	}      	

      	
      }
      
      echo $city;	

      //echo $date1[0];
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>