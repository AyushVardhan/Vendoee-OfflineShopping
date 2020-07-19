<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                       
                     	$oid = $_POST['oid']; 	        $name = $_POST['pname']; 		
                 	$desc = $_POST['desc']; 	$price = $_POST['price'];
                 	$oprice = $_POST['oprice']; 	$SDate = $_POST['SDate'];
                	$EDate = $_POST['EDate'];   	$img = $_POST['img'];
	               	$main_cat = $_POST['maincat'];   	$sub_cat = $_POST['subcat'];
                	
               $sth = $conn->prepare("SELECT id FROM seller_categories WHERE name='$main_cat'");
               
		if($sth->execute()) {
     
			 if($sth->rowCount() > 0) {
  
				  while($result = $sth->fetchObject()) {
				      $mainid = $result->id;
          			}
        		}
    		}
    		
		$sth1 = $conn->prepare("SELECT id FROM product_categories WHERE seller_category_id=$mainid and name='$sub_cat'");
               
		if($sth1->execute()) {
     
			 if($sth1->rowCount() > 0) {
  
 				 while($result1 = $sth1->fetchObject()) {
				      $subid = $result1->id;
          			}
        		}else{
        		$subid = "0";
        		}
    		}     		                	
                	
                	$dis = (($price - $oprice)*100)/$price;
                	
                	$STH1 = $conn->prepare("UPDATE offers SET verified = '0' WHERE id = $oid");
                        $res1 = $STH1 -> execute();

	$sth = $conn->prepare("UPDATE offers SET description='$desc',price=$price,offer_price=$oprice,product_name='$name',discount=$dis,image='$img',start_date='$SDate',end_date='$EDate',seller_category_id='$mainid',product_category_id='$subid' WHERE id=$oid");


                        if($sth->execute())
                        {
                        	echo 1;
                        }else{
                        	echo 0;
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