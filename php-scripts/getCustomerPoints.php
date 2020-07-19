<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $phone = $_POST['phone'];
                     //$phone = "8941096130";

                        $sql = "SELECT id FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();

                        $cid = $result ["id"];
						
			$oldpointR = 0;
			
			$sth1 = $conn->prepare("SELECT * FROM customer_points WHERE customer_id='$cid'");
			      if($sth1->execute())
			      {
				      	if(($sth1->rowCount() > 0)){
						while($result1 = $sth1->fetchObject())
					      	{
					      		$tmp = $result1->point;
					      		$oldpointR = $oldpointR + $tmp;
					      	}       		
				      	}else{
				      		$oldpointR = "0";
				      	}			      			      
			      	
			      }									
						
                        echo $oldpointR;

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }             
?>