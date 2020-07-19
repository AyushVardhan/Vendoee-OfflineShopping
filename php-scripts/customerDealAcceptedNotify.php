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
			
	$date1 = explode(" ", $date);		
	
      $sql = "SELECT * FROM customer_seller_offer_deals WHERE updated_at LIKE '".$date1[0]."%' and seller_confirm='1' and customer_id='$cid'";
      $sth5 = $conn->prepare($sql);
      if($sth5->execute())
      {
      	$count = $sth5->rowCount() ;
      	
      	if($count>1){
	      	while($result5 = $sth5->fetchObject())
	      	{
	      		$contact = $result5->id;
	      		$tmp = $tmp.",".$contact;
	      		$city = substr($tmp,1);
	      	}      		
      	}else{
	      	while($result5 = $sth5->fetchObject())
	      	{
	      		$contact = $result5->id;
	      		$city = $contact;
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