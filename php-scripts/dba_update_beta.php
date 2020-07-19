<?php
	define('HOST','localhost');
	define('USER','psVishalPs');
	define('PASS','vendoe1232');
	define('DB','vendoe');
        try {
                 $conn = new PDO("mysql:host=localhost;dbname=vendoe", USER, PASS);
                 $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
                 $conn->beginTransaction();
    
                     	$id = $_POST['id'];
                 	$name = $_POST['name'];
                	$bname = $_POST['bname'];
                        $email = $_POST['email'];
                        $description = $_POST['desc'];
                        $shopopen = $_POST['sopen'];
                        $shopclose = $_POST['sclose'];
                        $file = $_POST['image'];
                        $cashless = $_POST['cash'];
                        $deliver = $_POST['deliver'];
                        $pincode  = $_POST['pincode'];                    

$sth = $conn->prepare("UPDATE sellers SET name=:name,owner_name=:bname,email=:email,description=:description,image=:image,opening_time=:shopopen,closing_time=:shopclose,cashless=:cash,delivery=:deliver WHERE id = $id");

                        $sth->bindParam(':name',$name);
                        $sth->bindParam(':bname',$bname);
                        $sth->bindParam(':email',$email);
                        $sth->bindParam(':description',$description);
                        $sth->bindParam(':shopopen',$shopopen);
                        $sth->bindParam(':shopclose',$shopclose);
                        $sth->bindParam(':image',$file);
                        $sth->bindParam(':cash',$cashless);
                        $sth->bindParam(':deliver',$deliver);
                                                
                        if($sth->execute())
                        {

$sth1 = $conn->prepare("UPDATE seller_addresses SET pin=:pin WHERE seller_id = $id");

$sth1->bindParam(':pin',$pincode);

if($sth1->execute()){
                        	echo 1;
}
                        }else{
                        	echo "Error";
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