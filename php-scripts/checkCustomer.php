<?php

        define('HOST','localhost');
        define('USER','psVishalPs');
        define('PASS','vendoe1232');
        define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $phone = $_POST['phone'];
			//$phone = "7903880472";

                        $sql = "SELECT name,email,password FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
			if($STH->rowCount()>0){
				   	$result = $STH -> fetch();

		                        $name = $result ["name"];
					$password = $result ["password"];
					$email= $result ["email"];
									
					if($name!=null && $password!=null && $email!=null){
						echo "1";
					}else{
						echo "Profile Incomplete!";						
					}
			}else{
				echo "Customer not found!";
			}
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }             
?>