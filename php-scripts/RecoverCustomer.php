<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
    		$email = $_POST['email'];
    		$pass = $_POST['pass'];
    		$phone = $_POST['phone'];
    		
		
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 
                 	$sql = "SELECT id,password FROM customers where email="."'"."$email"."'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        if($result = $STH -> fetch())
                        {
                       		$hash = $result ["password"];
                       		$id = $result ["id"];
                        	if(password_verify($pass, $hash)){
                        	
					        $sql1 = "SELECT point FROM customer_points where customer_id="."'"."$id"."'";
                        			$STH1 = $conn->prepare($sql1);
                        			$STH1 -> execute();
                        			if(($STH1->rowCount() > 0)){
                        			
	                        		$result2 = $STH1->fetchObject();
						$cpoint= $result2->point;

			                 	$sql2 = "SELECT id FROM customers where phone_no="."'"."$phone"."'";
                       				$STH2 = $conn->prepare($sql2);
                        			$STH2 -> execute();							
						$result3 = $STH2 -> fetch();
						$newCid = $result3 ["id"];

						$sql3 = "SELECT * FROM customer_points WHERE customer_id=".$newCid;
                        			$STH3 = $conn->prepare($sql3);
                        			$STH3 -> execute();
						if($STH3->rowCount()==0){
							//echo "Customer";
							$sth = $conn->prepare("INSERT INTO customer_points(customer_id,point)VALUES(:cid,:pnt)");
        	     				        $sth->bindParam(':cid',$newCid);
                				        $sth->bindParam(':pnt',$cpoint);
							$sth->execute();
						}else{
			
							$result1 = $STH3 -> fetch();
	            			                $oldpoint = $result1 ["point"]; 	$oldpoint = $oldpoint + $cpoint;
							$sth = $conn->prepare("UPDATE customer_points SET point='$oldpoint' WHERE customer_id='$newCid'");
							$sth->execute();
						}
						
						$sth = $conn->prepare("UPDATE customer_points SET point='0' WHERE customer_id='$id'");
						$sth->execute();
													
       						echo "Points Recovered!";			
																
       						}else{
       							echo "No points found!";
       						} 	       

				}else{
					echo "Customer not found!";
				}
                        }else{
                        	echo 0;	
                        }
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>