<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 
               $oid = $_POST['id'];
               
               $ipaddress = '';
        if (isset($_SERVER['HTTP_CLIENT_IP']))
            $ipaddress = $_SERVER['HTTP_CLIENT_IP'];
        else if(isset($_SERVER['HTTP_X_FORWARDED_FOR']))
            $ipaddress = $_SERVER['HTTP_X_FORWARDED_FOR'];
        else if(isset($_SERVER['HTTP_X_FORWARDED']))
            $ipaddress = $_SERVER['HTTP_X_FORWARDED'];
        else if(isset($_SERVER['HTTP_FORWARDED_FOR']))
            $ipaddress = $_SERVER['HTTP_FORWARDED_FOR'];
        else if(isset($_SERVER['HTTP_FORWARDED']))
            $ipaddress = $_SERVER['HTTP_FORWARDED'];
        else if(isset($_SERVER['REMOTE_ADDR']))
            $ipaddress = $_SERVER['REMOTE_ADDR'];
        else
            $ipaddress = 'UNKNOWN';

                 $q1= "SELECT ip_address FROM offer_ip_addresses WHERE offer_id= $oid AND ip_address = '$ipaddress'";
		$statement1 = $conn ->prepare($q1);
		$status1 = $statement1->execute();   
		
		if (($status1) && ($statement1->rowCount() > 0))
		{
		               
                 $q= "SELECT hit_count FROM offer_hit_counters WHERE offer_id= $oid";
		$statement = $conn ->prepare($q);
		$status = $statement->execute();
		
		$result = $statement -> fetch();
		$count = $result["hit_count"];
		$count = $count + 1;

		$sth3 = $conn->prepare("UPDATE offer_hit_counters SET hit_count = :ph WHERE offer_id = $oid");
			
                $sth3->bindParam(':ph',$count);
		$sth3->execute();

		}else{
		
                 $q= "SELECT hit_count,unique_hit_count FROM offer_hit_counters WHERE offer_id= $oid";
		$statement = $conn ->prepare($q);
		$status = $statement->execute();
		
		$result = $statement -> fetch();
		
		$count = $result["hit_count"];
		$count = $count + 1;
		
		$count1 = $result["unique_hit_count"];
		$count1 = $count1 + 1;

		$sth3 = $conn->prepare("UPDATE offer_hit_counters SET hit_count = :ph,unique_hit_count = :ph1 WHERE offer_id = $oid");
			
                $sth3->bindParam(':ph',$count);
                $sth3->bindParam(':ph1',$count1);
		$sth3->execute();		
		
		$sth4 = $conn->prepare("INSERT INTO offer_ip_addresses(offer_id,ip_address)VALUES(:sid,:ph)");

                        $sth4->bindParam(':sid',$oid);
                        $sth4->bindParam(':ph',$ipaddress);

                        $sth4->execute();

		
		}         
	}
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>