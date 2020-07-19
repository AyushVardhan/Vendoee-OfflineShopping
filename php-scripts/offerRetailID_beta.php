<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
               
                     $sid = $_POST['id'];
                     $icnt= $_POST['itemCount'];
                     $lid= $_POST['lastid'];

$icnt = $icnt + 1; $rowCount = 0;
$countItem = 0;

               $sth = $conn->prepare("SELECT * FROM offers WHERE seller_id = $sid AND id<$lid ORDER BY id DESC LIMIT $icnt");
               
if($sth->execute()) {
 $rowCount = $sth->rowCount();     
 if($rowCount > 0) {
  
  while($result = $sth->fetchObject()) {
      
      $countItem = $countItem + 1;
      if($countItem < 4){
      
                $oid = $result->id; $pname = $result->product_name; $seller_catid = $result->seller_category_id; $product_catid = $result->product_category_id;
      $price = $result->price; $oprice = $result->offer_price; $img = $result->image; $sdate = $result->start_date; $edate = $result->end_date; $dis = $result->discount;
      $verified = $result->verified; $love = $result->love;
      
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

     if($rowCount == 4){
      	echo $oid."||";echo $pname."||";echo $seller_catname."||";echo $product_catname."||";echo $price."||";echo $oprice."||";echo $sdate."||";echo $edate."||";
      	echo $shop."||";
        echo $img."||"; echo $dis."||"; echo $lat."||"; echo $lng."||"; echo $contact."||"; echo $cash."||"; echo $verified."||"; echo $viewCount."||"; echo $sid."||"; echo $love."||"; echo "1".";";

      }else{
      	echo $oid."||";echo $pname."||";echo $seller_catname."||";echo $product_catname."||";echo $price."||";echo $oprice."||";echo $sdate."||";echo $edate."||";
      	echo $shop."||";
        echo $img."||"; echo $dis."||"; echo $lat."||"; echo $lng."||"; echo $contact."||"; echo $cash."||"; echo $verified."||"; echo $viewCount."||"; echo $sid."||"; echo $love."||"; echo "0".";";

      }
      
      
      }
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