<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
             		$oldContact = $_POST['oldcontact'];
             		$newContact = $_POST['newcontact'];
             		
      					
      			$sth = $conn->prepare("UPDATE customers SET phone_no = :newC WHERE phone_no = :oldC");
	        	$sth->bindParam(':newC',$newContact);
	        	$sth->bindParam(':oldC',$oldContact);
	        	                	        	                
	                if($sth->execute()){
	                	echo "1";
	                }else{
	                	echo "0";
	                }   
	                
			$conn->commit();
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>