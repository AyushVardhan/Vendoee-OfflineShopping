<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
             		$city = "";
             		$state = $_POST['state'];

      $sth5 = $conn->prepare("select distinct(city) from seller_addresses");
      if($sth5->execute())
      {
      	while($result5 = $sth5->fetchObject())
      	{
      		$contact = $result5->city;
      		$city = $city.",".$contact;
      	}
      }
      
      echo $city;

            }
        catch(PDOException $e)
            {
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>