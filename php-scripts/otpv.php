<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                       
                     
                	$ph = $_POST['ph'];

$sth = $conn->prepare("INSERT INTO customers(phone_no)VALUES(:ph)");

                        $sth->bindParam(':ph',$ph);

                        $sth->execute();
                        echo 1;

		$conn->commit();
            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>