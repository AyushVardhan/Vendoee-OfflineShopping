<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
               
               $city= $_POST['city'];
               $cattttid= $_POST['catid'];
               $icnt= $_POST['itemCount'];
               $lid= $_POST['lastid'];
               
           $sth = $conn->prepare("SELECT offers.id,offers.seller_id,offers.product_name,offers.seller_category_id,offers.product_category_id,offers.description,offers.	price,offers.offer_price,offers.discount,offers.image,offers.start_date,offers.end_date FROM offers INNER JOIN seller_addresses ON seller_addresses.seller_id = offers.seller_id WHERE seller_addresses.city='$city' AND offers.verified='1' AND offers.seller_category_id='$cattttid' AND offers.id>$lid ORDER BY offers.id DESC LIMIT $icnt");
           $result123 = array();
               
if($sth->execute()) {
     
 if($sth->rowCount() > 0) {
  
  while($result = $sth->fetchObject()) {
      $oid = $result->id; $pname = $result->product_name; $seller_catid = $result->seller_category_id; $product_catid = $result->product_category_id;
      $price = $result->price; $oprice = $result->offer_price; $img = $result->image; $sdate = $result->start_date; $edate = $result->end_date;
      $sid = $result->seller_id; $desc= $result->description; $dis = $result->discount;
      
      $sth1 = $conn->prepare("SELECT name FROM seller_categories WHERE id = $seller_catid");
      if($sth1->execute())
      {
      	while($result1 = $sth1->fetchObject())
      	{
      		$seller_catname = $result1->name;
      	}
      }
      
      $sth2 = $conn->prepare("SELECT name FROM product_categories WHERE id = $product_catid");
      if($sth2->execute())
      {
      
      	if(($sth2->rowCount() > 0)){
      		while($result2 = $sth2->fetchObject())
      		{
      		$product_catname = $result2->name;
       		}       		
      	}else{
      		$product_catname = "Others";
      	}      
     

      }
      
      $sth3 = $conn->prepare("SELECT name,cashless FROM sellers WHERE id = $sid");
      if($sth3->execute())
      {
      	while($result3 = $sth3->fetchObject())
      	{
      		$shop = $result3->name;
      		$cash = $result3->cashless;
      	}
      }
      
      $sth4 = $conn->prepare("SELECT lat,lng FROM seller_addresses WHERE seller_id = $sid");
      if($sth4->execute())
      {
      	while($result4 = $sth4->fetchObject())
      	{
      		$lat = $result4->lat;
      		$lng = $result4->lng;
      	}
      }
      
      $sth5 = $conn->prepare("SELECT phone_no FROM seller_contacts WHERE seller_id =$sid LIMIT 1");
      if($sth5->execute())
      {
      	while($result5 = $sth5->fetchObject())
      	{
      		$contact = $result5->phone_no;
      	}
      }
      
      $sth6 = $conn->prepare("SELECT hit_count FROM offer_hit_counters WHERE offer_id= $oid");
      if($sth6->execute())
      {
      	if(($sth6->rowCount() > 0)){
      		$result6 = $sth6-> fetch();
		$viewCount = $result6["hit_count"];
      	}else{
      		$viewCount = 0;
      	}
      }    
      
      
      
      	echo $oid."||";echo $sid."||";echo $pname."||";echo $seller_catname."||";echo $product_catname."||";echo $price."||";echo $oprice."||";echo $sdate."||";echo $edate."||";
      	echo $shop."||"; echo $desc."||";
      	echo $img."||"; echo $dis."||"; echo $lat."||"; echo $lng."||"; echo $contact."||"; echo $cash."||"; echo $viewCount .";";
      	
		/*
	array_push($result123,
		array(
		'oid'=>$oid,
		'sid'=>$sid,
		'pname'=>$pname,
		'seller_catname'=>$seller_catname,
		'product_catname'=>$product_catname,
		'price'=>$price,
		'oprice'=>$oprice,
		'sdate'=>$sdate,
		'edate'=>$edate,
		'shop'=>$shop,
		'img'=>$img,
		));	
      	*/
          }
             //echo json_encode($result123);
             
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