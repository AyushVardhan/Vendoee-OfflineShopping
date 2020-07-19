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
                        $password = $_POST['pass'];
                        $cont = $_POST['cont'];
                        $addr = $_POST['addr'];
                       

$sth = $conn->prepare("INSERT INTO customers(name, email, password, phone_no, address) VALUES (:name,:email,:password,:cont,:addr)");

                        $sth->bindParam(':name',$name);
                        $sth->bindParam(':email',$email);
                        $sth->bindParam(':password',$password);
                        $sth->bindParam(':cont',$cont);
                        $sth->bindParam(':addr',$addr);


                        $sth->execute();
                        $conn->commit();
                        echo 1;

            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>
