<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');
        try {                       
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
              
                     
               $oid = $_POST['id'];

               $sth = $conn->prepare("SELECT cat_name FROM seller_intermediate_categories WHERE seller_id = $oid ");
               
		if($sth->execute()) {
     
 			if($sth->rowCount() > 0) {
  
  				while($result = $sth->fetchObject()) {
      					echo $result->cat_name.",";
          				}
        			}
    			} 

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>