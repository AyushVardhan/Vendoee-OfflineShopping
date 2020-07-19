<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');

        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
    
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();

                       
                     
                 	$name = $_POST['name'];
                	$bname = $_POST['bname'];
                        $email = $_POST['email'];
                        $uname = $_POST['uname'];
                        $password = $_POST['pass'];
                        $description = $_POST['desc'];
                        $shopopen = $_POST['sopen'];
                        $shopclose = $_POST['sclose'];
                        $file = $_POST['image'];
                        $cashless = $_POST['cash'];
                        
                	$addr = $_POST['addr'];
                        $lat = $_POST['lat'];
                        $lng = $_POST['lng'];
                        $city = $_POST['city'];
                        $state = $_POST['state'];
                        $country = $_POST['country'];
                        $pin = $_POST['pin'];
                        $ph = $_POST['ph'];
                        
                        $userEnteredPassword = $password;
			$password = password_hash($userEnteredPassword, PASSWORD_DEFAULT);
			
			
                 $q1= "SELECT email FROM sellers WHERE email= '$email'";
		$statement1 = $conn ->prepare($q1);
		$status1 = $statement1->execute();
		
		if (($status1) && ($statement1->rowCount() > 0))
		{

                 	$sql = "SELECT id FROM sellers where email="."'"."$email"."'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();
                        
                       	$pin = $result ["id"];
                       	
$sth = $conn->prepare("UPDATE sellers SET name='$name',owner_name='$bname',email='$email',username='$uname',password='$password',description='$description',image=:image,cashless='$cashless',opening_time='$shopopen',closing_time='$shopclose' WHERE id=$pin");
                        $sth->bindParam(':image',$file);
$sth->execute();

$sth1 = $conn->prepare("UPDATE seller_contacts SET phone_no=$ph WHERE seller_id=$pin");

                        if($sth1->execute())
                        {
                        	echo $pin;
                        }
                       	
		}else{
		
$sth = $conn->prepare("INSERT INTO sellers(name,owner_name,email,username,password,description,image,cashless,opening_time,closing_time)VALUES(:name,:bname,:email,:uname,:password,:description,:image,:cash,:shopopen,:shopclose)");

                        $sth->bindParam(':cash',$cashless);
                        $sth->bindParam(':name',$name);
                        $sth->bindParam(':bname',$bname);
                        $sth->bindParam(':email',$email);
                        $sth->bindParam(':uname',$uname);
                        $sth->bindParam(':password',$password);
                        $sth->bindParam(':description',$description);
                        $sth->bindParam(':shopopen',$shopopen);
                        $sth->bindParam(':shopclose',$shopclose);
                        $sth->bindParam(':image',$file);

			$pincd = 0;

                        if($sth->execute())
                        {
                        
                       	                        
                        $sql = "SELECT id FROM sellers where email="."'"."$email"."'";
                        $STH = $conn->prepare($sql);
                        $STH -> execute();
                        $result = $STH -> fetch();
                        $pincd = $result ["id"];
                        $sid = $pincd;
                        
                        $sth1 = $conn->prepare("INSERT INTO seller_addresses(seller_id,address,lat,lng,city,state,country,pin)VALUES(:sid,:addr,:lat,:lng,:city,:state,:country,:pin)");

                        $sth1->bindParam(':sid',$sid);
                        $sth1->bindParam(':addr',$addr);
                        $sth1->bindParam(':lat',$lat);
                        $sth1->bindParam(':lng',$lng);
                        $sth1->bindParam(':city',$city);
                        $sth1->bindParam(':state',$state);
                        $sth1->bindParam(':country',$country);
                        $sth1->bindParam(':pin',$pin);


                        if($sth1->execute()){
                        
                        $sth2 = $conn->prepare("INSERT INTO seller_contacts(seller_id,phone_no)VALUES(:sid,:ph)");

                        $sth2->bindParam(':sid',$sid);
                        $sth2->bindParam(':ph',$ph);

                        $sth2->execute();
                        echo $sid;
                        

                        }
                        
                        
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
