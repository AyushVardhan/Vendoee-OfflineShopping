<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');
        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                     
                     	$id = $_POST['id'];
                 	$cont= $_POST['contact'];


			$sth = $conn->prepare("INSERT INTO seller_contacts(seller_id,phone_no)VALUES(:id,:cont)");

                        $sth->bindParam(':cont',$cont);
			$sth->bindParam(':id',$id);

                        if($sth->execute())
                        {
                        	echo 1;
                        }else{
                        	echo "Error";
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