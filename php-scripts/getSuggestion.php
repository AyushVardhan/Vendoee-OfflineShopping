<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $connection = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
                 $city= $_POST['city'];$state= $_POST['state'];$search= $_POST['search'];
                 
    		 $sql = "SELECT DISTINCT offers.product_name FROM offers INNER JOIN seller_addresses ON seller_addresses.seller_id = offers.seller_id WHERE seller_addresses.city='$city' AND seller_addresses.state='$state' AND offers.verified='1' AND offers.product_name LIKE '%$search%'";
		  $statement = $connection->prepare($sql);
  			$statement->execute();
  			if($statement->rowCount())
 				 {
	 				while($result5 = $statement->fetchObject())
			      		{
						$id = $result5->id;
						$sid = $result5->seller_id;
						$pname = $result5->product_name;
						$img = $result5->image;
						
						$sql1 = "select owner_name from sellers where id = ".$sid;
                        			$STH = $connection->prepare($sql1);
                       				$STH -> execute();
                        			$result = $STH -> fetch();
                
						$bname = $result["owner_name"];
												
						echo $pname.";";
      					}	
										
  				}  
  			elseif(!$statement->rowCount())
  				{
    					echo "no rows";
  				}
	

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($connection);
?>
