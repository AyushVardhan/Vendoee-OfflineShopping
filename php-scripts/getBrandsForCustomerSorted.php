<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                           
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
               
               $city= $_POST['city'];	$state= $_POST['state'];
               $mid= $_POST['mid'];	$catid = $_POST['catid'];
               
               if($catid=="0"){

$sth = $conn->prepare("SELECT sellers.id,sellers.name,sellers.owner_name,sellers.image,seller_addresses.lat,seller_addresses.lng,seller_addresses.address,sellers.cashless FROM sellers INNER JOIN seller_addresses ON seller_addresses.seller_id = sellers.id WHERE seller_addresses.city='$city' AND seller_addresses.state='$state' AND sellers.brand='1' AND sellers.id>'$mid' AND sellers.verified='1' LIMIT 4");               
               
               }else{

           $sth = $conn->prepare("SELECT sellers.id,sellers.name,sellers.owner_name,sellers.image,seller_addresses.lat,seller_addresses.lng,seller_addresses.address,sellers.cashless FROM sellers INNER JOIN seller_addresses ON seller_addresses.seller_id = sellers.id INNER JOIN seller_intermediate_categories ON seller_intermediate_categories.seller_id = sellers.id WHERE seller_addresses.city='$city' AND seller_addresses.state='$state' AND sellers.brand='1' AND sellers.verified='1' AND sellers.id>'$mid' AND seller_intermediate_categories.seller_category_id='$catid' LIMIT 4");                              
               
               }
           
$rowCount = 0;
$countItem = 0;
               
if($sth->execute()) {
 $rowCount = $sth->rowCount();     
 if($rowCount > 0)  {
  
  while($result = $sth->fetchObject()) {
      $countItem = $countItem + 1;
      if($countItem < 4){
                $pname = $result->name; $img = $result->image; $sid = $result->id; $cash = $result->cashless;
      $lat = $result->lat;
      $lng = $result->lng;
      $ownerName = $result->owner_name;
      $address = $result->address;
      
      
      $sth5 = $conn->prepare("SELECT phone_no FROM seller_contacts WHERE seller_id =$sid LIMIT 1");
      if($sth5->execute())
      {
      	while($result5 = $sth5->fetchObject())
      	{
      		$contact = $result5->phone_no;
      	}
      }
      
            $category="";
      $sth6 = $conn->prepare("select cat_name from seller_intermediate_categories where seller_id = ".$sid);
      			if($sth6->execute())
      			{
      				while($result6 = $sth6->fetchObject())
      				{
      					$tmp = $result6->cat_name;
      					$category = $tmp.", ".$category;
      				}
      			}
      
      if($rowCount == 4){
            echo $pname."||"; echo $img."||"; echo $sid."||"; echo $contact."||"; echo $lat."||"; echo $lng."||"; echo $cash."||";echo $category."||"; echo $ownerName."||"; echo $address."||"; echo "1".";";
      }else{
            echo $pname."||"; echo $img."||"; echo $sid."||"; echo $contact."||"; echo $lat."||"; echo $lng."||"; echo $cash."||";echo $category."||"; echo $ownerName."||"; echo $address."||"; echo "0".";"; 
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