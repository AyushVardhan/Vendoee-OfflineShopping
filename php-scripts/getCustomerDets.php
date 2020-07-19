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

                        $sql = "SELECT * FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();

                        $cid = $result ["id"];
                        $name = $result ["name"];
			$address = $result ["address"];
			$email= $result ["email"];
			$password= $result ["password"];

			if($name==null){
				$name="vendoee";
			}
			
			if($address==null){
				$address="vendoee";
			}
			
			if($email==null){
				$email="vendoee";
			}

			if($password==null){
				$password="vendoee";
			}
			
			
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
						
                        echo $name.";".$address.";".$email.";".$password.";".$oldpointR;

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }             
?>