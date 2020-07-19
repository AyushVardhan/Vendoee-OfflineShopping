<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $sid = $_POST['sid'];
			//$sid = '1';
			
                        $sql = "select id,customer_id,product_name,price,date,time,product_description from customer_request_offers where seller_id =".$sid;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        
                        
                        while($result = $STH -> fetch()){
                        
                                              	$idR = $result ["id"];    

                                                $sql1 = "SELECT id FROM offer_requests WHERE request_offer_id="."'"."$idR"."'";
			                        $statement1 = $conn->prepare($sql1);
						$statement1->execute(); 
						if($statement1->rowCount()==0){
						
	                                               	$cid = $result ["customer_id"];
							$pname = $result ["product_name"];
							$pdesc = $result ["product_description"];
							$price = $result ["price"];						
							$date = $result ["date"];
							$time = $result ["time"];
																
							$sql1 = "select name,phone_no from customers where id=".$cid ;
	                        			$STH1 = $conn->prepare($sql1);
	                        			$STH1 -> execute();
	                        			$result1 = $STH1 -> fetch();
	                        
	                        			$cname = $result1 ["name"];
	                        			$cnumb = $result1 ["phone_no"];
	                        			
	                        	  echo $pname."||".$pdesc."||".$price."||".$date."||"."000"."||".$cid."||".$cname."||".$cnumb."||".$idR.";";
	                        	  						
						}
          			
                        }
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

?>