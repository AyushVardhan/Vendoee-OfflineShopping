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
                      

                        $sql = "SELECT id FROM customers where email="."'"."$email"."'"." and password="."'"."$pass"."'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        if($result = $STH -> fetch())
                        {
                       		$pin = $result ["id"];
                        	echo $pin;
                        }else{
                        	echo 0;
                        }

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>
