<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                 date_default_timezone_set('Asia/Kolkata');
		    $date = date('Y-m-d');

			//$date1 = str_replace('-', '/', $date);
			//$yesterday = date('Y-m-d',strtotime($date1 . "-1 days"));
			
	$sql = "SELECT id FROM offers WHERE start_date = '".$date."' and verified='1'";
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

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>