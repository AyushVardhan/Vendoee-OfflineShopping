<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $id = $_POST['id'];

                        $sql = "select name,owner_name,image,email,opening_time,closing_time, cashless, delivery, brand from sellers where id = ".$id;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();
                        
                        $pin = $result ["image"];
			$name = $result ["name"];
			$bname = $result ["owner_name"];
			$email= $result ["email"];
			$otime = $result['opening_time'];
			$ctime = $result['closing_time'];
			$cashless = $result['cashless'];
			$delivery = $result['delivery'];
			$brand = $result['brand'];
			
			$sql1 = "select phone_no from seller_contacts where seller_id = ".$id." LIMIT 1";
                        $STH1 = $conn->prepare($sql1);
                        $STH1 -> execute();
                        $result1 = $STH1 -> fetch();
                        
                        $contact= $result1 ["phone_no"];
                        
                        $sql2 = "select address,lat,lng,city,state from seller_addresses where seller_id = ".$id." LIMIT 1";
                        $STH2 = $conn->prepare($sql2);
                        $STH2 -> execute();
                        $result2 = $STH2 -> fetch();
                        
                        $addr= $result2 ["address"];
                        $lat= $result2 ["lat"];
                        $lng= $result2 ["lng"];
                        $city= $result2 ["city"];
                        $state= $result2 ["state"];
                        
                        $category = "";
		        $sth5 = $conn->prepare("select cat_name from seller_intermediate_categories where seller_id = ".$id);
      			if($sth5->execute())
      			{
      				while($result5 = $sth5->fetchObject())
      				{
      					$tmp = $result5->cat_name;
      					$category = $tmp.", ".$category;
      				}
      			}
			
                        echo $pin.";".$name.";".$bname.";".$email.";".$contact.";".$addr.";".$lat.";".$lng.";".$category.";".$otime.";".$ctime.";".$cashless.";".$delivery.";".$brand.";".$city.";".$state;

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

               
               
               
?>