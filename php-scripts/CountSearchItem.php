<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $prod = $_POST['search'];

                        $sql = "SELECT id FROM offers where product_name="."'"."$prod"."'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH->rowCount();
			$result3 = $STH -> fetch();
			if($result == 1){
			
				$sql = "SELECT id FROM offers WHERE product_name =  '$prod'";
                        	$STH = $conn->prepare($sql);
                        	$STH -> execute();
				$result3 = $STH -> fetch();
				$id = $result3[id];
				echo "ID ".$id;
			}else{
				echo "many ".$result ;
			}

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>