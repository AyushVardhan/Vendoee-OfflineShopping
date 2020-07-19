<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $phone = $_POST['phone'];
			//$sid = '135';
			
			$sql = "SELECT * FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();
                        $cid = $result ["id"];
			
                        $sql = "SELECT distinct(product_name),price,date,product_description FROM `customer_request_offers` WHERE customer_id=".$cid;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        
                        
                        while($result = $STH -> fetch()){

						$pname = $result ["product_name"];
						$pdesc = $result ["product_description"];
						$price = $result ["price"];						
						$date = $result ["date"];
                        			
                        	  echo $pname."||".$pdesc."||".$price."||".$date.";";			
                        }
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

?>