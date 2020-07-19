<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

                 $phone = $_POST['phone'];
                 //$phone = "8941096130";

                 date_default_timezone_set('Asia/Kolkata');
		    $date = date('Y-m-d');

                        $sql = "SELECT id FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
			if($STH->rowCount()>0){
				   	$result = $STH -> fetch();

		                        $cid = $result ["id"];
					
			}		
	$city = "";

      $sql = "SELECT offer_id FROM offer_customer_love_relations WHERE customer_id ='$cid'";
      $sth5 = $conn->prepare($sql);
      if($sth5->execute())
      {
      	$count = $sth5->rowCount() ;
      	
      	if($count>0){

	      	while($result5 = $sth5->fetchObject())
	      	{
	      		$offid = $result5->offer_id;
			
                        $sql5 = "SELECT product_category_id FROM offers WHERE id = '$offid'";
                        $STH5 = $conn->prepare($sql5);
                        $STH5 -> execute();
			if($STH5->rowCount()>0){
				   	$result5 = $STH5 -> fetch();
		                        $subcatid = $result5 ["product_category_id"];
			}
			
//				      		      		      echo $subcatid." ";
			
                        $sql6 = "SELECT id FROM offers WHERE product_category_id = '$subcatid' and verified='1' and start_date >= '".$date."' and end_date >= '".$date."'";
                        $STH6 = $conn->prepare($sql6);
                        $STH6 -> execute();
			if($STH6->rowCount()>0){

				while($result6 = $STH6->fetchObject())
			      	{
			      		$contact = $result6->id;
			      						      		      		      //echo $contact." ";
			      		$tmp = $tmp.",".$contact;
			      	}
			}						
	      	}
	      	$city = substr($tmp,1);      		
      	}else{

	      	$city = "";      		      	
      	}
      	
      }
      
      echo $city;
      //echo $date1[0];
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>