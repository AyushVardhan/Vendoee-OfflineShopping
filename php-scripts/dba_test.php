<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $id = $_POST['email'];

                        $sql = "select id from sellers where email = '".$id."'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();
                        
			if($result){
				$pin = $result ["id"];
				
				$STH = $conn->prepare("DELETE FROM sellers WHERE id = $pin");
                        	$res = $STH -> execute();
                        
                        	$STH1 = $conn->prepare("DELETE FROM seller_addresses WHERE seller_id = $pin");
                        	$res1 = $STH1 -> execute();
                        	
                        	$STH2 = $conn->prepare("DELETE FROM seller_contacts WHERE seller_id = $pin");
                        	$res2 = $STH2 -> execute();
                        	
                        	$STH3 = $conn->prepare("DELETE FROM seller_intermediate_categories WHERE seller_id = $pin");
                        	$res3 = $STH3 -> execute();
				
			}else{
				echo 0;
			}

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

               
               
               
?>