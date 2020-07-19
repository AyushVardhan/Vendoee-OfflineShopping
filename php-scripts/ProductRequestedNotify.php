<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                 $sid = $_POST['sid'];
                 //$sid = "1";

                 date_default_timezone_set('Asia/Kolkata');
		    $date = date('Y-m-d');
			
	$date1 = explode(" ", $date);		
	//echo $date1[0];
      $sql = "SELECT * FROM customer_request_offers WHERE created_at LIKE '".$date1[0]."%' and seller_id='$sid'";
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