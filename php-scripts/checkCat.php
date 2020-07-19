<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
    		$id= $_POST['sid'];

                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                      
            

                        $sql = "SELECT count(*) FROM seller_intermediate_categories where seller_id=".$id;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        
                        $count = $STH->fetchColumn();
                        
                        if($count>0)
                        {	
                        	echo $count;
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

	  mysqli_close($con);
?>
