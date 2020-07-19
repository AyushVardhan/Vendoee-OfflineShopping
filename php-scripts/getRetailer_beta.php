<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $id = $_POST['id'];

                        $sql = "select name,owner_name,image,email from sellers where id = ".$id;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();
                        
                        $pin = $result ["image"];
			$name = $result ["name"];
			$bname = $result ["owner_name"];
			$email= $result ["email"];
			
			$sql1 = "select phone_no from seller_contacts where seller_id = ".$id." LIMIT 1";
                        $STH1 = $conn->prepare($sql1);
                        $STH1 -> execute();
                        $result1 = $STH1 -> fetch();
                        
                        $contact= $result1 ["phone_no"];
                        
                        $sql2 = "select address,lat,lng from seller_addresses where seller_id = ".$id." LIMIT 1";
                        $STH2 = $conn->prepare($sql2);
                        $STH2 -> execute();
                        $result2 = $STH2 -> fetch();
                        
                        $addr= $result2 ["address"];
                        $lat= $result2 ["lat"];
                        $lng= $result2 ["lng"];
                        
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
      			
      			$sql1 = "SELECT * FROM seller_points WHERE seller_id=".$id;
                        $STH1 = $conn->prepare($sql1);
                        $STH1 -> execute();
                        $result1 = $STH1 -> fetch();
	                $oldpointR = $result1 ["point"];
	                
	                if($oldpointR==null){
	                	$oldpointR = "0";
	                }
			
                        echo $pin.";".$name.";".$bname.";".$email.";".$contact.";".$addr.";".$lat.";".$lng.";".$category.";".$oldpointR;

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

               
               
               
?>