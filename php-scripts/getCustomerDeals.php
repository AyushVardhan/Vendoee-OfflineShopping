<?php
define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {         
            
            $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                       
                     $phone = $_POST['phone'];
				$mid= $_POST['mid'];
                      //$phone = "8941096130";
				//$mid= "26";

                        $sql = "SELECT id FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();

                        $cid = $result ["id"];

$rowCount = 0;
$countItem = 0;  

$sth = $conn->prepare("SELECT customer_seller_offer_deals.seller_id,customer_seller_offer_deals.offer_id,customer_seller_offer_deals.id,customer_seller_offer_deals.price,customer_seller_offer_deals.seller_confirm,offers.product_name,offers.image,offers.discount,offers.start_date,offers.end_date FROM customer_seller_offer_deals JOIN offers ON customer_seller_offer_deals.offer_id=offers.id WHERE customer_seller_offer_deals.customer_id = $cid AND customer_seller_offer_deals.id>$mid LIMIT 4");
if($sth->execute()) {

 $rowCount = $sth->rowCount();     
 if($rowCount > 0) {
  
    while($result = $sth->fetchObject()) {
  
      $countItem = $countItem + 1;
      if($countItem < 4){

      $sid = $result->seller_id; $oid = $result->offer_id; $price = $result->price ; $SC = $result->seller_confirm;
      $tid = $result->id;

      		                $prod_name = $result->product_name;
      		                $image = $result->image;
      		                $discount = $result->discount;
      		                $start = $result->start_date;   
      		                $end = $result->end_date;   	
                            
                      $sth3 = $conn->prepare("SELECT name FROM sellers WHERE id = $sid");
                      if($sth3->execute())
                      {
      	                while($result3 = $sth3->fetchObject())
      	                {
      		                $shop = $result3->name;
      	                }
                      }	   		
      
                      if($rowCount == 4){
                        echo $oid."||";echo $sid."||";echo $prod_name."||";echo $price."||";echo $shop."||";echo $image."||";echo $start."||";echo $end."||";echo $discount."||";echo $SC."||";echo $tid."||"; echo "1".";";           
                      }else{
      	                echo $oid."||";echo $sid."||";echo $prod_name."||";echo $price."||";echo $shop."||";echo $image."||";echo $start."||";echo $end."||";echo $discount."||";echo $SC."||";echo $tid."||"; echo "0".";";          
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
?>