<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {

                 date_default_timezone_set('Asia/Kolkata');
		    $date = date("Y-m-d H:i:s");
		    
		date_default_timezone_set('Asia/Kolkata');
		    $datet = date('Y-m-d');

                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                       
                 $sid = 0;
                 $cid = 0;      
                 $phone = $_POST['phone'];
                 $offerid = $_POST['oid'];
                 $offerprice = $_POST['price'];                 
		 //$phone = "7903880472";

                        $sql = "SELECT id FROM customers WHERE phone_no=".$phone;
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
			if($STH->rowCount()>0){
				   	$result = $STH -> fetch();

		                        $cid = $result ["id"];
					
			}		

                        $sql1 = "SELECT seller_id FROM offers WHERE id=".$offerid;
                        $STH1 = $conn->prepare($sql1);
                        $STH1 -> execute();
			if($STH1->rowCount()>0){
				   	$result1 = $STH1 -> fetch();

		                        $sid = $result1 ["seller_id"];
					
			}					 


			//echo $cid.",".$sid.",".$offerid.",".$offerprice;

			$sth = $conn->prepare("INSERT INTO customer_seller_offer_deals(customer_id,seller_id,offer_id,price,created_at)VALUES(:cid,:sid,:oid,:price,:dat)");
                        $sth->bindParam(':cid',$cid);
                        $sth->bindParam(':sid',$sid);
                        $sth->bindParam(':oid',$offerid);
                        $sth->bindParam(':price',$offerprice); 
                        $sth->bindParam(':dat',$date); 

                        if($sth->execute()){
                                                	
	                        $sql11 = "SELECT limit_count,start_date FROM customer_points WHERE customer_id='$cid' and seller_id = '$sid'";
	                        $STH11 = $conn->prepare($sql11);
	                        $STH11 -> execute();
				if($STH11->rowCount()>0){
				
					   	$result11 = $STH11 -> fetch();
			                        $limc = $result11 ["limit_count"];
			                        $stdate = $result11 ["start_date"];
			                        						
						if($limc > 5){
				                        $date1=date_create($stdate);
							$date2=date_create($datet);
							$diff=date_diff($date1,$date2);
							$val = $diff->format("%a");	
							
		                                      	echo $val;									
						}else{
		                                      	echo "sucess";						
						}						
				}else{
						echo "sucess";	
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