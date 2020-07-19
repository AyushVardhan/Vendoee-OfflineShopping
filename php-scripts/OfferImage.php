<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                       
                     
                 	$oid = $_POST['oid'];	                	$img2 = $_POST['img2'];
                	$img1 = $_POST['img1'];				


			$STH1 = $conn->prepare("UPDATE offers SET verified = '0' WHERE id = $oid");
                        $res1 = $STH1 -> execute();

			$STH = $conn->prepare("DELETE FROM offer_images WHERE offer_id = $oid");
                        $res = $STH -> execute();
                        
                        if($res)
			{
				
				
				$sth1 = $conn->prepare("INSERT INTO offer_images(offer_id,image)VALUES(:oid,:img1)");
                        	$sth1->bindParam(':oid',$oid);
                        	$sth1->bindParam(':img1',$img1);
                        	$sth1->execute();
				
				
                        	
                        	$sth2 = $conn->prepare("INSERT INTO offer_images(offer_id,image)VALUES(:oid,:img2)");
                        	$sth2->bindParam(':oid',$oid);
                        	$sth2->bindParam(':img2',$img2);
                        	$sth2->execute();
                        	
                        
                        	echo 1;
			}else{
				echo "QueryFailed";
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