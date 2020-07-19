<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
      
                     
                 	$sid = $_POST['id'];
                	

			$sth1 = $conn->prepare("DELETE FROM seller_intermediate_categories where seller_id = $sid");
			$sth1->execute();
			
			$conn->commit();
			
                        //echo 1;

            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>