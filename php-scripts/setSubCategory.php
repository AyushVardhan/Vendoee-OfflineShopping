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
                	//$sid = "500";
                	//$subcat = "1,1;1,3;2,7;2,9;3,15";
                	$subcat = $_POST['subcat'];


			$array =  explode(';', $subcat);

			foreach ($array as $item) {
				$array1 =  explode(',', $item);
				//echo $array1[0]." ".$array1[1]." ".$sid."</br>";
				
				
			$sth = $conn->prepare("INSERT INTO seller_intermediate_subcategory(seller_id,seller_category_id,seller_subcategory_id)VALUES(:sid,:cid,:name)");

                        $sth->bindParam(':sid',$sid);
                        $sth->bindParam(':name',$array1[1]);
                        $sth->bindParam(':cid',$array1[0]);

                        $sth->execute();
                        
			}
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