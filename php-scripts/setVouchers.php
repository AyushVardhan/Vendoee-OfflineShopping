<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                       
                 	$point = $_POST['point'];                     
                 	$name = $_POST['name'];
                	$image = $_POST['image'];

$sth = $conn->prepare("INSERT INTO vouchers(name,image,points)VALUES(:name,:image,:point)");

                        $sth->bindParam(':point',$point);
                        $sth->bindParam(':image',$image);
                        $sth->bindParam(':name',$name);

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