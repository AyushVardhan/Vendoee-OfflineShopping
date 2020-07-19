<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();

                 	$name = $_POST['name'];
                	$email = $_POST['email'];
                        $phoneNo = $_POST['phoneNo'];
                        $password = $_POST['password'];
                        
			$password = password_hash($password , PASSWORD_DEFAULT);
			
			
                $q1= "SELECT email FROM customers WHERE email= '$email'";
		$statement1 = $conn ->prepare($q1);
		$status1 = $statement1->execute();
		
		if (($status1) && ($statement1->rowCount() > 0))
		{
			echo "Email Id already exist";
                       	
		}else{
			$q2= "SELECT phone_no FROM customers WHERE phone_no = '$phoneNo'";
			$statement1 = $conn ->prepare($q1);
			$status1 = $statement1->execute();
			
			if (($status1) && ($statement1->rowCount() > 0))
			{
				echo "Phone No Id already exist";
	                       	
			}else{
				// send otp
				// proceed for registraion
			}
		
		
		}       
                        $conn->commit();
            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>
