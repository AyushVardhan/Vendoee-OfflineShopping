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
			
                        $sql = "select id,customer_id,offer_id,price from customer_seller_offer_deals where seller_id = '$sid' and seller_confirm = '0'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        
                        
                        while($result = $STH -> fetch()){
                                               	$cid = $result ["customer_id"];
						$oprice = $result ["price"];
						$oid = $result ["offer_id"];
						$id = $result ["id"];
			
						$sql1 = "select product_name,image from offers where id=".$oid ;
                        			$STH1 = $conn->prepare($sql1);
                        			$STH1 -> execute();
                        			$result1 = $STH1 -> fetch();
                        
                        			$pname = $result1 ["product_name"];
                        			$pimage = $result1 ["image"];
                        			
                        			$sql2 = "SELECT name,phone_no FROM customers WHERE id=".$cid;
                        			$STH2 = $conn->prepare($sql2);
                        			$STH2 -> execute();
						if($STH2->rowCount()>0){
				   			$result2 = $STH2 -> fetch();

		                        		$cname = $result2 ["name"];
							$cphone = $result2 ["phone_no"];
						}
                        			
                        	  echo $id."||".$pname."||".$pimage."||".$cname."||".$cphone."||".$oprice.";";			
                        }
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

?>