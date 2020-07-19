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

               	$sth = $conn->prepare("SELECT * FROM offers WHERE id='$id'");
               
		if($sth->execute()) {
     
 			if($sth->rowCount() > 0) {
  
  				while($result = $sth->fetchObject()) {
      					$name = $result->product_name;	$main = $result->seller_category_id;	$sub = $result->product_category_id;	
      					$desc = $result->description;	$price = $result->price;	$oprice = $result->offer_price;	
      					$sdate = $result->start_date;	$edate = $result->end_date; $dis = $result->discount; $img = $result->image;
      					
      					$sth1 = $conn->prepare("SELECT name FROM seller_categories WHERE id=$main");
      					if($sth1->execute())
      					{
      						while($result1 = $sth1->fetchObject()) {
      							$mainname = $result1->name;
      						}
      					}
      					
      					$sth2 = $conn->prepare("SELECT name FROM product_categories WHERE id=$sub");
      					if($sth2->execute())
      					{
      					      	if(($sth2->rowCount() > 0)){
					      		while($result2 = $sth2->fetchObject())
      							{
      								$subname = $result2->name;
       							}       		
      						}else{
      							$subname = "Others";
      						}
      						
      					}
      					
      					echo $name.",";echo $mainname.",";echo $subname.",";
      					echo $desc.",";echo $price.",";echo $oprice.",";
      					echo $sdate.",";echo $edate.",";echo $main.",";echo $sub.",";echo $dis.",";echo $img;
          			}
        		}
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