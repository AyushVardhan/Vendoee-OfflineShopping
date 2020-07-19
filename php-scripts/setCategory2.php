<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                       
                     
                 	$sid = $_POST['sid'];
                	$name = $_POST['cname'];
                        $cid = $_POST['cid'];
                        
                      
			
			$sth = $conn->prepare("INSERT INTO seller_intermediate_categories(seller_id,seller_category_id,cat_name)VALUES(:sid,:cid,:name)");

                        $sth->bindParam(':sid',$sid);
                        $sth->bindParam(':name',$name);
                        $sth->bindParam(':cid',$cid);
			

                        $sth->execute();
                        echo "1";
		$conn->commit();
            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>