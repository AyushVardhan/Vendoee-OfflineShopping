<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
    		$email = $_POST['email'];
    		$pass = $_POST['pass'];

    		
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 
                 if(is_numeric($email)){
                 		
                 		$sql1 = "SELECT seller_id FROM seller_contacts WHERE phone_no="."'"."$email"."'"." LIMIT 1";
                        	$STH1 = $conn->prepare($sql1);
                        	$STH1 -> execute();
                        	$result1 = $STH1 -> fetch();
                        	$pin1 = $result1 ["seller_id"];
                        	
                        	$sql2 = "SELECT id,password FROM sellers WHERE id="."'"."$pin1"."'";
                        	$STH2 = $conn->prepare($sql2);
                        	$STH2 -> execute();
                        	if($result2 = $STH2 -> fetch())
                        	{
                       			$pin2 = $result2 ["id"];
                       			$hash = $result2 ["password"];
                       			
                       			if(password_verify($pass, $hash)){
						echo $pin2;
					}else{
						echo 0;
					}
                        	}else{
                        		echo 0;
                        	}
                 
                 }else{
                 		                
                 	$sql = "SELECT id,password FROM sellers where email="."'"."$email"."'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        if($result = $STH -> fetch())
                        {
                       		$pin = $result ["id"];
                       		$hash = $result ["password"];
                        	if(password_verify($pass, $hash)){
					echo $pin;
				}else{
					echo 0;
				}
                        }else{
                        	echo 0;	
                        }
                 
                 }

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>
