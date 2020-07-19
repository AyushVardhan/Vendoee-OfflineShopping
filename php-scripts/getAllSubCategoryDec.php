<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                     $id = 0;
               $main_cat = $_POST['category'];
               //$main_cat = 'Men';
               $sth = $conn->prepare("SELECT id FROM seller_categories WHERE name = '$main_cat'");
               
		if($sth->execute()) {
		
     
		 if($sth->rowCount() > 0) {

			  while($result = $sth->fetchObject()) {
			      $id = $result->id;  
          		  }
	            }
	       }
	       
	       
	       $sth = $conn->prepare("SELECT name FROM product_categories WHERE seller_category_id = $id ");
               
		if($sth->execute()) {
     
		 if($sth->rowCount() > 0) {
  
			  while($result = $sth->fetchObject()) {
			      echo $result->name.",";
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