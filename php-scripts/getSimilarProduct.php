<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
               
               $city= $_POST['city'];$state= $_POST['state'];
               $oid = $_POST['id'];
               $cat= $_POST['name'];
     
               
               //$city= 'Dehradun';
               //$oid = '8';
               //$cat= 'teddy';
               
               $sth1 = $conn->prepare("SELECT seller_category_id FROM offers WHERE id= '$oid'");
               $sth1->execute();
               while($result1 = $sth1->fetchObject()) {
               		$catid = $result1->seller_category_id;
               }
               
               
           $sth = $conn->prepare("SELECT offers.id,offers.product_name,offers.offer_price,offers.image FROM offers INNER JOIN seller_addresses ON seller_addresses.seller_id = offers.seller_id WHERE seller_addresses.city='$city' AND seller_addresses.state='$state' AND offers.id!='$oid' AND offers.verified='1' AND offers.seller_category_id='$catid' AND MATCH (offers.product_name) AGAINST ('$cat' IN BOOLEAN MODE) ORDER BY offers.discount DESC LIMIT 8");
           // 
           
               
if($sth->execute()) {
     
 if($sth->rowCount() > 0) {
  
	if($sth->rowCount()>1){
		//echo "count > 4 <br>";
		for($i=1;$i<=3;$i++){
			
			if($i==3){
				//echo "inside view more <br>";
	         		echo "0"."||";echo "View More"."||";echo "Offers"."||";echo "abcd".";"; 
	              	}else{
	              		$result = $sth->fetchObject();
		        	$oid = $result->id; $pname = $result->product_name; 
		        	$oprice = $result->offer_price; $img = $result->image;
		    		echo $oid."||";echo $pname."||";echo $oprice."||";echo $img.";"; 
		    		//echo $pname.",";
	              	}
		}
	}else{
	//echo "inside normal more <br>";
	  while($result = $sth->fetchObject()) {
      $oid = $result->id; $pname = $result->product_name; 
      $oprice = $result->offer_price; $img = $result->image; 

      	echo $oid."||";echo $pname."||";echo $oprice."||";echo $img.";";
      	//echo $pname.",";
          }
	}
	             
        }else{
        //echo "no result found, now displaying from categoryy only <br>";
$sth = $conn->prepare("SELECT offers.id,offers.product_name,offers.offer_price,offers.image FROM offers INNER JOIN seller_addresses ON seller_addresses.seller_id = offers.seller_id WHERE seller_addresses.city='$city' AND offers.id!='$oid' AND offers.verified='1' AND offers.seller_category_id='$catid' ORDER BY offers.discount DESC LIMIT 3");

	if($sth->execute()){
		if($sth->rowCount() > 0){
		
		 while($result = $sth->fetchObject()) {
      $oid = $result->id; $pname = $result->product_name; 
      $oprice = $result->offer_price; $img = $result->image; 
      
  
      //echo $pname.",";
      echo $oid."||";echo $pname."||";echo $oprice."||";echo $img.";";
          } 
		
		}
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

	  mysqli_close($con);
?>