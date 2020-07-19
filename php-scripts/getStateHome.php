<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
             		$city = "";
             		
      			$sth6 = $conn->prepare("select distinct(state) from seller_addresses");
    			  		if($sth6->execute())
      					{
					      	while($result6 = $sth6->fetchObject())
      						{
      							$contact = $result6->state;
      							$city = $city.",".$contact;
      						}
      					}
            
      			echo $city;
			$conn->commit();
            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>