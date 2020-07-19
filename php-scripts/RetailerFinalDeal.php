<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                    date_default_timezone_set('Asia/Kolkata');
		    $date1 = date("Y-m-d H:i:s");
		    
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
		    date_default_timezone_set('Asia/Kolkata');
		    $date = date('Y-m-d');
		    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                     
                     	$toast = "";
                 	$did = $_POST['dealid']; 		$price = $_POST['price'];
                 	//$did = '12'; 		$price = '300';                 	
                 	
                 	$sql1 = "SELECT customer_id,seller_id FROM customer_seller_offer_deals WHERE id=".$did;
                        $STH1 = $conn->prepare($sql1);
                        $STH1 -> execute();
			if($STH1->rowCount()>0){
				   	$result1 = $STH1 -> fetch();

		                        $cid = $result1 ["customer_id"];
		                        $sid = $result1 ["seller_id"];					
			}

			$pointC = "20"; $limt = "1";
			$sql1 = "SELECT * FROM customer_points WHERE customer_id='$cid' and seller_id='$sid'";
                        $STH1 = $conn->prepare($sql1);
                        $STH1 -> execute();
			if($STH1->rowCount()==0){

				$sth = $conn->prepare("INSERT INTO customer_points(customer_id,point,seller_id,start_date,limit_count)VALUES(:cid,:pnt,:sid,:date,:limit)");
        	                $sth->bindParam(':cid',$cid);
        	                $sth->bindParam(':sid',$sid);
        	                $sth->bindParam(':date',$date);
                	        $sth->bindParam(':pnt',$pointC);
                    	        $sth->bindParam(':limit',$limt);
				$sth->execute();
								
			}else{

				$result1 = $STH1 -> fetch();
	                        $oldpoint = $result1 ["point"];   $dat= $result1 ["start_date"];  $lim= $result1 ["limit_count"];
	                        
	                        $date1=date_create($dat);
				$date2=date_create($date);
				$diff=date_diff($date1,$date2);
				$val = $diff->format("%a");
				
				if($val<30){
					if($lim<5){
					$oldpoint = $oldpoint + 20;	$lim = $lim + 1;
					$sth = $conn->prepare("UPDATE customer_points SET point='$oldpoint',limit_count='$lim' WHERE customer_id='$cid' and seller_id='$sid'");
					$sth->execute();
					}else{
							$toast = "Points limit for this shop is reached.";
					}
				}else if($val>30){
				
						$dat= $date;	$lim = "1"; 	$oldpoint = $oldpoint + 20;
				$sth = $conn->prepare("UPDATE customer_points SET point='$oldpoint',limit_count='$lim',start_date='$dat' WHERE customer_id='$cid' and seller_id='$sid'");
				$sth->execute();				
				}

			}
			
			
			$pointR = "20";
			$sql1 = "SELECT * FROM seller_points WHERE seller_id=".$sid;
                        $STH1 = $conn->prepare($sql1);
                        $STH1 -> execute();
			if($STH1->rowCount()==0){
				//echo "Retailer";
				$sth = $conn->prepare("INSERT INTO seller_points(seller_id,point)VALUES(:cid,:pnt)");
        	                $sth->bindParam(':cid',$sid);
                	        $sth->bindParam(':pnt',$pointR);
				$sth->execute();
			}else{
			
				$result1 = $STH1 -> fetch();
	                        $oldpointR = $result1 ["point"]; 	$oldpointR = $oldpointR + 20;
				$sth = $conn->prepare("UPDATE seller_points SET point='$oldpointR' WHERE seller_id='$sid'");
				$sth->execute();
			}
                        
                        
                        $sql50 = "UPDATE customer_seller_offer_deals SET seller_confirm='1',price='$price',updated_at='".$date."' WHERE id='$did'";
                        $sth = $conn->prepare($sql50);
			
			if($sth->execute()){
				if(strlen($toast)>0)
				{
					echo "2";
				}else{
					echo "1";				
				}
			}else{
				echo "0";			
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