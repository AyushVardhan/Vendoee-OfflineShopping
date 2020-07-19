<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                       
                 	$phone = $_POST['phone'];
                	$voucher_id = $_POST['vid'];

                        $sql = "SELECT id FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();
                        $customer_id = $result ["id"];

			$sth = $conn->prepare("INSERT INTO customer_voucher_requests(customer_id,voucher_id)VALUES(:sid,:ph)");
                        $sth->bindParam(':sid',$customer_id);
                        $sth->bindParam(':ph',$voucher_id);
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