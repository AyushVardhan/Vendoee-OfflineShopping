<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();

			/*
                        $name = "fgh";
                	$contact = "7903880472";
                        $desc = "hey";
                        $sids = "94,96";
                        */

                        date_default_timezone_set('Asia/Kolkata');
		
                        
                 	$name = $_POST['prodname'];
                	$contact = $_POST['contact'];
                        $desc = $_POST['desc'];
                        $sids = $_POST['sids'];
                        $price = $_POST['price']; 
                        $date1 = $_POST['date'];                                                                       
                        
                        $cid = "0";
                       
                       $sth = $conn->prepare("SELECT id FROM customers WHERE phone_no = '$contact'");
 	              
			if($sth->execute()) {
				 if($sth->rowCount() > 0) {
					  while($result = $sth->fetchObject()) {
					      $cid = $result->id;  
          		  			}
	            		}
	       		}
                       
                        $IdArray = explode(',',$sids);
                        $count = 0;
                        //print_r($IdArray);
                        
                        foreach ($IdArray as $value) {

				$date = date('Y-m-d H:i:s');
	                        $sth = $conn->prepare("INSERT INTO customer_request_offers(customer_id,seller_id,product_name,price,date,product_description,created_at)VALUES(:c,:s,:p,:price,:date,:d,:t)");
	                        $sth->bindParam(':c',$cid);
                        	$sth->bindParam(':s',$value);
                        	$sth->bindParam(':p',$name);
                        	$sth->bindParam(':d',$desc);
                        	$sth->bindParam(':t',$date);
                        	$sth->bindParam(':date',$date1);          
                        	$sth->bindParam(':price',$price);          
                        	$sth->execute();  
                        	$count++;                    	                        	
			}
                        
                        if($count == count($IdArray)){
                        	echo "1";
                        }else{
                        	echo "0";
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