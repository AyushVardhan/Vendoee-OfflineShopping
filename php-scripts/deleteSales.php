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

                        $STH = $conn->prepare("DELETE FROM offers WHERE id = $id");
                        $res = $STH -> execute();
                        
                        
                        $STH1 = $conn->prepare("DELETE FROM offer_images WHERE offer_id = $id");
                        $res1 = $STH1 -> execute();
                        
			if($res1)
			{
				echo 1;
			}else{
				echo 0;
			}

			$conn->commit();
            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($conn);
?>