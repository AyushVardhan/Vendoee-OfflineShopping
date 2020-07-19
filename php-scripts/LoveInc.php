<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 
               $oid = $_POST['id'];
               $phonenum = $_POST['phone'];               
               
                 date_default_timezone_set('Asia/Kolkata');
		    $date = date('Y-m-d');
               
               
               //$oid = "184";
               //$phonenum = "8941096130";               

                $sql12 = "SELECT id FROM customers WHERE phone_no=".$phonenum;
                $STH12 = $conn->prepare($sql12);
                $STH12 -> execute();
		$result12 = $STH12 -> fetch();
                $cid = $result12 ["id"];
		
                $sql13 = "SELECT * FROM offer_customer_love_relations WHERE customer_id=$cid AND offer_id=$oid";
                $STH13 = $conn->prepare($sql13);
                $STH13 -> execute();
		if($STH13->rowCount()>0){

			$STH1 = $conn->prepare("DELETE FROM offer_customer_love_relations WHERE customer_id=$cid AND offer_id=$oid");
                        $STH1 -> execute();
                        
	                $sql14 = "SELECT love FROM offers WHERE id=".$oid;
        	        $STH14 = $conn->prepare($sql14);
                	$STH14 -> execute();
			$result14 = $STH14 -> fetch();
                	$love = $result14 ["love"]; 
                	
                	$love = $love - 1;
                	$sth15 = $conn->prepare("UPDATE offers SET love='$love' WHERE id='$oid'"); 
                	$sth15 -> execute();                        
                	
		}else{
			$sth = $conn->prepare("INSERT INTO offer_customer_love_relations(offer_id,customer_id,created_at)VALUES(:oid,:cid,:dat)");
                        $sth->bindParam(':oid',$oid);
                        $sth->bindParam(':cid',$cid);
                        $sth->bindParam(':dat',$date);
                        $sth->execute();
                        
	                $sql14 = "SELECT love FROM offers WHERE id=".$oid;
        	        $STH14 = $conn->prepare($sql14);
                	$STH14 -> execute();
			$result14 = $STH14 -> fetch();
                	$love = $result14 ["love"]; 
                	
                	$love = $love + 1;
                	$sth15 = $conn->prepare("UPDATE offers SET love='$love' WHERE id='$oid'"); 
                	$sth15->execute();
		}	
		
	       
	}
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>