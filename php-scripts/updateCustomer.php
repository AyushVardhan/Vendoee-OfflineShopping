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
                 	$address = $_POST['address']; 	$password = $_POST['password'];
                 	$email = $_POST['email']; 	$phone= $_POST['contact']; 	   	
                	
                	$q1= "SELECT email FROM customers WHERE email= '$email' AND phone_no='$phone'";
			$statement1 = $conn ->prepare($q1);
			$status1 = $statement1->execute();
			$result14 = $statement1->fetchObject();
			$emailTest = $result14->email;
			
		if (($status1) && ($statement1->rowCount() == 1))
		{
		
			$q1= "SELECT email FROM customers WHERE email= '$email'";
			$statement2 = $conn ->prepare($q1);
			$status2 = $statement2->execute();
			
			if($statement2->rowCount()>1){
				echo "Email exists!";
			}else{
				if($password!=null){
				$password = password_hash($password, PASSWORD_DEFAULT);
				$sth = $conn->prepare("UPDATE customers SET name='$name',email='$email',address='$address',password='$password' WHERE phone_no='$phone'");
				}else{
					$sth = $conn->prepare("UPDATE customers SET name='$name',email='$email',address='$address' WHERE phone_no='$phone'");
				}

	                        if($sth->execute())
        	                {
                        		echo 1;
                        	}else{
                        		echo 0;
                        	}			
			}		
		}else{
			$q1= "SELECT email FROM customers WHERE email= '$email' AND phone_no!='$phone'";
			$statement2 = $conn ->prepare($q1);
			$status2 = $statement2->execute();
			if($statement2->rowCount()>0){
				echo "Email-ID exists! Please try with new one.";
			}else{
				if($password!=null){
				$password = password_hash($password, PASSWORD_DEFAULT);
				$sth = $conn->prepare("UPDATE customers SET name='$name',email='$email',address='$address',password='$password' WHERE phone_no='$phone'");
				}else{
					$sth = $conn->prepare("UPDATE customers SET name='$name',email='$email',address='$address' WHERE phone_no='$phone'");
				}

	                        if($sth->execute())
        	                {
                        		echo 1;
                        	}else{
                        		echo 0;
                        	}				
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