<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
                       
                     
                 	$sid = $_POST['sid'];
                	$addr = $_POST['addr'];
                        $lat = $_POST['lat'];
                        $lng = $_POST['lng'];
                        $city = $_POST['city'];
                        $state = $_POST['state'];
                        $country = $_POST['country'];
                        $pin = $_POST['pin'];

$sth = $conn->prepare("INSERT INTO seller_addresses(seller_id,address,lat,lng,city,state,country,pin)VALUES(:sid,:addr,:lat,:lng,:city,:state,:country,:pin)");

                        $sth->bindParam(':sid',$sid);
                        $sth->bindParam(':addr',$addr);
                        $sth->bindParam(':lat',$lat);
                        $sth->bindParam(':lng',$lng);
                        $sth->bindParam(':city',$city);
                        $sth->bindParam(':state',$state);
                        $sth->bindParam(':country',$country);
                        $sth->bindParam(':pin',$pin);


                        $sth->execute();
                        echo 1;
                        
                        $conn->commit();

            }
        catch(PDOException $e)
            {
            $conn->rollback();
             echo "Error is: " . $e->getMessage();
             }

	  mysqli_close($con);
?>