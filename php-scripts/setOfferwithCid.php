<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
  
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                   date_default_timezone_set('Asia/Kolkata');
		    $datereq = date('Y-m-d H:i:s');
  
                 $conn->beginTransaction();
                     
                 	$sid = $_POST['sid']; 		$pname = $_POST['pname'];
                 	$mainid = $_POST['mainid']; 	$subid = $_POST['subid'];
                 	$desc = $_POST['desc']; 	$price = $_POST['price'];
                 	$oprice = $_POST['oprice']; 	$SDate = $_POST['SDate'];
                	$EDate = $_POST['EDate'];   	$img = $_POST['img'];                     $dis = $_POST['dis']; $cid = $_POST['cid'];


			$array =  explode(',', $cid);
			$custID = $array[0];
			$reqID = $array[1];
			
$sth = $conn->prepare("INSERT INTO offers(seller_id,product_name,seller_category_id,product_category_id,description,price,offer_price,discount,image,start_date,end_date) VALUES (:sid,:pname,:mainid,:subid,:desc,:price,:oprice,:dis,:img,:SDate,:EDate)");

                        $sth->bindParam(':sid',$sid);	$sth->bindParam(':pname',$pname);
                        $sth->bindParam(':mainid',$mainid);	$sth->bindParam(':subid',$subid);
                        $sth->bindParam(':desc',$desc);	$sth->bindParam(':price',$price);
                        $sth->bindParam(':oprice',$oprice);	$sth->bindParam(':img',$img);
                        $sth->bindParam(':SDate',$SDate);	$sth->bindParam(':EDate',$EDate);  $sth->bindParam(':dis',$dis);

                        $sth->execute();
                        
                        $sql = "SELECT MAX(id) FROM offers WHERE seller_id ="."'"."$sid"."'";
                        $statement = $conn->prepare($sql);
			$statement->execute(); // no need to add `$sql` here, you can take that out
			$item_id = $statement->fetchColumn();   
			
			$ipaddress = 0;
			$ipaddress1 = 0;       
                        
                        $sth4 = $conn->prepare("INSERT INTO offer_hit_counters(offer_id,hit_count,unique_hit_count)VALUES(:sid,:ph,:ph1)");
                        $sth4->bindParam(':sid',$item_id);
                        $sth4->bindParam(':ph',$ipaddress);
                        $sth4->bindParam(':ph1',$ipaddress1);
                        $sth4->execute();
                        
                        $sth5 = $conn->prepare("INSERT INTO offer_requests(seller_id,customer_id,request_offer_id,offer_id,created_at)VALUES(:sid,:cid,:rid,:oid,:dat)");
                        $sth5->bindParam(':sid',$sid);
                        $sth5->bindParam(':cid',$custID);
                        $sth5->bindParam(':rid',$reqID);
                        $sth5->bindParam(':oid',$item_id);
                        $sth5->bindParam(':dat',$datereq);
                        $sth5->execute();                        
                        
                        echo $item_id;			
		

			$conn->commit();
            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>