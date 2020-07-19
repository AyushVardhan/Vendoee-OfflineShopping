<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 
                     
               $oid = $_POST['id'];
               $phonenum = $_POST['phone'];
               
               //$oid = "13";
               //$phonenum = "8941096130";
               
                $sql12 = "SELECT id FROM customers WHERE phone_no=".$phonenum;
                $STH12 = $conn->prepare($sql12);
                $STH12 -> execute();
		$result12 = $STH12 -> fetch();
                $cid = $result12 ["id"];
									

                $sql13 = "SELECT * FROM offer_customer_love_relations WHERE customer_id=$cid AND offer_id=$oid";
                $STH13 = $conn->prepare($sql13);
                $STH13 -> execute();
		if($STH13->rowCount()>0){
			$loveStat = "YES";
		}else{
			$loveStat = "NO";		
		}
			
               
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
		
               $sth = $conn->prepare("SELECT offers.image,name, product_name, discount, love, seller_category_id,product_category_id,seller_id,offers.description,price,offer_price,start_date,end_date FROM offers, sellers WHERE offers.seller_id = sellers.id AND offers.id= $oid");
               
		if($sth->execute()) {
     
 			if($sth->rowCount() > 0) {
  
  				while($result = $sth->fetchObject()) {
  				
      					$sellID = $result->seller_category_id;
      					$prodID = $result->product_category_id;
      					$sid = $result->seller_id;
      					      					
      					$sth1 = $conn->prepare("SELECT name FROM seller_categories WHERE id = $sellID ");
					      if($sth1->execute())
     						 {
      							while($result1 = $sth1->fetchObject())
      							{
      								$seller_catname = $result1->name;
      							}
    						  }
    						  
    					$sth2 = $conn->prepare("SELECT name FROM product_categories WHERE id = $prodID ");
      						if($sth2->execute())
      							{
							      	if(($sth2->rowCount() > 0)){
							      		while($result2 = $sth2->fetchObject())
							      		{
								      		$product_catname = $result2->name;
							       		}       		
							      	}else{
							      		$product_catname = "Others";
      								}
      							}

					      $sth6 = $conn->prepare("SELECT hit_count FROM offer_hit_counters WHERE offer_id= $oid");
					      if($sth6->execute())
					      {
					      	if(($sth6->rowCount() > 0)){
					      		$result6 = $sth6-> fetch();
							$viewCount = $result6["hit_count"];
					      	}else{
					      		$viewCount = 0;
					      	}
					      }

					      $sth5 = $conn->prepare("SELECT phone_no FROM seller_contacts WHERE seller_id =$sid LIMIT 1");
					      if($sth5->execute())
					      {
					      	while($result5 = $sth5->fetchObject())
					      	{
					      		$contact = $result5->phone_no;
					      	}
					      }
      							     							
      					
      					echo $result->image."||";
      					echo $result->name."||";echo $result->product_name."||";
      					echo $seller_catname."||";echo $product_catname."||"; echo $result->description."||";echo $result->price."||";
      					echo $result->offer_price."||";
      					echo $result->start_date."||";echo $result->end_date."||"; echo $loveStat."||";echo $result->discount."||";echo $result->love."||";
          				echo $viewCount."||"; echo $contact."||"; echo $sid;
          				}
        			}
    			} 

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>