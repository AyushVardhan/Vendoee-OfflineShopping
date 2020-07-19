<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $phone = $_POST['phone'];
                     $pname= $_POST['pname'];
                     
                     //$phone = "8941096130";
                     //$pname= "Samsung phone";
			
			$sql = "SELECT * FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();
                        $cid = $result ["id"];
			
                        $sql = "SELECT id,seller_id FROM customer_request_offers WHERE customer_id='$cid' AND product_name='$pname'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        
                      	if($STH->rowCount()>0){
                      	
                        while($result = $STH -> fetch()){
						
				$reqid = $result ["id"];
				$selid = $result ["seller_id"];
							
	                        $sql = "SELECT name FROM sellers WHERE id='$selid'";
        	                $STH1 = $conn->prepare($sql);
                	        $STH1 -> execute(); $result1 = $STH1 -> fetch();
                	        $shopName = $result1 [name];	
                	        
	                        $sql = "SELECT offer_id from offer_requests WHERE request_offer_id='$reqid'";
        	                $STH2 = $conn->prepare($sql);
                	        $STH2 -> execute(); 
                	        
                	        if($STH2->rowCount()>0){
	                	        $result2 = $STH2 -> fetch();
        	        	        $offId = $result2 [offer_id];
        	        	        
        	        	     		$sql3 = "SELECT id from offers WHERE id='$offId' AND verified='1'";
        	                		$STH3 = $conn->prepare($sql3);
                	        		$STH3 -> execute(); 
                	        		
                	        		if($STH3->rowCount()>0){
                	        		
                	        		}else{
                	        			$offId = "-1";
                	        		}
                	        }else{
                	        	$offId = "-1";
                	        }	                	        					
                        
                        	echo $reqid.",".$selid.",".$shopName.",".$offId.";";					
                        }
                      	
                      	}else{
                      		echo 0;
                      	}
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

?>